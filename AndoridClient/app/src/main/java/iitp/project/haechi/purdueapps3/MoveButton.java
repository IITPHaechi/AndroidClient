
package iitp.project.haechi.purdueapps3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import iitp.project.haechi.purdueapps3.retrofit.ConnectorClass;
/**
 * Created by dnay2 on 2016-09-25.
*/

public class MoveButton extends AppCompatActivity {

    Button up, down, left, right;
    long pressedTime;
    ConnectorClass conn = new ConnectorClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.move_button);
        findViewById(R.id.up).setTag("up");
        findViewById(R.id.down).setTag("down");
        findViewById(R.id.right).setTag("right");
        findViewById(R.id.left).setTag("left");

        findViewById(R.id.up).setOnTouchListener(touchListener);
        findViewById(R.id.down).setOnTouchListener(touchListener);
        findViewById(R.id.right).setOnTouchListener(touchListener);
        findViewById(R.id.left).setOnTouchListener(touchListener);


    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            /**
             *   터치를 하는동안 반복으로 호출되는 메소드 OnTouchListener를 이용한다.
             *       터치시각       누른 후 n초후                  이후시간
             *       pressedTime       (pressedTime + n*1,000)
             *          |----------------|------------------------------|  시간의 흐름도
             *         1. n 초가 지나기 전에는 호출 없이 return
             *              (pressedTiem + n*1,000 >  System.currentTimeMillis())
             *         2. n 초 이후에는  메소드 내용 호출
             *              (pressedTiem + n*1,000 <  System.currentTimeMillis())
             */
            if(pressedTime != 0 && pressedTime + 500 > System.currentTimeMillis()){
                return true;
            }
            switch ((String) view.getTag()){
                case "up":
                    break;
                case "down":
                    break;
                case "right":
                    break;
                case "left":
                    break;
            }
            pressedTime = System.currentTimeMillis();
            Log.d("test", view.getTag() + " is clicked");
            return false;
        }
    };
}

