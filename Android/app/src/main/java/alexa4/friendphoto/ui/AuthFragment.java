package alexa4.friendphoto.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import alexa4.friendphoto.R;
import alexa4.friendphoto.repositories.AuthCallback;

public class AuthFragment extends Fragment {
    private WebView mWebView;
    private AuthCallback mCallback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.auth_fragment, container, false);

        mCallback = ((RepositoryProvider) getActivity()).getRepository();

        mWebView = root.findViewById(R.id.webView);
        mWebView.setWebViewClient(new MyWebViewClient(mCallback));

        loadAuthPage();

        return root;
    }

    // Load page to webView, if user not authenticated
    void loadAuthPage() {
        // Scope = 65538. Friends + offline
//        if (mWebView.getUrl() != null && !mWebView.getUrl().startsWith("https://oauth.vk.com/authorize"))
        mWebView.loadUrl("https://oauth.vk.com/authorize?client_id=6994466&" +
                "display=page&scope=65538&response_type=token&v=5.95");
    }

    @Override
    public void onDestroy() {
        mCallback = null;
        super.onDestroy();
    }

    /**
     * Own implementation of client to handle input requests
     */
    private class MyWebViewClient extends WebViewClient {
        private AuthCallback mCallback;

        MyWebViewClient(AuthCallback callback) {
            mCallback = callback;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            System.out.println("request.getUrl().toString() = " + request.getUrl().toString());
            view.loadUrl(request.getUrl().toString());

            System.out.println("HOST: " + request.getUrl().getHost() + request.getUrl().getPath());
            // If there is result of authentication, then send it callback
            if ((request.getUrl().getHost() + request.getUrl().getPath())
                    .equals("oauth.vk.com/blank.html")) {
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

            System.out.println("HOST: " + request.getHost() + request.getPath());
            // If there is result of authentication, then send it callback
            if ((request.getHost() + request.getPath()).equals("oauth.vk.com/blank.html")) {
                mCallback.sendUserAuthFragment(request.getFragment());
            }

            return true;
        }
    }
}