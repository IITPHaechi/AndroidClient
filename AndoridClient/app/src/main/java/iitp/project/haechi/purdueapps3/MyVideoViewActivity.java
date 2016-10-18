package iitp.project.haechi.purdueapps3;

import android.media.MediaCodec;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by dnay2 on 2016-10-17.
 */
public class MyVideoViewActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    MediaCodec mMediaCodec;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void setVIdeo(){
        try{
            mMediaCodec = MediaCodec.createDecoderByType("video/avc");
        }catch (IOException e){
            Log.d("testapp", e.getMessage());
        }


    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
