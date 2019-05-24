package alexa4.friendphoto.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import alexa4.friendphoto.models.User;


/**
 * Provider that can communicate with SharedPreferences
 */
public class StorageProvider {
    // Instance of preferences to r/w data
    private SharedPreferences mPreferences;
    private static final String TAG = "StorageProvider";

    /**
     * Check is provider initialized
     */
//    public boolean isInitialized() {
//        return mPreferences != null;
//    }

    /**
     * Initialize provider by preferences instance
     */
    public void initialize(SharedPreferences prefs) {
        mPreferences = prefs;
    }

    /**
     * Save string with parameters to SharedPreferences
     */
    public void saveString(String name, String data) {
        mPreferences.edit().putString(name, data).apply();
    }

    /**
     * Check is user authenticated
     */
    public boolean isUserAuthenticated() {
        return mPreferences.getBoolean("isAuthenticated", false);
    }

    /**
     * Get data about user and return its reference
     * if there is not some data, then throw error
     */
    public User getUserData() throws Exception {
        int id = mPreferences.getInt("user_id", 0);
        String token = mPreferences.getString("access_token", null);
        if (id == 0 || token == null) {
            Log.e(TAG, "Incorrect data: user_id = " + id + ", token = " + token);
            throw new Exception();
        }

        Log.d(TAG, "Authentication success: " + id + "\n" + token);
        return new User(token, id);
    }

    /**
     * Save data about user
     */
    public void saveUserData(User user) {
        mPreferences.edit().putString("access_token", user.mToken)
                .putInt("user_id", user.mId)
                .putBoolean("isAuthenticated", true)
                .apply();

        Log.d(TAG, "User Data saved: " + user.mId + "\n" + user.mToken);
    }
}
