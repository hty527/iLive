package com.android.gift.model;

import android.animation.TypeEvaluator;
import android.graphics.Point;

/**
 * TinyHung@Outlook.com
 * 2018/8/25
 * 二次方公式
 * 计算贝塞尔曲线运动轨迹
 */

public class BezierEvaluator implements TypeEvaluator<Point> {

    private final Point mPoint;

    public BezierEvaluator(Point point){
        this.mPoint=point;
    }

    @Override
    public Point evaluate(float fraction, Point startValue, Point endValue) {
        int x = (int) ((1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * mPoint.x + fraction * fraction * endValue.x);
        int y = (int) ((1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * mPoint.y + fraction * fraction * endValue.y);
        return new Point(x,y);
    }
}
