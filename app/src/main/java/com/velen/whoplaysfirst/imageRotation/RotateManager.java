package com.velen.whoplaysfirst.imageRotation;


import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class RotateManager{

    private Context context;
    private ImageView imageView;
    private int lastAngle = 0;

    public RotateManager(Context context, ImageView imageView) {
        this.context = context;
        this.imageView = imageView;
    }

    public void rotateToPointAt(int x, int y, int duration) {

        int toAngle = getAngleForCoords2(x, y);

        RotateAnimation animation;

        if(toAngle > lastAngle) {
            animation = new RotateAnimation(toAngle, lastAngle, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            animation = new RotateAnimation(lastAngle, toAngle, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }

        animation.setDuration(duration - 10);
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);

        imageView.setAnimation(animation);
        imageView.startAnimation(animation);

        lastAngle = toAngle;
    }

    public void hideArrow() {
        imageView.clearAnimation();
        imageView.setVisibility(View.GONE);
    }

    public void showArrow() {
        imageView.setVisibility(View.VISIBLE);
    }

    private int getAngleForCoords(int fx, int fy) {
        double a;
        int ax = (int)imageView.getX() + imageView.getWidth()  / 2;
        int ay = (int)imageView.getY() + imageView.getHeight()  / 2;

        int dx = fx - ax;
        int dy = fy - ay;
        if (dx == 0) {
            a = 90;
            return (int)a;
        }
        if (dy == 0) {
            a = 0;
            return (int)a;
        }

        a = Math.toDegrees(Math.atan(dy / dx));
        if (dx > 0 && dy > 0) {
            // do nothing, a is correct
        } else if (dx > 0 && dy < 0) {
            a = 360 - a;
        } else if (dx < 0 && dy > 0) {
            a = 180 - a;
        } else {
            a = 180 + a;
        }
        return (int)a;
    }

    private int getAngleForCoords2(int fx, int fy) {
        double a;
        int ax = (int)imageView.getX() + imageView.getWidth()  / 2;
        int ay = (int)imageView.getY() + imageView.getHeight()  / 2;

        int dx = fx - ax;
        int dy = fy - ay;
        if (dx == 0) {
            a =  dy > 0 ? 90 : 270;
            return (int)a;
        }
        if (dy == 0) {
            a = dx > 0 ? 0 : 180;
            return (int)a;
        }

        a = Math.toDegrees(Math.atan((double) dy / (double) dx));
        //a = Math.toDegrees((double) dy / (double) dx);
        if (dx > 0 && dy > 0) {
            //bottom right
            a = - 380 + a;
        } else if (dx > 0 && dy < 0) {
            //top right
            a =  a;
        } else if (dx < 0 && dy > 0) {
            //bottom left
            a = -180 + a;
        } else if(dx < 0 && dy < 0){
            //top left
            a = -180 + a;
        }
        return (int)a;
    }

}
