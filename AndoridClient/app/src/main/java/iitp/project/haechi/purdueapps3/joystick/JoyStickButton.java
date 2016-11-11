package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dnay2 on 2016-10-21.
 */
public class JoyStickButton extends View {

    public final static int BACK_BUTTON = 101;
    public final static int FRONT_BUTTON = 102;

    int funtionName = 0;

    OnJoyStickButtonListener listener;

    Bitmap padBitmap = null;

    Bitmap buttonBitmap = null;

    public JoyStickButton(Context context) {
        super(context, null);
    }

    public JoyStickButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public JoyStickButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    public interface OnJoyStickButtonListener {
        void onPressed();
    }

    private boolean flag = false;
    Handler handler = new Handler();
    Runnable repeater = new Runnable() {
        @Override
        public void run() {
            if (flag) {
                handler.postDelayed(repeater, 400);
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                flag = true;
                handler.post(repeater);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                flag = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }


    public void setListener(OnJoyStickButtonListener listener) {
        this.listener = listener;
    }

    public void setFuntionName(int funtionName) {
        this.funtionName = funtionName;
    }
}
