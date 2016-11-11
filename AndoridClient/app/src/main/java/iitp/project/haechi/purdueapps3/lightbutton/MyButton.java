package iitp.project.haechi.purdueapps3.lightbutton;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by dnay2 on 2016-10-19.
 */
public class MyButton extends TextView {

    OnButtonListener listener;
    GestureDetector gestureDetector;
    public int color = Color.RED;

    public MyButton(Context context) {
        super(context);
        setBackgroundColor(color);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(color);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(color);
    }

    public interface OnButtonListener {
        void onButton(int color);
    }

    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }

    public void setListener(OnButtonListener listener) {
        this.listener = listener;
    }

    @Override
    public void setBackgroundColor(int colors) {
        super.setBackgroundColor(color);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                if (listener != null) listener.onButton(color);
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                return false;
////                break;
//        }
//
//
//        return true;
//    }
//


    Handler handler = new Handler();
    boolean pushingDown = false;
    Runnable repeater = new Runnable() {
        @Override
        public void run() {
            if(pushingDown){
                handler.postDelayed(this, 400);
                if (listener != null) listener.onButton(color);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                pushingDown = true;
                handler.post(repeater);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pushingDown = false;
                break;

        }

        if (listener != null) listener.onButton(color);

        return true;
    }
}
