package iitp.project.haechi.purdueapps3.videorec;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by dnay2 on 2016-09-26.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraView(Context context,Camera mCamera) {
        super(context, null);
        this.mCamera = mCamera;
        this.mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraView(Context context, AttributeSet attrs, Camera mCamera) {
        super(context, attrs, 0);
        this.mCamera = mCamera;
        this.mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyleAttr, Camera mCamera) {
        super(context, attrs, defStyleAttr);
        this.mCamera = mCamera;
        this.mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder){
        try{
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }catch (IOException e){
            //error setting camera preview
        }
    }

    //카메라 인스턴스 파괴
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    //카메라 로테이션 변경되었을 때 작동
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder,
                               int format,
                               int w,
                               int h) {
        if(mHolder.getSurface() == null){
            //preview surface does not exist
            return;
        }

        try{
            mCamera.startPreview();
        } catch (Exception e){
            //ignore : tried to stop a non existent preview
        }

        //start preview with new settings
        try{
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        }catch (Exception e){
            //camera starting error
        }

    }
}
