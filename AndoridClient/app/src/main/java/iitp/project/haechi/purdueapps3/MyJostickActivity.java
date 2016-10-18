package iitp.project.haechi.purdueapps3;

import android.media.MediaCodec;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.VideoView;

import iitp.project.haechi.purdueapps3.joystick.NewJoyStick;
import iitp.project.haechi.purdueapps3.socket.SocketClientTask;

/**
 * Created by dnay2 on 2016-10-13.
 */
public class MyJostickActivity extends AppCompatActivity implements NewJoyStick.NewJoyStickListener {

    private static final String IP_ADDRESS = "172.24.1.1";
    private static final int PORT_ = 8888;

    private static final String VIDEO_URL = "http://172.24.1.1:8082/index.html";

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

    //영상을 가져온다
//    VideoView videoView;

    //웹뷰 클라이언트
    WebView webView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

//        videoView = (VideoView) findViewById(R.id.videoView);
        webView = (WebView) findViewById(R.id.webView);

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


        //동영상 가져오기
//        setVideoView();
        setWebView();
    }

    private void setWebView(){
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(VIDEO_URL);
    }

    private class MyWebChromeClient extends WebChromeClient implements MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener{
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
            if(view instanceof FrameLayout){
                FrameLayout frame = (FrameLayout) view;
                VideoView video = (VideoView) frame.getFocusedChild();
                frame.removeView(video);
//                a.setContentView(video);
//                video.setOnCompletionListener(this);
//                video.setOnErrorListener(this);
//                video.start();
            }
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
//            Log.d(TAG, "Video completo");
//            a.setContentView(R.layout.main);
//            WebView wb = (WebView) a.findViewById(R.id.webview);
//            a.initWebView();
        }

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            return false;
        }
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void setVideoView(){
        Uri video = Uri.parse(VIDEO_URL);

        Surface surface;
        MediaCodec codec = null;
//
//        try{
//            codec = MediaCodec.createByCodecName("video/avc");
//            codec.configure(format, ...);
//            codec.start();
//
//            ByteBuffer[] inputBuffers = codec.getInputBuffers();
//            ByteBuffer[] outputBuffers = codec.getOutputBuffers();
//
//            for(;;){
//                int inputBufferId = codec.dequeueInputBuffer(10000);
//                if(inputBufferId >= 0){
//                    codec.queueInputBuffer(inputBufferId,);
//                }
//            }
//        }catch (IOException e){
//
//        }





//        MediaFormat mOutputFormat = MediaFormat.createVideoFormat("video/avc", 320, 240);
//        mOutputFormat.setInteger(MediaFormat.KEY_BIT_RATE, 125000);
//        mOutputFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
//        mOutputFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar);
//        mOutputFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);


//        MediaController mc = new MediaController(this);
//        mc.setAnchorView(videoView);
//        mc.setMediaPlayer(videoView);
//
//        videoView.setMediaController(mc);
//        videoView.setVideoURI(video);
//        videoView.requestFocus();
//        videoView.start();
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
