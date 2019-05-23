package alexa4.friendphoto.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alexa4.friendphoto.models.Photo;

/**
 * Controller that contains list of photos of one friend
 */
public class PhotosController {
    public final ArrayList<Photo> mPhotos = new ArrayList<>();
    // Owner id needs to check is photos already downloaded
    private int mOwnerId;

    /**
     * Set up photos list from input json string
     *
     * @param jsonString json string from response
     */
    public void setPhotos(String jsonString) {
        mPhotos.clear();

        // TODO: make list auto-complemented while scrolling
        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("response");
            int count = json.getInt("count");
            JSONArray array = json.getJSONArray("items");
            for (int i = 0; i < count; i++)
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
