package alexa4.friendphoto.models;


/**
 * Basic model where stores information about user
 */
public class User {
    public final String mToken;
    public final int mId;

    public User(String token, int id) {
        mToken = token;
        mId = id;
    }
}
