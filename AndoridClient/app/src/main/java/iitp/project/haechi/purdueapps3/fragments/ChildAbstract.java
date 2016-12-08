package iitp.project.haechi.purdueapps3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iitp.project.haechi.purdueapps3.R;
import iitp.project.haechi.purdueapps3.views.HaechiWebView;
import iitp.project.haechi.purdueapps3.views.HaechiWebViewClient;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class ChildAbstract extends Fragment {

    HaechiWebView webView = null;
    View v=null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, int layoutResource) {
        super.onCreateView(inflater, container, savedInstanceState);
        v = inflater.inflate(R.layout.child_webview, null);
        webView = (HaechiWebView) v.findViewById(R.id.webView);
        return v;
    }


    public void stopVideo() {
        if (webView != null) {
            webView.stopLoading();
            Log.d("test", this.getClass().toString() + "  video is stopped");
        }

    }

    public void loadVideo(String url) {
        if(v !=null && webView == null){
            webView = (HaechiWebView) v.findViewById(R.id.webView);
            webView.setWebViewClient(new HaechiWebViewClient(webView, URL));
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            Log.d("test", this.getClass().toString() + "  video is loaded");
        } else if(v != null && webView != null){
            webView.setWebViewClient(new HaechiWebViewClient(webView, URL));
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
            Log.d("test", this.getClass().toString() + "  video is loaded");
        }

    }

    public void loadVideo() {

    }
}
