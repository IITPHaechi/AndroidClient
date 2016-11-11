package iitp.project.haechi.purdueapps3;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by dnay2 on 2016-09-28.
 */
public class VideoViewActivity extends AppCompatActivity {


    VideoView videoView;
    WebView webView;
    private static final String webUrl = "http://172.24.1.1:8111/stream";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);

//        videoView = (MyVideoView) findViewById(R.id.videoView);
        webView = (WebView) findViewById(R.id.webView);
        findViewById(R.id.btnPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });
        setWebView();
    }



    private void playVideo(){
        Uri uri = Uri.parse(webUrl);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                Log.d("testapp", "video view start!");
                videoView.start();
            }
        });

//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
    }

    private void setWebView(){
//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webUrl);
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        if(webView.isActivated())
        webView.destroy();
    }
}
