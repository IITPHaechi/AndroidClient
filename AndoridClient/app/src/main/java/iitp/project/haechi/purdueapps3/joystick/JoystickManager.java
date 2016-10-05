package iitp.project.haechi.purdueapps3.joystick;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import iitp.project.haechi.purdueapps3.ApplicationController;
import iitp.project.haechi.purdueapps3.NetworkService;
import iitp.project.haechi.purdueapps3.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dnay2 on 2016-10-03.
 */
public class JoystickManager extends ViewGroup {

    private static JoystickManager instance;

    public static JoystickManager getInstance() {
        return instance;
    }

    //지속시간 및 빈도
    private int commandPerSec = 0;
    private int movingDuration = 0;
    private long time;

    //바퀴에 대한 명령어
    private static final int FRONT = 1;
    private static final int STOP = 0;
    private static final int BACK = -1;

    //어느쪽 바퀴인지 식별
    private static final int LEFT = R.id.joy1;
    private static final int RIGHT = R.id.joy2;

    JoyStick L_Joystick, R_Joystick;
    private NetworkService networkService;
    ApplicationController app;

    JoyStickManagerListener jmListener;

    public interface JoyStickManagerListener {
        void onActive(JoystickManager joystickManager, double angle, double power);
    }

    public JoystickManager() {
        super(null, null, 0);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    public void setJoystick(JoyStick l_Joystick, JoyStick r_Joystick) {
        L_Joystick = l_Joystick;
        R_Joystick = r_Joystick;
        L_Joystick.setTag(LEFT);
        R_Joystick.setTag(RIGHT);
    }

    public void setJoystickManagerListener(JoyStickManagerListener jmListener) {
        this.jmListener = jmListener;
    }

    //조이스틱을 만지고 있는 동안 루프
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //조이스틱 ACTIVE

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //조이스틱 INACTIVE
                break;
        }
        if (jmListener != null){
            jmListener.onActive(this, 100f, 100f);
        }

        return true;
    }



    //명령 쿼리 날리기
    public void move(
            int resourceId,
            int duration,
            int stopORmove
    ) {
        int isLeft = resourceId == LEFT ? 1 : 0;
        Call<dummy> moveRobot = networkService.moveRobot(isLeft, duration, stopORmove);
        moveRobot.enqueue(new Callback<dummy>() {
            @Override
            public void onResponse(Call<dummy> call, Response<dummy> response) {
                if (response.isSuccessful()) Log.d("test", "robot is moving");
            }

            @Override
            public void onFailure(Call<dummy> call, Throwable t) {

            }
        });
    }

    public void doAction(
            int resourceId,
            int duration,
            int stopORmove
    ){
        int isLeft = resourceId == LEFT ? 1 : 0;
        //소켓 명령 날리기
    }

    //매니져를 쓰면 발생하는 메소드
    public void action(
            JoystickManager manager,
            double angle,
            double power
    ){

    }


    //움직임 명령 가져오기
    public int getMoving(){
        if(L_Joystick.getTouchDown() && R_Joystick.getTouchDown()){
            //둘다 만지고 있는 경우
            if(L_Joystick.angle > 1.0 && R_Joystick.angle > 1.0) return FRONT; //앞으로
            if(L_Joystick.angle < -1.0 && R_Joystick.angle < -1.0) return BACK; //뒤로
        } else if(L_Joystick.getTouchDown()){
            //왼쪽만 만지고 있는 경우
            if(L_Joystick.angle > 1.0) return FRONT;
            if(L_Joystick.angle < -1.0) return BACK;
        } else if(R_Joystick.getTouchDown()){
            //오른쪽만 만지고 있는 경우
            if(R_Joystick.angle > 1.0) return FRONT;
            if(R_Joystick.angle < -1.0) return BACK;
        } else return STOP;
        return 0;
    }

    //시스템 값 재설정
    public void setCommandPerSec(int commandPerSec) {
        this.commandPerSec = commandPerSec;
    }

    public void setMovingDuration(int movingDuration) {
        this.movingDuration = movingDuration;
    }

}
