package alexa4.friendphoto.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import alexa4.friendphoto.R;
import alexa4.friendphoto.repositories.AuthCallback;
import alexa4.friendphoto.repositories.DataRepository;


public class MainActivity extends AppCompatActivity implements ActivityCallback{
    private WebView mWebView;
    private DataRepository mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRepository = new DataRepository(MainActivity.this);

        mWebView = findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient(mRepository));
    }

    // Load page to webView, if user not authenticated
    void loadAuthPage() {
        // Scope = 65538. Friends + offline
        mWebView.loadUrl("https://oauth.vk.com/authorize?client_id=6994466&" +
                "display=page&scope=65538&response_type=token&v=5.95");
    }

    @Override
    protected void onDestroy() {
        mRepository.dispose();
        super.onDestroy();
    }

    @Override
    public void userAuthenticated(boolean isAuth) {
        if (!isAuth)
            loadAuthPage();
        else {
            //TODO: hide webView
            mRepository.downloadFriends();
        }
    }

    @Override
    public void friendsDownloaded(boolean isDownloaded) {
        //TODO: show friends page

    }

    @Override
    public void photosDownloaded(boolean isDownloaded) {
        //TODO: show photos page
    }
}

/**
 * Own implementation of client to handle input requests
 */
class MyWebViewClient extends WebViewClient {
    private AuthCallback mCallback;

    public MyWebViewClient(AuthCallback callback) {
        mCallback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        System.out.println("request.getUrl().toString() = " + request.getUrl().toString());
        view.loadUrl(request.getUrl().toString());

        System.out.println("HOST: "+request.getUrl().getHost()+request.getUrl().getPath());
        // If there is result of authentication, then send it callback
        if ((request.getUrl().getHost()+request.getUrl().getPath()).equals("oauth.vk.com/blank.html")) {
            mCallback.sendUserAuthFragment(request.getUrl().getFragment());
        }

        return true;
    }

    // For old devices
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri request = Uri.parse(url);

        System.out.println("request.getUrl().toString() = " + request.toString());
        view.loadUrl(request.toString());

        System.out.println("HOST: "+request.getHost()+request.getPath());
        // If there is result of authentication, then send it callback
        if ((request.getHost()+request.getPath()).equals("oauth.vk.com/blank.html")) {
            mCallback.sendUserAuthFragment(request.getFragment());
        }

        return true;
    }
}