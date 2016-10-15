package iitp.project.haechi.purdueapps3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //UI로직
    View.OnClickListener bHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()) {
                case R.id.btnSocket:
                    intent = new Intent(MainActivity.this, SocketActivity.class);
                    break;
                case R.id.btnMyjoystick:
                    intent = new Intent(MainActivity.this, MyJostickActivity.class);
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
        findViewById(R.id.btnSocket).setOnClickListener(bHandler);
        findViewById(R.id.btnMyjoystick).setOnClickListener(bHandler);
    }

}
