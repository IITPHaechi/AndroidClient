package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.ArrayList;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-11-09.
 */

public class WebPagerAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<WebView> items;

    private static final String NORMAL = "http://172.24.1.1:9090/stream";
    private static final String THERMO = "http://172.24.2.139:9090/stream";
    private static final String IRADIATE = "http://172.24.1.1:9090/stream";

    public WebPagerAdapter(Context context, ArrayList<WebView> items) {
        this.context = context;
        this.items = items;
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
    public void finishUpdate(ViewGroup container) {
        Log.d("test", "finishUpdate");
        super.finishUpdate(container);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int pos) {
        View view = null;
        view = View.inflate(context, R.layout.child_webview, null);
        LinearLayout childContainer = (LinearLayout) view.findViewById(R.id.child_container);
        childContainer.addView(items.get(pos));
//        WebView wv = (WebView) view.findViewById(R.id.webView);
//        String url = "";
//
//        if(!url.equals("")){
//            wv.setWebViewClient(new MyWebViewClient());
//            wv.getSettings().setJavaScriptEnabled(true);
//            wv.loadUrl(url);
//            Log.d("test", "WebViewClient is made by " + url);
//        }
//
//        Log.d("test", "view pager 작동 확인");
        container.addView(view);
        return true;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.d("test", "web view is working OK");
            return true;
        }
    }
}
