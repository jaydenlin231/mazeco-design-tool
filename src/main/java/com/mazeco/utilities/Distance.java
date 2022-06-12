package com.mazeco.utilities;

import java.awt.Point;

/**
 * Convinient utility class for calculating distance between {@code Point} Objects.
 */
public final class Distance {

    /**
     * Caculating the Manhanttan Distance between two {@code Point} Objects.
     * This function is also used as the Heuristic function h(n) for the 
     * A* algorithm used for maze solving.
     * 
     * @param point1 a {@code Point} Object.
     * @param point2 another {@code Point} Object.
     * @return integer representing the two given {@code Point} Objects in number of blocks.
     * 
     * @see MazeSolver
     */
    public static int getManhanttanDistance(Point point1, Point point2){
        return (int) (Math.abs(point1.getX() - point2.getX()) + Math.abs(point1.getY() - point2.getY())) ;
    }
}
