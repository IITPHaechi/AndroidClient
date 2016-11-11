package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by dnay2 on 2016-11-09.
 */

public class PagerSelector extends LinearLayout {

    int itemsNumber;

    public PagerSelector(Context context) {
        super(context, null);
    }

    public PagerSelector(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PagerSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize(){
        LayoutParams llp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(llp);
    }

    private void setSelector(int itemsNumber){
        this.itemsNumber = itemsNumber;
        Paint mPaint = new Paint();

        for(int i=0;i<itemsNumber;i++) {

        }
    }


}
