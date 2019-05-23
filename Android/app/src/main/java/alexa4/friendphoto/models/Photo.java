package alexa4.friendphoto.models;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Basic model that contains information about one photo
 */
public class Photo {
    // Different sizes of image to show in different places
    public final String mBigSize;
    public final String mSmallSize;

    public Photo(JSONObject json) throws JSONException {
        JSONArray array = json.getJSONArray("sizes");
        mBigSize = array.getJSONObject(4).getString("url");
        mSmallSize = array.getJSONObject(0).getString("url");
    }
}
