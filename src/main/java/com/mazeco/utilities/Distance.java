package com.mazeco.utilities;

import java.awt.Point;

public final class Distance {
    public static int getManhanttanDistance(Point point1, Point point2){
        return (int) (Math.abs(point1.getX() - point2.getX()) + Math.abs(point1.getY() - point2.getY())) ;
    }
}
