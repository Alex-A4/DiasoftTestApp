package alexa4.friendphoto.repositories;

import android.util.Log;

import alexa4.friendphoto.controllers.FriendsController;
import alexa4.friendphoto.controllers.PhotosController;
import alexa4.friendphoto.models.User;
import alexa4.friendphoto.ui.ActivityCallback;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Repository that contains both friends and photos controllers,
 * this repository makes network calls and db calls
 */
public class DataRepository implements AuthCallback {
    private static final String TAG = "DATA_REPOSITORY";
    // Network client
    private final OkHttpClient client = new OkHttpClient();
    // Data controllers
    private final FriendsController mFriends = new FriendsController();
    private final PhotosController mPhotos = new PhotosController();
    private User mUser;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    // The link to UI
    private ActivityCallback mUI;

    public DataRepository(ActivityCallback UI) {
        mUI = UI;
    }
    //    public static final MediaType JSON
//            = MediaType.get("application/json; charset=utf-8");


    /**
     * RX call of downloading photos of specified friend
     *
     * @param friendId id of friend whose photos need to download
     */
    public void getPhotosObservable(int friendId) {
        mDisposable.add(Observable.<Boolean>create(emitter -> {
                    Request request = new Request.Builder()
                            .url("https://api.vk.com/method/photos.get?owner_id="
                                    + friendId + "&album_id=wall&v=5.95&access_token="
                                    + mUser.mToken)
                            .build();
                    try (Response response = client.newCall(request).execute()) {
                        Log.d(TAG, response.body().toString());

                        if (mPhotos.isIdenticalId(friendId))
                            emitter.onNext(false);
                        else {
                            mPhotos.setOwnerId(friendId);
                            mPhotos.setPhotos(response.body().string());
                            emitter.onNext(true);
                        }
                    }
                    emitter.onComplete();
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mUI::photosDownloaded,
                                e -> mUI.photosDownloaded(false))
        );
    }

    /**
     * Download information about friends
     */
    public void downloadFriends() {
        mDisposable.add(Observable.<Boolean>create(emitter -> {
                    Request request = new Request.Builder()
                            .url("https://api.vk.com/method/friends.get?fields=photo_100&v=5.95&access_token="
                                    + mUser.mToken)
                            .build();
                    try (Response response = client.newCall(request).execute()) {
                        Log.d(TAG, response.body().toString());
                        mFriends.setFriends(response.body().toString());
                        emitter.onNext(true);
                    }
                    emitter.onComplete();
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(mUI::friendsDownloaded,
                                e -> mUI.friendsDownloaded(false))
        );
    }


    /**
     * Dispose all subscription and clear data
     */
    public void dispose() {
        mDisposable.dispose();
        mFriends.dispose();
        mPhotos.dispose();
    }


    /**
     * Get data of user authentication process and pull result to UI through callback
     * @param fragments the result string of authentication process
     */
    @Override
    public void sendUserAuthFragment(String fragments) {
        Log.d(TAG, fragments);
        String[] data = fragments.split("&");

        // If user cancel authentication
        if (data[0].contains("error")) {
            mUI.userAuthenticated(false);
        }

        // Initialize user data
        mUser = new User(data[0].replace("access_token=", ""),
                Integer.parseInt(data[2].replace("user_id=", "")));
        mUI.userAuthenticated(true);
    }
}
