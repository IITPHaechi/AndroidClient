package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class HaechiWebView extends WebView {

    public HaechiWebView(Context context) {
        super(context);
    }

    public HaechiWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HaechiWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        final int offset = computeHorizontalScrollOffset();
        final int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if(range == 0) return false;
        if(direction < 0){
            return offset>0;
        } else {
            return offset < range-1;
        }
    }
}
