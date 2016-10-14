package iitp.project.haechi.purdueapps3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import iitp.project.haechi.purdueapps3.joystick.JoyStick;
import iitp.project.haechi.purdueapps3.joystick.JoystickManager;
import iitp.project.haechi.purdueapps3.socket.SocketClientTask;

/**
 * Created by dnay2 on 2016-10-13.
 */
public class MyJostickActivity extends AppCompatActivity implements JoystickManager.JoyStickManagerListener {

    private static final String IP_ADDRESS = "172.24.1.1";
    private static final int PORT_ = 8888;

    private static final String MY_IP = "172.24.1.122";
    private static final int MY_PORT = 12345;

    //바퀴에 대한 명령어
    private static final int MOVE_FRONT = 1;
    private static final int MOVE_STOP = 0;
    private static final int MOVE_BACK = -1;
    private static final int MOVE_RIGHT = 4;
    private static final int MOVE_LEFT = 3;

    //통신을 관리
    SocketClientTask cTask;
    EditText console;

    //조이스틱을 관리
    JoyStick joyL;
    JoyStick joyR;
    JoystickManager manager;
    LinearLayout mManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        console = (EditText) findViewById(R.id.console);
        joyL = (JoyStick) findViewById(R.id.joyL);
        joyR = (JoyStick) findViewById(R.id.joyR);

//        joyL.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                getMoving();
//                return false;
//            }
//        });
//        joyR.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                getMoving();
//                return false;
//            }
//        });

//        mManager = (LinearLayout) findViewById(R.id.manager);
//        manager = (JoystickManager) findViewById(R.id.manager);
//        manager.setJoystick(joyL, joyR);
//        manager.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                getMoving();
//                return false;
//            }
//        });

        findViewById(R.id.conn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connBtn(view);
            }
        });

        findViewById(R.id.disconn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connBtn(view);
            }
        });
    }

    long time;
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (System.currentTimeMillis() > time + 500) {
                getMoving();
                time = System.currentTimeMillis();
            }
            return false;
        }
    };

    private void connection() {
        cTask = new SocketClientTask(IP_ADDRESS, PORT_);
        cTask.setConsole(console);
//        manager.setcTask(cTask);
        cTask.execute();
    }

    private void disconnection() {
        if (cTask != null) {
            cTask.disconnection();
        }
    }

    @Override
    public void onActive(JoystickManager joystickManager) {
        joystickManager.getMoving();
    }

    //소켓 연결 버튼
    public void connBtn(View v) {
        switch (v.getId()) {
            case R.id.conn:
                connection();
                break;
            case R.id.disconn:
                disconnection();
                break;
        }
    }

    long mTime = 0;

    //움직임 명령쿼리 가져오기
    public void getMoving() {
        if (System.currentTimeMillis() > mTime + 100) {
            String order = "";
            if (cTask != null) {
                switch (movingRobot()) {
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
                Log.d("joystick", "발현  :  " + order);
                mTime = System.currentTimeMillis();
            }
        }
    }

    //움직여라 노예
    public int movingRobot() {
        if (joyL.getAngle() > 1.0 && joyR.getAngle() > 1.0) return MOVE_FRONT;
        else if (joyL.getAngle() < -1.0 && joyR.getAngle() < -1.0) return MOVE_BACK;
        else if (joyL.getAngle() > 1.0) return MOVE_LEFT;
        else if (joyR.getAngle() > 1.0) return MOVE_RIGHT;
        return MOVE_STOP;
    }


}
