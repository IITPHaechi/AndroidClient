package iitp.project.haechi.purdueapps3.videorec;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import iitp.project.haechi.purdueapps3.R;

/**
 * Created by dnay2 on 2016-09-26.
 */
public class VideoActivity extends AppCompatActivity{


    private Camera mCamera;
    private MediaRecorder mMediaRecorder;
    private CameraView mCameraView;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        checkCameraHardware(this);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera = getCameraInstance();

                mCameraView = new CameraView(VideoActivity.this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.cameraview);
                preview.addView(mCameraView);
            }
        });

        findViewById(R.id.capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, mPicture);
            }
        });
    }


    //카메라가 있는지 여부검사
    private boolean checkCameraHardware(Context context){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            Toast.makeText(VideoActivity.this, "카메라가 없습니다", Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
    }

    //카메라 인스턴스 호출
    public static Camera getCameraInstance(){
        Camera c = null;
        try{
            c = Camera.open();
        }catch (Exception e){
            //camera callback error
        }
        return c;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            String  name = "name";
            try{
                FileOutputStream out = new FileOutputStream(name);
                out.flush();
                out.write(bytes);
                out.close();
            }catch (IOException e){

            }

            final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            AlertDialog dialog = new AlertDialog.Builder(VideoActivity.this)
                    .setTitle("image")
                    .setView(R.layout.viewer)
                    .create();

            if(bitmap != null){
                ImageView iv = (ImageView) dialog.findViewById(R.id.viewer);
//                Picasso.with(VideoActivity.this).load()
            }
            dialog.show();




        }
    };
    private boolean prepareVideoRecorder(){
           mCamera = getCameraInstance();
        mMediaRecorder = new MediaRecorder();

        //Unlock and set camera to MediaRecorder
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        //set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        //set a camcorderProfile
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        //set output format and encoding
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        //set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        //set the preview output
        mMediaRecorder.setPreviewDisplay(mCameraView.getHolder().getSurface());

        //prepare configured MediaRecorder
        try{
            mMediaRecorder.prepare();
        }catch (IllegalStateException e){
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }catch (IOException e){
            e.printStackTrace();
            releaseMediaRecorder();
            return false;
        }

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();
        releaseCamera();
    }

    private void setRecording(){
        if(isRecording){
            mMediaRecorder.stop();
            releaseMediaRecorder();
            mCamera.lock();

            isRecording = false;
        } else {
            if(prepareVideoRecorder()){
                mMediaRecorder.start();
                isRecording = true;
            } else {
                releaseMediaRecorder();
            }
        }
    }

    private void releaseMediaRecorder(){
        if(mMediaRecorder != null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private void releaseCamera(){
        if(mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "VID_"+ timeStamp + ".mp4");
        } else {
            return  null;
        }

        return mediaFile;
    }
}
