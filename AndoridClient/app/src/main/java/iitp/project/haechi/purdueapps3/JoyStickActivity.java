package iitp.project.haechi.purdueapps3;

import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import iitp.project.haechi.purdueapps3.joystick.CameraView;
import iitp.project.haechi.purdueapps3.joystick.JoyStick;
import iitp.project.haechi.purdueapps3.joystick.JoystickManager;

/**
 * Created by dnay2 on 2016-10-02.
 */
public class JoyStickActivity extends AppCompatActivity implements JoystickManager.JoyStickManagerListener {

    private Camera mCamera = null;
    private CameraView mCameraView = null;
    ListView durationList, commandpersecList;
    EditText logText;


    private static int commandpersec = 500; // how many times command per sec. default : 500ms
    private static int isLeftWheel = -1;    // which of wheel move? default : -1 (all of wheels not working)
    private static int durationTime = 1000; // how long time the machine moves. default : 1000ms
    private static int isStopOrder = 0;   // identificate the order for stop or move.

    private static final int LEFT_WHEEL = 1;
    private static final int RIGHT_WHEEL = 0;

    private static final int STOP_ORDER = 1;
    private static final int MOVE_ORDER = 0;

    private static ArrayList<String> durationArr = new ArrayList<>();
    private static ArrayList<String> commandArr = new ArrayList<>();
    TestAdapter durationAdater, commandAdater;

    JoystickManager mJoystickManager = new JoystickManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);
        try {
            mCamera = Camera.open();//you can use open(int) to use different cameras
        } catch (Exception e) {
            Log.d("ERROR", "Failed to get camera: " + e.getMessage());
        }
        mCameraView = new CameraView(this, mCamera);
//        FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
//        camera_view.addView(mCameraView);

        JoyStick joy1 = (JoyStick) findViewById(R.id.joy1);
        joy1.setPadColor(Color.parseColor("#55ffffff"));
        joy1.setButtonColor(Color.parseColor("#55ff0000"));

        JoyStick joy2 = (JoyStick) findViewById(R.id.joy2);
        joy2.setPadColor(Color.parseColor("#55ffffff"));
        joy2.setButtonColor(Color.parseColor("#55ff0000"));

        mJoystickManager.setJoystick(joy1, joy2);
        mJoystickManager.setJoystickManagerListener(this);

        setConsole();

    }

    private void setConsole() {
        for (int i = 100; i <= 2000; i += 100) durationArr.add("" + i);
        for (int i = 1; i <= 20; i++) commandArr.add(i + "00ms 마다");
        commandAdater = new TestAdapter(JoyStickActivity.this, commandArr);
        durationAdater = new TestAdapter(JoyStickActivity.this, durationArr);

        commandpersecList = (ListView) findViewById(R.id.commandperseclist);
        durationList = (ListView) findViewById(R.id.durationlist);
        logText = (EditText) findViewById(R.id.logText);

        commandpersecList.setAdapter(commandAdater);
        durationList.setAdapter(durationAdater);

        commandpersecList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mJoystickManager.setCommandPerSec((i + 1) * 100);
            }
        });

        durationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mJoystickManager.setMovingDuration(Integer.parseInt(durationArr.get(i)));
            }
        });
    }

    @Override
    public void onActive(JoystickManager joystickManager, double angle, double power) {

    }
}
