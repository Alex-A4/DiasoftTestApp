package alexa4.friendphoto.ui;


/**
 * Callback interface to communicate with Activity to update UI
 */
public interface ActivityCallback {
    /**
     * If user authenticated, then hide webView and start to loading friends
     * else show starting page
     */
    void userAuthenticated(boolean isAuth);

    /**
     * If friends downloaded then send true and show them, else show some error
     * @param isDownloaded
     */
    void friendsDownloaded(boolean isDownloaded);

    /**
     * If photos downloaded, then send true and show them, else show some error
     * @param isDownloaded
     */
    void photosDownloaded(boolean isDownloaded);
}
