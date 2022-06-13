package com.mazeco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.*;
import java.awt.Point;

import com.mazeco.utilities.Distance;

public class DistanceTest {
    @Test
    public void testManhanttanDistanceSamePoints()
    {
        Point point1 = new Point(0,0);
        Point point2 = new Point(0,0);
        assertEquals(0, Distance.getManhanttanDistance(point1, point2));
    }
    @Test
    public void testManhanttanDistanceHorizontal()
    {
        Point point1 = new Point(0,0);
        Point point2 = new Point(5,0);
        assertEquals(5, Distance.getManhanttanDistance(point1, point2));
    }

    @Test
    public void testManhanttanDistanceReverseHorizontal()
    {
        Point point1 = new Point(5,0);
        Point point2 = new Point(0,0);
        assertEquals(5, Distance.getManhanttanDistance(point1, point2));
    }
    
    @Test
    public void testManhanttanDistanceVertical()
    {
        Point point1 = new Point(0,0);
        Point point2 = new Point(0,5);
        assertEquals(5, Distance.getManhanttanDistance(point1, point2));
    }
    
    @Test
    public void testManhanttanDistanceReverseVertical(){
        Point point1 = new Point(0,5);
        Point point2 = new Point(0,0);
        assertEquals(5, Distance.getManhanttanDistance(point1, point2));
    }

    @Test
    public void testManhanttanDistanceDiagonal()
    {
        Point point1 = new Point(0,0);
        Point point2 = new Point(5,5);
        assertEquals(10, Distance.getManhanttanDistance(point1, point2));
    }

    @Test
    public void testManhanttanDistanceReverseDiagonal()
    {
        Point point1 = new Point(5,5);
        Point point2 = new Point(0,0);
        assertEquals(10, Distance.getManhanttanDistance(point1, point2));
    }

}
