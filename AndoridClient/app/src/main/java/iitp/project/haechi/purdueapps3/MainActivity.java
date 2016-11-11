package iitp.project.haechi.purdueapps3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import iitp.project.haechi.purdueapps3.videoview.MjpegActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMainButton(View v){
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnSocket:
                intent = new Intent(MainActivity.this, SocketActivity.class);
                break;
            case R.id.btnMyjoystick:
                intent = new Intent(MainActivity.this, MyJostickActivity.class);
                break;
            case R.id.btnVideoView:
                intent = new Intent(MainActivity.this, VideoViewActivity.class);
                break;
            case R.id.btnJcodec:
                intent = new Intent(MainActivity.this, MyJcodecVideoView.class);
                break;
            case R.id.btnMjpeg:
                intent = new Intent(MainActivity.this, MjpegActivity.class);
                break;
        }
        startActivity(intent);
    }

}
