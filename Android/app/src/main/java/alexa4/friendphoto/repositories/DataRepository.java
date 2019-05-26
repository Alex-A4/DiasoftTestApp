package alexa4.friendphoto.repositories;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import alexa4.friendphoto.controllers.DbController;
import alexa4.friendphoto.controllers.FriendsController;
import alexa4.friendphoto.controllers.PhotosController;
import alexa4.friendphoto.models.Friend;
import alexa4.friendphoto.models.Photo;
import alexa4.friendphoto.models.User;
import alexa4.friendphoto.ui.ActivityCallback;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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

    private DbController mDbController;
    private StorageProvider mStorage = new StorageProvider();

    // The link to UI
    private ActivityCallback mUI;

    public DataRepository(ActivityCallback UI) {
        mUI = UI;
        mDbController = new DbController(mUI.getContext());
        mStorage.initialize(mUI.getContext()
                .getSharedPreferences("DataRepository", Context.MODE_PRIVATE));
    }

    /**
     * RX call of downloading photos of specified friend
     *
     * @param friendId id of friend whose photos need to download
     */
    public Observable<ArrayList<Photo>> downloadPhotos(int friendId) {
        return Observable.<ArrayList<Photo>>create(emitter -> {
            Request request = new Request.Builder()
                    .url("https://api.vk.com/method/photos.get?owner_id="
                            + friendId + "&album_id=wall&v=5.95&access_token="
                            + mUser.mToken)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (!mPhotos.isIdenticalId(friendId) && response.isSuccessful()) {
                    mPhotos.setOwnerId(friendId);
                    mPhotos.setPhotos(response.body().string());
                }
            }
            emitter.onNext(mPhotos.mPhotos);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()
                );
    }

    /**
     * Return observable which should download information about friends
     */
    public Observable<ArrayList<Friend>> downloadFriends() {
        return Observable.<ArrayList<Friend>>create(emitter -> {
            // Read from DB if there is first launch
            if (!mFriends.isLoaded())
                mFriends.setFromDb(mDbController.readFriends());

            // Download from network if there is no data after DB reading
            if (!mFriends.isLoaded()) {

                Request request = new Request.Builder()
                        .url("https://api.vk.com/method/friends.get?fields=photo_100, status&v=5.95&access_token="
                                + mUser.mToken)
                        .build();
                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful()) {
                        mFriends.setFriends(response.body().string());
                        mDbController.saveFriends(mFriends.mFriends);
                    }
                }

            }
            emitter.onNext(mFriends.mFriends);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()
                );
    }


    /**
     * Dispose all subscription and clear data
     */
    public void dispose() {
        mFriends.dispose();
        mPhotos.dispose();
    }


    /**
     * Get data of user authentication process and pull result
     * to UI through callback
     *
     * @param fragments the result string of authentication process
     */
    @Override
    public void sendUserAuthFragment(String fragments) {
        Log.d(TAG, fragments);
        String[] data = fragments.split("&");

        // If user cancel authentication
        if (data[0].contains("error")) {
            mUI.authenticateUser(false);
            return;
        }

        // Initialize user data
        mUser = new User(data[0].replace("access_token=", ""),
                Integer.parseInt(data[2].replace("user_id=", "")));
        mStorage.saveUserData(mUser); //save data to prefs
        mUI.authenticateUser(true); //notify UI
    }

    /**
     * Check is user authenticated and get its info if true
     * This method must be called before any transactions
     *
     * @return is user authenticated
     */
    public boolean checkAuthentication() {
        if (mStorage.isUserAuthenticated()) {
            try {
                mUser = mStorage.getUserData();

                return true;
            } catch (Exception e) {
                return false;
            }
        } else return false;
    }
}
