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
    // Text of photo
    public final String mText;

    public Photo(JSONObject json) throws JSONException {
        mText = json.getString("text");

        JSONArray array = json.getJSONArray("sizes");
        mBigSize = array.getJSONObject(4).getString("url");
        mSmallSize = array.getJSONObject(0).getString("url");

    }
}
