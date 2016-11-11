package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-11-09.
 */

public class WebPagerAdapter extends PagerAdapter {

    private Context context;
    private String[] items = new String[3];

    private static final int NORMAL = 0;
    private static final int THERMO = 1;
    private static final int IRADIATE = 2;

    public WebPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {
        View view = null;
        view = View.inflate(context, R.layout.child_webview, null);
        WebView wv = (WebView) view.findViewById(R.id.webView);
        new MyWebViewClient(wv, items[pos]);
        container.addView(view);
        return true;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class MyWebViewClient extends WebViewClient {

        MyWebViewClient(WebView wb, String url) {
            shouldOverrideUrlLoading(wb, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
