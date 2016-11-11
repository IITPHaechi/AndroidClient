package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dnay2 on 2016-10-21.
 */
public class JoyStickDir extends View {

    //static Value
    float width;
    float height;
    int percentage;

    //position Value
    float posX;
    float posY;
    float centerX;
    float centerY;
    float min;

    //calculated Value
    float buttonRadius;
    double power = -1;
    double angle = -1;

    //그림
    int padColor;
    int buttonColor;
    int padResId;
    int btnResId;
    Bitmap padImg;
    Bitmap btnImg;


    OnJoyStickDirListener listener;

    public interface OnJoyStickDirListener{
        void onMove();
    }

    public JoyStickDir(Context context) {
        super(context, null);
    }

    public JoyStickDir(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public JoyStickDir(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

    }

    public void setPadResId(int padResId) {
        this.padResId = padResId;
        padImg = BitmapFactory.decodeResource(getResources(), padResId);
    }

    public void setBtnResId(int btnResId) {
        this.btnResId = btnResId;
        btnImg = BitmapFactory.decodeResource(getResources(), btnResId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerX = width/2;
        centerY = height/2;
        min = Math.min(width, height);
        posX = centerX;
        posY = centerY;
        buttonRadius = (min / 2f * (percentage/100f));
    }
}
