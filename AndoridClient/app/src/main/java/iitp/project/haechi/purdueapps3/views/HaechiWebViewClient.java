package iitp.project.haechi.purdueapps3.views;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class HaechiWebViewClient extends WebViewClient {

    private HaechiWebView webView;
    private String url;

    public HaechiWebViewClient(HaechiWebView webView, String url) {
        this.webView = webView;
        this.url = url;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
    }
}
