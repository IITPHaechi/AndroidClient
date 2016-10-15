package iitp.project.haechi.purdueapps3;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import iitp.project.haechi.purdueapps3.joystick.NewJoyStick;
import iitp.project.haechi.purdueapps3.socket.SocketClientTask;

/**
 * Created by dnay2 on 2016-10-13.
 */
public class MyJostickActivity extends AppCompatActivity implements NewJoyStick.NewJoyStickListener {

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

    private static final int MOVE_LEFT_BACK = 5;
    private static final int MOVE_RIGHT_BACK = 6;

    //통신을 관리
    SocketClientTask cTask;
    EditText console;

    //조이스틱을 관리
    NewJoyStick joyL;
    NewJoyStick joyR;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        console = (EditText) findViewById(R.id.console);
        joyL = (NewJoyStick) findViewById(R.id.joyL);
        joyL.setTag("LEFT");
        joyR = (NewJoyStick) findViewById(R.id.joyR);
        joyR.setTag("RIGHT");
        joyL.setListener(this);
        joyR.setListener(this);

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
    public void onMove(NewJoyStick.NewJoyStickListener listener, float power) {
            getMoving();
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

    //움직임 명령쿼리 가져오기
    public void getMoving() {
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
                        order = "left=0,right=1,time=1";
                        break;
                    case MOVE_LEFT:
                        order = "left=1,right=0,time=1";
                        break;
                    case MOVE_STOP:
                        order = "left=0,right=0,time=0";
                        break;
                    case MOVE_RIGHT_BACK:
                        order = "left=0,right=-1,time=1";
                        break;
                    case MOVE_LEFT_BACK:
                        order = "left=-1,right=0,time=1";
                        break;
                }
                cTask.actionSend(order);
                Log.d("joystick", "발현  :  " + order);
        }
    }

    //움직여라 노예
    public int movingRobot() {
        if(joyL.getMoving() == MOVE_FRONT && joyR.getMoving() == MOVE_FRONT){//둘다 앞인 경우
            return MOVE_FRONT;
        } else if(joyL.getMoving() == MOVE_BACK && joyR.getMoving() == MOVE_BACK){//둘다 뒤인 경우
            return MOVE_BACK;
        } else if(joyL.getMoving() == MOVE_FRONT){//왼쪽만 앞인 경우
            return MOVE_LEFT;
        } else if(joyR.getMoving() == MOVE_FRONT){//오른쪽만 앞인 경우
            return MOVE_RIGHT;
        } else if(joyL.getMoving() == MOVE_BACK){//왼쪽만 뒤인 경우
            return MOVE_LEFT_BACK;
        } else if(joyR.getMoving() == MOVE_BACK){//오른쪽만 뒤인 경우
            return MOVE_RIGHT_BACK;
        } else//모두 스탑인 경우
            return MOVE_STOP;
    }


}
