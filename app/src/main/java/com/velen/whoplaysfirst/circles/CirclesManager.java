package com.velen.whoplaysfirst.circles;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class CirclesManager {

    private List<CircleShape> circles = new ArrayList<>();

    public void addCircleIfNew(int id, int x, int y, Paint paint) {
        if(!idExists(id)) {
            circles.add(new CircleShape(id, x, y, paint));
        } else {
            getCircleById(id).x = x;
            getCircleById(id).y = y;
        }
    }

    public void removeCircle(int id) {
        if(idExists(id)) {
            circles.remove(getCircleById(id));
        }
    }

    public void removeAllCircles() {
        circles = new ArrayList<>();
    }

    public CircleShape getCircleById(int id) {
        for(CircleShape circleShape : circles) {
            if(circleShape.id == id) {
                return circleShape;
            }
        }
        return null;
    }

    private boolean idExists(int id) {
        for(CircleShape circleShape : circles) {
            if(circleShape.id == id) {
                return true;
            }
        }
        return false;
    }

    public List<CircleShape> getCircles() {
        return circles;
    }
}
