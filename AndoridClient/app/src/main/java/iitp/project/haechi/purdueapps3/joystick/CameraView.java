package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import iitp.project.haechi.purdueapps3.ApplicationController;
import iitp.project.haechi.purdueapps3.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cholmink on 16. 9. 21..
 */

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {

    float width;
    float height;
    float centerX;
    float centerY;
    float min;
    float posX;
    float posY;
    float radius;
    int i;
    int size = 20;
    int minRadius;
    int maxRadius;
    int maxX;
    int maxY;
    int tmpRadius;
    Bitmap droid;
    RectF rectF;
    float rotate;

    double angle;
    double power;

    double angle2;

    GameLoop gameLoop;

    long movedTime = 0;
    long untilTime = 0;

    private NetworkService networkService;
    ApplicationController app;

    public CameraView(Context context, Camera camera) {
        super(context);
        networkService = ApplicationController.getInstance().getNetworkService();
        app = (ApplicationController) getContext().getApplicationContext();

        mCamera = camera;
//        mCamera.setDisplayOrientation(180);
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            //when the surface is created, we can set the camera to draw images in this surfaceholder
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();

            gameLoop = new GameLoop(this);
            gameLoop.setRunning(true);
            gameLoop.start();

        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceCreated " + e.getMessage());
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

        this.width = width;
        this.height = height;
        min = Math.min(width, height);

        centerX = width / 2;
        centerY = height / 2;
        posX = centerX;
        posY = centerY;
        radius = min / 12;
        rectF = new RectF(posX - radius, posY - radius, posX + radius, posY + radius);


        minRadius = (int) (min / 250);
        maxRadius = (int) (min / 220);


        if (maxRadius == minRadius) maxRadius += minRadius;

        if (mHolder.getSurface() == null)//check if the surface is ready to receive camera data
            return;

        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            //this will happen when you are trying the camera if it's not running
        }

        //now, recreate the camera preview
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("ERROR", "Camera error on surfaceChanged " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.release();

        gameLoop.setRunning(false);
        gameLoop = null;

    }

    private SurfaceHolder mHolder;
    private Camera mCamera;


    public void move(double angle, double power, int isLeft, int duration, int stopORmove, int hz) {
        Log.d("test", "movedTime:" + movedTime);
        Log.d("test", "untilTime:" + untilTime);
        this.angle = angle;
        this.power = power;

        if (System.currentTimeMillis() > untilTime) {

            if (angle >= 0.1) {
                movingRobot(isLeft, duration, stopORmove);
                untilTime = System.currentTimeMillis() + hz;
            }
            else if (angle <= -0.1) {
                movingRobot(isLeft, duration, stopORmove);
                untilTime = System.currentTimeMillis() + hz;
            }

            Log.d("test", "moved");
        }



    }

    public void rotate(double angle2) {
        initialize();
        this.angle2 = angle2;


    }

    public void initialize() {
        if (angle2 == 0) rotate = 0;
        else rotate = (float) Math.toDegrees(angle2) - 90;
    }

    private void turningon() {
        Call<dummy> turnon = networkService.turnonLight();
        turnon.enqueue(new Callback<dummy>() {
            double realangle = angle;

            @Override
            public void onResponse(Call<dummy> call, Response<dummy> response) {
                if (response.isSuccessful()) {
                    Log.d("test", "angle value : " + realangle);
                }
            }

            @Override
            public void onFailure(Call<dummy> call, Throwable t) {

            }
        });
    }

    private void turningoff() {
        Call<dummy> turnon = networkService.turnoffLight();
        turnon.enqueue(new Callback<dummy>() {
            double realangle = angle;

            @Override
            public void onResponse(Call<dummy> call, Response<dummy> response) {
                if (response.isSuccessful()) {
                    Log.d("test", "angle value : " + realangle);
                }
            }

            @Override
            public void onFailure(Call<dummy> call, Throwable t) {

            }
        });
    }

    private void movingRobot(int isLeft, int duration, int stopORmove){

        Call<dummy> moveRobot = networkService.moveRobot(isLeft,duration,stopORmove);
        moveRobot.enqueue(new Callback<dummy>() {
            @Override
            public void onResponse(Call<dummy> call, Response<dummy> response) {
                if(response.isSuccessful()) Log.d("test", "robot is moving");
            }

            @Override
            public void onFailure(Call<dummy> call, Throwable t) {

            }
        });
    }


}