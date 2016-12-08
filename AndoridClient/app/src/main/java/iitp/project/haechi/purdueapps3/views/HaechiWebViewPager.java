package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dnay2 on 2016-12-07.
 */

public class HaechiWebViewPager extends ViewPager {

    public HaechiWebViewPager(Context context) {
        super(context);
    }

    public HaechiWebViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if(v instanceof HaechiWebView){
            return v.canScrollHorizontally(-dx);
        } else {
            return super.canScroll(v,checkV,dx,x,y);
        }
    }
}
