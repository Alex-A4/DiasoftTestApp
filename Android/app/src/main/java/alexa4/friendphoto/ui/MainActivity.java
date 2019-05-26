package alexa4.friendphoto.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import alexa4.friendphoto.R;
import alexa4.friendphoto.repositories.DataRepository;


public class MainActivity extends AppCompatActivity implements ActivityCallback, RepositoryProvider {
    private static final String TAG = "MainActivity";
    private DataRepository mRepository;
    private AuthFragment mAuthFragment;
    private FriendsFragment mFriendsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRepository = new DataRepository(MainActivity.this);
        mAuthFragment = new AuthFragment();
        mFriendsFragment = new FriendsFragment();

        checkAuthenticationAndInitializeUI();
    }

    /**
     * Check is user already authenticated and then set up starting screen
     */
    private void checkAuthenticationAndInitializeUI() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // If user authenticated, open friends page else load webView
        if (mRepository.checkAuthentication()) {
            transaction.replace(R.id.fragment_root, mFriendsFragment)
                    .commit();
            setTitle("Friends");
        } else {
            transaction.replace(R.id.fragment_root, mAuthFragment)
                    .commit();
            setTitle("Authentication");
        }
    }

    /**
     * Callback from repository that sign is user authenticated
     * If true, then show friends page else show toast
     *
     * @param isAuth
     */
    @Override
    public void authenticateUser(boolean isAuth) {
        if (!isAuth) {
            Log.d(TAG, "User is not authenticated");
            Toast.makeText(this, "Unable to load page, try later",
                    Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "User is authenticated");
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.replace(R.id.fragment_root, mFriendsFragment)
                    .commit();
            setTitle("Friends");
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        mRepository.dispose();
        super.onDestroy();
    }

    // Provide repository for fragments
    @Override
    public DataRepository getRepository() {
        return mRepository;
    }
}