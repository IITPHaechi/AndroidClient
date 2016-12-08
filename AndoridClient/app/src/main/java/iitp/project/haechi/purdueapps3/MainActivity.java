package iitp.project.haechi.purdueapps3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onMainButton(View v){
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btnMyjoystick:
                intent = new Intent(MainActivity.this, MyJostickActivity.class);
                break;
        }
        startActivity(intent);
    }

}
