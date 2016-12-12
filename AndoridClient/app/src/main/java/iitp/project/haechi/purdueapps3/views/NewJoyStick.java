package iitp.project.haechi.purdueapps3.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-10-14.
 */
public class NewJoyStick extends View {

    //기본 셋팅
    private float width;            //너비
    private float height;           //높이
    private int padColor;           // 배경색
    private int buttonColor;        //조이스틱 색

    //value
    public float posY; //위치 pos
    public float centerY; //기준  = height/2
    float sensitiveDis; // 민감도 설정
    int sensitivites = 5; //민감도 값이 높을 수록 민감도 하향
    float power; // 파워
    long repeatTime = 400;


    //작은 버튼 크기
    int percentage = 40;
    float buttonhHeight;

    Rect padRect, buttonRect;
    Rect temp;
    Paint padPaint, buttonPaint;            //draw 객체

    Bitmap padBGBitmap =null, buttonBitmap =null;

    private boolean pushingDown = false;
    private Handler handler = new Handler();
    private Runnable repeater = new Runnable() {
        @Override
        public void run() {
            if(pushingDown){
                postDelayed(repeater, 400);
                if(listener != null) listener.onMove(listener, power);
            }
        }
    };

    private void setPosY(float getY){
        posY = getY;
    }

    public void setRepeatTime(long repeatTime) {
        this.repeatTime = repeatTime;
    }

    //리스너 설정
    NewJoyStickListener listener = null;

    public interface NewJoyStickListener{
        void onMove(NewJoyStickListener listener, float power);
    }


    public NewJoyStick(Context context) {
        super(context);
        init(context, null);
    }

    public NewJoyStick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        padColor = Color.BLACK;
        buttonColor = Color.RED;

        temp = new Rect();

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewJoyStick);
            if (typedArray != null) {
                padColor = typedArray.getColor(R.styleable.NewJoyStick_padColor, Color.BLACK);
                buttonColor = typedArray.getColor(R.styleable.NewJoyStick_buttonColor, Color.RED);
                sensitivites = typedArray.getInteger(R.styleable.NewJoyStick_sensitivity, 25);
                int padResId = typedArray.getResourceId(R.styleable.NewJoyStick_padDrawable, -1);
                int buttonResId = typedArray.getResourceId(R.styleable.NewJoyStick_buttonDrawable, -1);

                if(padResId > -1) {
                    padBGBitmap = BitmapFactory.decodeResource(getResources(), padResId);
                    padBGBitmap.isRecycled();
                }
                if(buttonResId > -1){
                    buttonBitmap = BitmapFactory.decodeResource(getResources(), buttonResId);
                    buttonBitmap.isRecycled();
                }
                typedArray.recycle();
            }
        }

        padPaint = new Paint();
        padPaint.setStyle(Paint.Style.FILL);
        padPaint.setAntiAlias(true);
        padPaint.setColor(padColor);

        buttonPaint = new Paint();
        buttonPaint.setStyle(Paint.Style.FILL);
        buttonPaint.setAntiAlias(true);
        buttonPaint.setColor(buttonColor);

        padRect = new Rect(0,0,(int)width, (int)height);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerY = height / 2;
        posY = centerY;
//        buttonhHeight = height * percentage / 50f;
        buttonhHeight = width/2;
        sensitiveDis = height / 4;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        buttonhHeight = height * percentage / 200;
        int buttonTop = (int)(posY - buttonhHeight);
        int buttonBottom = (int)(buttonTop + buttonhHeight*2);

        if(buttonTop < 0) {
            buttonTop = 0;
            buttonBottom = (int)(buttonhHeight*2);
        }
        if(buttonBottom > height) {
            buttonBottom = (int)height;
            buttonTop = (int) (height - buttonhHeight*2);
        }

        if(canvas == null) return;

        if(padBGBitmap == null){
            padPaint.setColor(padColor);
            canvas.drawRect(padRect, padPaint);
        } else {
            temp.set(0,0,(int)width, (int) height);
            canvas.drawBitmap(padBGBitmap, null, temp, padPaint);
        }

        if(buttonBitmap == null){
            buttonRect = new Rect(0, buttonTop, (int) width, buttonBottom);
            canvas.drawRect(buttonRect, buttonPaint);
        } else {
            temp.set(0,buttonTop,(int) width, buttonBottom);
            canvas.drawBitmap(buttonBitmap, null, temp, buttonPaint);
        }





    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //명령주기 설정
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //움직이는 동안 발생하는 메소드 적용
                pushingDown = true;
                posY = event.getY(); //Y를 가져옴
                power = centerY - posY; //Y위치에 따른 power값 갱신
                handler.post(repeater);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //취소되면 원래위치로 이동
                pushingDown = false;
                posY = centerY;
                power = 0;
                break;
        }

        if(event.getX() < 0 || event.getX() > width) posY = centerY;

        invalidate();

        return true;
    }

    public void setPadColor(int padColor) {
        this.padColor = padColor;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
    }

    public void setSensitivites(int sensitivites) {
        this.sensitivites = sensitivites;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public void setListener(NewJoyStickListener listener) {
        this.listener = listener;
    }

    public float getPower() {
        return power;
    }

    public int getMoving(){
        if(posY < centerY + sensitiveDis && posY > centerY-sensitiveDis){
            return 0; //정지
        } else if(posY < centerY - sensitiveDis) {
            return 1; // 앞
        } else if(posY > centerY + sensitiveDis){
            return -1; // 뒤
        }
        return 0;
    }
}
