package alexa4.friendphoto.controllers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import alexa4.friendphoto.models.Friend;


/**
 * Controller that contains list of friends and can restore it from json string
 */
public class FriendsController {
    private static final String TAG = "FriendsController";
    public final ArrayList<Friend> mFriends = new ArrayList<>();


    /**
     * Set up friends list from input json string
     *
     * @param jsonString input string with friends
     */
    public void setFriends(String jsonString) {
        Log.d(TAG, jsonString);
        mFriends.clear();
        try {
            JSONObject json = new JSONObject(jsonString).getJSONObject("response");
            int count = json.getInt("count");
            JSONArray array = json.getJSONArray("items");
            for (int i = 0; i < count; i++)
                mFriends.add(new Friend(array.getJSONObject(i)));

        } catch (JSONException e) {
            e.printStackTrace();
            mFriends.clear();
        }
    }

    public void dispose() {
        mFriends.clear();
    }
}
