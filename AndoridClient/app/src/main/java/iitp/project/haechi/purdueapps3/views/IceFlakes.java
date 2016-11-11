package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dnay2 on 2016-11-09.
 */

public class IceFlakes extends ViewGroup {

    //field

    //included view
    private View mainView;
    private View handleView;


    public IceFlakes(Context context) {
        super(context, null);
    }

    public IceFlakes(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public IceFlakes(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs){

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
