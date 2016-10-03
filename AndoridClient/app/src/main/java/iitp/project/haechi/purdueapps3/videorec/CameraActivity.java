package iitp.project.haechi.purdueapps3.videorec;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;

import iitp.project.haechi.purdueapps3.R;

public class CameraActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraView mCameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        checkCameraHardware(this);


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera = getCameraInstance();

                mCameraView = new CameraView(CameraActivity.this, mCamera);
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
            Toast.makeText(CameraActivity.this, "카메라가 없습니다", Toast.LENGTH_SHORT).show();
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

    private PictureCallback mPicture = new PictureCallback() {
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
            AlertDialog dialog = new AlertDialog.Builder(CameraActivity.this)
                    .setTitle("image")
                    .setView(R.layout.viewer)
                    .create();

            if(bitmap != null){
                ImageView iv = (ImageView) dialog.findViewById(R.id.viewer);
//                Picasso.with(CameraActivity.this).load()
            }
            dialog.show();




        }
    };
}
