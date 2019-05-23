package alexa4.friendphoto.repositories;

/**
 * Auth callback which sends from webView and return result of user
 * authentication (error or success)
 */
public interface AuthCallback {
    /**
     * Send fragment from url.
     * This fragment contains error, if user cancel authentication
     * or access token and user_id if everything OK
     */
    void sendUserAuthFragment(String fragment);
}
