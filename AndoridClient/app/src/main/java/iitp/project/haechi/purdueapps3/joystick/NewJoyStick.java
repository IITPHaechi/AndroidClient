package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-10-14.
 */
public class NewJoyStick extends View {

    //기본 셋팅
    Paint paint;            //draw 객체
    private float width;            //너비
    private float height;           //높이
    private int padColor;    // 배경색
    private int buttonColor;        //조이스틱 색

    //value
    float posY; //위치 pos
    float centerY; //기준  = height/2
    float sensitiveDis; // 민감도 설정
    int sensitivites = 5; //민감도 값이 높을 수록 민감도 하향
    float power; // 파워


    //작은 버튼 크기
    int percentage = 40;
    float buttonhHeight;

    //시간설정
    long time;

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
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        padColor = Color.BLACK;
        buttonColor = Color.RED;

        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NewJoyStick);
            if (typedArray != null) {
                padColor = typedArray.getColor(R.styleable.NewJoyStick_padColor, Color.BLACK);
                buttonColor = typedArray.getColor(R.styleable.NewJoyStick_buttonColor, Color.RED);
                sensitivites = typedArray.getInteger(R.styleable.NewJoyStick_sensitivity, 25);
                typedArray.recycle();
            }
        }
        sensitiveDis = 3 * sensitivites;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerY = height / 2;
        posY = centerY;
        buttonhHeight = height * percentage / 50f;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        android.graphics.Path path = new android.graphics.Path();
        Rect padRect = new Rect(0,0,(int)width, (int)height);
        paint.setColor(padColor);
        canvas.drawRect(padRect, paint);

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

        Rect buttonRect = new Rect(0, buttonTop, (int) width, buttonBottom);
//        Log.d("newjoy", "posY : " + (posY-30f));
        paint.setColor(buttonColor);
        canvas.drawRect(buttonRect, paint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //움직이는 동안 발생하는 메소드 적용
                posY = event.getY(); //Y를 가져옴
                power = centerY - posY; //Y위치에 따른 power값 갱신
                invalidate();

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //취소되면 원래위치로 이동
                posY = centerY;
                power = 0;
                invalidate();
                break;
        }

        //명령주기 설정
        if(System.currentTimeMillis() > time + 250){
            if(listener != null) listener.onMove(listener, power);
            time = System.currentTimeMillis();
        }


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
