package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import iitp.project.haechi.purdueapps3.ApplicationController;
import iitp.project.haechi.purdueapps3.NetworkService;
import iitp.project.haechi.purdueapps3.R;
import iitp.project.haechi.purdueapps3.socket.SocketClientTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dnay2 on 2016-10-03.
 */
public class JoystickManager extends LinearLayout {

    private static JoystickManager instance;

    public static JoystickManager getInstance() {
        return instance;
    }

    //지속시간 및 빈도
    private int commandPerSec = 0;
    private int movingDuration = 0;
    private long time;

    //바퀴에 대한 명령어
    private static final int MOVE_FRONT = 1;
    private static final int MOVE_STOP = 0;
    private static final int MOVE_BACK = -1;
    private static final int MOVE_RIGHT = 4;
    private static final int MOVE_LEFT = 3;

    //어느쪽 바퀴인지 식별
    private static final int LEFT = R.id.joyL;
    private static final int RIGHT = R.id.joyR;

    JoyStick L_Joystick, R_Joystick;
    private NetworkService networkService;
    ApplicationController app;

    SocketClientTask cTask;

    JoyStickManagerListener jmListener;

    public interface JoyStickManagerListener {
        void onActive(JoystickManager joystickManager);
    }

    public JoystickManager(Context context) {
        super(context, null);
    }

    public JoystickManager(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public JoystickManager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

    //움직임 명령쿼리 가져오기
    public void getMoving() {
        Log.d("joystick", "발현");
        String order = "";
        if(cTask != null){
            switch (movingRobot()){
                case MOVE_FRONT:
                    order = "left=1,right=1,time=1";
                    break;
                case MOVE_BACK:
                    order = "left=-1,right=-1,time=1";
                    break;
                case MOVE_RIGHT:
                    order = "left=1,right=0,time=1";
                    break;
                case MOVE_LEFT:
                    order = "left=0,right=1,time=1";
                    break;
                case MOVE_STOP:
                    order = "left=0,right=0,time=0";
                    break;
            }
            cTask.actionSend(order);
        }
    }

    //움직여라 노예
    public int movingRobot() {
        if (L_Joystick.angle > 10.0 && R_Joystick.angle > 10.0) return MOVE_FRONT;
        else if (L_Joystick.angle < -10.0 && R_Joystick.angle < -10.0) return MOVE_BACK;
        else if (L_Joystick.angle > 10.0) return MOVE_LEFT;
        else if (R_Joystick.angle > 10.0) return MOVE_RIGHT;
        return MOVE_STOP;
    }

    //시스템 값 재설정
    public void setCommandPerSec(int commandPerSec) {
        this.commandPerSec = commandPerSec;
    }

    public void setMovingDuration(int movingDuration) {
        this.movingDuration = movingDuration;
    }


    //소켓 연결 가져오기
    public void setcTask(SocketClientTask cTask) {
        this.cTask = cTask;
    }
}
