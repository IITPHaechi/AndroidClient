package iitp.project.haechi.purdueapps3.videoview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by dnay2 on 2016-10-18.
 */
public class MjpegActivity extends Activity {

    private MjpegView mv;
    private static final int MENU_QUIT = 1;

    private static final String VIDEO_URL = "http://172.24.1.1:8111/stream";

    /* Creates the menu items*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mv = new MjpegView(this);
        setContentView(mv);

        new ReadThread().run();
    }

    private class ReadThread extends Thread{
        @Override
        public void run() {
            mv.setSource(MjpegInputStream.read(VIDEO_URL));
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.stopPlayback();
    }
}
