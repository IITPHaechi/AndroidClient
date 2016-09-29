package iitp.project.haechi.purdueapps3;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by dnay2 on 2016-09-28.
 */
public class VideoViewActivity extends AppCompatActivity {


    VideoView videoView;
    private String url = "https://www.youtube.com/watch?v=K1Hu_oRjITU";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videoview);

        videoView = (VideoView) findViewById(R.id.videoView);
        findViewById(R.id.btnPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo();
            }
        });

    }

    private void playVideo(){
        Uri uri = Uri.parse(url);

        videoView.setVideoURI(uri);
        videoView.start();
    }
}
