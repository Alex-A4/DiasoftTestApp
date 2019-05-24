package alexa4.friendphoto.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alexa4.friendphoto.models.Photo;

/**
 * Controller that contains list of photos of one friend
 */
public class PhotosController {
    private static final String TAG = "PhotosController";
    public final ArrayList<Photo> mPhotos = new ArrayList<>();
    // Owner id needs to check is photos already downloaded
    private int mOwnerId = 0;

    /**
     * Set up photos list from input json string
     *
     * @param jsonString json string from response
     */
    public void setPhotos(String jsonString) {
        Log.d(TAG, jsonString);
        mPhotos.clear();

        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("response");
            JSONArray array = json.getJSONArray("items");
            for (int i = 0; i < array.length(); i++)
                mPhotos.add(new Photo(array.getJSONObject(i)));

        } catch (JSONException e) {
            e.printStackTrace();
            mPhotos.clear();
        }
    }

    /**
     * Check is otherId and this id is equals
     *
     * @return true if otherId == mOwnerId, else false
     */
    public boolean isIdenticalId(int otherId) {
        return mOwnerId == otherId;
    }

    // Default setter
    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public void dispose() {
        mPhotos.clear();
    }
}
