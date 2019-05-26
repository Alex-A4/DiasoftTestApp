package alexa4.friendphoto.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Basic model that contains information about one friend
 */
public class Friend {
    // Default values of friend
    public final int mId;
    public final String mName;
    public final String mLastName;
    public final String mPhotoUrl; // url of profile photo
    public final String mStatus;

    /**
     * Default constructor.
     * Uses to restore from DB
     */
    public Friend(int id, String name, String lastName,
                  String photoUrl, String status) {
        mId = id;
        mName = name;
        mLastName = lastName;
        mPhotoUrl = photoUrl;
        mStatus = status;
    }

    // Constructor to restore data from JSON
    public Friend(JSONObject json) throws JSONException {
        mId = json.getInt("id");
        mStatus = json.getString("status");
        mName = json.getString("first_name");
        mLastName = json.getString("last_name");
        mPhotoUrl = json.getString("photo_100");
    }
}
