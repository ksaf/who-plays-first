package com.velen.whoplaysfirst.circles;

import android.graphics.Paint;

public class CircleShape {

    int id;
    int x;
    int y;
    Paint paint;

    public CircleShape(int id, int x, int y, Paint paint) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.paint = paint;
    }
}
