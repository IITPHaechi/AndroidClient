package iitp.project.haechi.purdueapps3.joystick;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

/**
 * Created by cholmink on 16. 9. 20..
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    float width;
    float height;
    float centerX;
    float centerY;
    float min;
    float posX;
    float posY;
    float radius;
    GameLoop gameLoop;
    Paint paint;
    Random random = new Random();
    int i;
    int size = 20;
    int minSpeed;
    int maxSpeed;
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

    //Need to import camera inputs

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        rectF = new RectF();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (canvas == null) return;
        super.draw(canvas);
        canvas.drawColor(Color.BLACK);

        //The camera stuff goes in here

        posX -= Math.cos(angle) * (power/2);
        posY += Math.sin(-angle) * (power/2);
        if (posX > width - radius) posX = width - radius;
        if (posX < radius) posX = radius;
        if (posY > height - radius) posY = height - radius;
        if (posY < radius) posY = radius;


        canvas.rotate(rotate, posX, posY);
        rectF.set(posX - radius, posY - radius, posX + radius, posY + radius);
        canvas.drawBitmap(droid, null, rectF, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameLoop = new GameLoop(this);
        gameLoop.setRunning(true);
        gameLoop.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        min = Math.min(width, height);

        centerX = width/2;
        centerY = height/2;
        posX = centerX;
        posY = centerY;
        radius = min/12;
        rectF = new RectF(posX - radius, posY - radius, posX + radius, posY + radius);

        minSpeed = (int) (min/75);
        maxSpeed = (int) (min/25);

        minRadius = (int) (min/250);
        maxRadius = (int) (min/220);

        if (maxRadius == minRadius) maxRadius += minRadius;

        //Used to be a star here.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameLoop.setRunning(false);
        gameLoop = null;
    }

    public void move(double angle, double power) {
        this.angle = angle;
        this.power = power;
    }

    public void rotate(double angle2) {
        this.angle2 = angle2;
    }
}