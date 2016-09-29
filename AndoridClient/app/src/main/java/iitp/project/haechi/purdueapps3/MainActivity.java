package iitp.project.haechi.purdueapps3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import iitp.project.haechi.purdueapps3.cameraproject.CameraMasterMain;

public class MainActivity extends AppCompatActivity {

    //UI로직
    View.OnClickListener bHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.btnLight:
                    intent = new Intent(MainActivity.this, LightButton.class);
                    break;
                case R.id.btnMove:
                    intent = new Intent(MainActivity.this, MoveButton.class);
                    break;
                case R.id.btnCamera:
                    intent = new Intent(MainActivity.this, CameraActivity.class);
                    break;
                case R.id.btnVideo:
                    intent = new Intent(MainActivity.this, VideoViewActivity.class);
                    break;
                case R.id.btnCameraServer:
                    intent = new Intent(MainActivity.this, CameraMasterMain.class);
                    break;

            }
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UI로직 사용
        findViewById(R.id.btnLight).setOnClickListener(bHandler);
        findViewById(R.id.btnMove).setOnClickListener(bHandler);
        findViewById(R.id.btnCamera).setOnClickListener(bHandler);
        findViewById(R.id.btnVideo).setOnClickListener(bHandler);
        findViewById(R.id.btnCameraServer).setOnClickListener(bHandler);

    }

}
