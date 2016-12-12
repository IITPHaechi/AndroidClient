package iitp.project.haechi.purdueapps3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by dnay2 on 2016-12-08.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new SplashTask().execute();
    }

    private class SplashTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            SystemClock.sleep(2000);
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            return null;
        }
    }
}
