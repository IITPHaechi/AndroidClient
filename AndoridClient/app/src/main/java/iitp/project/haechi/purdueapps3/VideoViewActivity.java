package iitp.project.haechi.purdueapps3;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;

import iitp.project.haechi.purdueapps3.videoview.MyVideoView;

/**
 * Created by dnay2 on 2016-09-28.
 */
public class VideoViewActivity extends AppCompatActivity {


    MyVideoView videoView;
    private static final String webUrl = "http://172.24.1.1/video";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);

        videoView = (MyVideoView) findViewById(R.id.videoView);
        findViewById(R.id.btnPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

    }

    private void playVideo(){
        Uri uri = Uri.parse(webUrl);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);

        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        videoView.requestFocus();
        videoView.start();
    }
}
