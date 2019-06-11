package com.velen.whoplaysfirst.circles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.Handler;

import com.velen.whoplaysfirst.R;
import com.velen.whoplaysfirst.imageRotation.RotateManager;
import com.velen.whoplaysfirst.SelectionEndListener;
import com.velen.whoplaysfirst.vibration.VibrationUtil;

import java.util.Random;


public class CircleColorManager {

    private Paint strokePaint;
    private Paint fillPaint;
    private CirclesManager circlesManager;
    private static final int STARTING_INTERVAL = 150;
    private static int interval = STARTING_INTERVAL;
    private Handler handler = new Handler();
    private int finalRounds = -1;
    private SelectionEndListener selectionEndListener;
    private RotateManager rotateManager;
    private MediaPlayer mediaPlayer;
    private Context context;

    public CircleColorManager(CirclesManager circlesManager) {
        this.circlesManager = circlesManager;
        setupFillPaint(Color.parseColor("#D11F0C"), Color.parseColor("#9A1608"));
        setupStrokePaint(Color.BLACK);
    }

    public void setSelectionEndListener(SelectionEndListener selectionEndListener) {
        this.selectionEndListener = selectionEndListener;
    }

    public void setRotateManager(RotateManager rotateManager) {
        this.rotateManager = rotateManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public RotateManager getRotateManager() {
        return rotateManager;
    }

    private Runnable paintChanger = new Runnable() {
        @Override
        public void run() {
            changeCurrentPaints();
//            if(interval > 150) {
//                interval = interval - (int)(interval * 0.1);
//            }
            interval = interval + (int)(interval * 0.05);
            handler.postDelayed(paintChanger, interval);
        }
    };

    private Runnable finalPaintChanger = new Runnable() {
        @Override
        public void run() {
            changeCurrentPaints();
            interval = interval + (int)(interval * 0.05);
            if(finalRounds >= 0) {
                finalRounds--;
                handler.postDelayed(finalPaintChanger, interval);
            } else {
//                mediaPlayer.release();
                interval = STARTING_INTERVAL;
                VibrationUtil.vibrate(context, 800);
                //selectionEndListener.onSelectionEnd();
            }
        }
    };

    private void setupStrokePaint(int color) {
        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(color);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(2);
        strokePaint.setStrokeCap(Paint.Cap.BUTT);
    }

    private void setupFillPaint(int color1, int color2) {
        fillPaint = new Paint();
        fillPaint.setShader(new LinearGradient(0, 0, 0, 340, color1, color2, Shader.TileMode.MIRROR));
        fillPaint.setAntiAlias(true);
        //fillPaint.setColor(color);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setStrokeWidth(2);
        fillPaint.setStrokeCap(Paint.Cap.BUTT);
    }

    public void changeCurrentPaints() {
        for(int i = 0; i < circlesManager.getCircles().size(); i++) {
            CircleShape circleShape = circlesManager.getCircles().get(i);
            if(circleShape.paint == fillPaint) {
                /** Vibration*/
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        VibrationUtil.vibrate(context, interval / 2);
                    }
                }, interval / 2);

                circleShape.paint = strokePaint;
                if(finalRounds > -1) {
                    if(rotateManager != null) {
                        rotateManager.rotateToPointAt(circleShape.x, circleShape.y, interval);
                    }
                }
                if(i == circlesManager.getCircles().size() - 1) {
                    if(circlesManager.getCircles().size() > 0) {
                        circlesManager.getCircles().get(0).paint = fillPaint;
                        mediaPlayer.start();
                    }
                    return;
                } else {
                    circlesManager.getCircles().get(i+1).paint = fillPaint;
                    mediaPlayer.start();
                    return;
                }
            }
        }
    }

    public Paint getStrokeOnlyPaint() {
        return strokePaint;
    }

    public Paint getFullColorPaint() {
        return fillPaint;
    }

    public void startChangingColors() {
        mediaPlayer = MediaPlayer.create(context, R.raw.pop);
        if(rotateManager != null) {
            rotateManager.showArrow();
        }
        if(circlesManager.getCircles().size() > 0) {
            circlesManager.getCircles().get(0).paint = fillPaint;
            handler.post(paintChanger);
        }
    }

    public void stopChangingColors() {
        handler.removeCallbacksAndMessages(null);
        finalRounds = getWinnerIndex();
        handler.postDelayed(finalPaintChanger, interval);
    }

    private int getWinnerIndex() {
        int min = 0;
        int max = circlesManager.getCircles().size() - 1;

        Random r = new Random();
        if(max > 0)
            return r.nextInt(max - min + 1) + min;
        else
            return 0;
    }

}
