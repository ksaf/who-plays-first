package com.velen.whoplaysfirst.circles;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.velen.whoplaysfirst.SelectionEndListener;

public class CirclesView extends View implements View.OnTouchListener{

    public static final int MAX_FINGERS = 5;
    private final static int radius = 170;
    private SelectionEndListener selectionEndListener;
    private CirclesManager circlesManager = new CirclesManager();
    private CircleColorManager circleColorManager = new CircleColorManager(circlesManager);

    public CirclesView(Context context) {
        super(context);
        setFocusable(true);
        this.setOnTouchListener(this);
    }

    public void setSelectionEndListener(SelectionEndListener selectionEndListener) {
        this.selectionEndListener = selectionEndListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //drawing the circle
        for(CircleShape circleShape : circlesManager.getCircles()) {
            canvas.drawCircle(circleShape.x, circleShape.y, radius, circleShape.paint);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        //System.out.println("X,Y:"+"x"+","+y);
        //randColor();

        int pointerCount = event.getPointerCount();
        int cappedPointerCount = pointerCount > MAX_FINGERS ? MAX_FINGERS : pointerCount;

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);



        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // TODO use data
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                // TODO use data
                for(int i = 0; i < pointerCount; i++) {
                    int index = event.findPointerIndex(i);
                    if(index >= 0) {
                        int x = (int) event.getX(index) - (radius / 5);      //logic to plot the circle in exact touch place
                        int y = (int) event.getY(index) - (radius / 5);
                        circlesManager.addCircleIfNew(index, x, y, circleColorManager.getStrokeOnlyPaint());
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(pointerCount < 2) {
                    selectionEndListener.onSelectionEnd();
                    circlesManager.removeAllCircles();
                }
                circlesManager.removeCircle(pointerId);
                if(circlesManager.getCircles().size() < 1) {
                    if(circleColorManager.getRotateManager() != null) {
                        circleColorManager.getRotateManager().hideArrow();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL: {
                // TODO use data
                break;
            }
        }
        invalidate();
        return true;
    }

    public void removeAllCircles() {
        if(circlesManager.getCircles().size() > 0) {
            circlesManager.removeAllCircles();
        }
        invalidate();
    }

    public CirclesManager getCirclesManager() {
        return circlesManager;
    }

    public CircleColorManager getCircleColorManager() {
        return circleColorManager;
    }
}
