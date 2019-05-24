package alexa4.friendphoto.ui;


/**
 * Callback interface to communicate with Activity to update UI
 */
public interface ActivityCallback {
    /**
     * If user authenticated, then hide webView and start to loading friends
     * else show starting page
     */
    void authenticateUser(boolean isAuth);
}
