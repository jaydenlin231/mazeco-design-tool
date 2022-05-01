package com.mazeco.utilities;

import java.util.List;
import java.awt.geom.Point2D;

import com.mazeco.models.MazeModel;

/**
 * Static Utility Class to find the solution of a MazeModel
 */
public final class MazeSolver {
    /**
     * Solves the given {@code aMazeModel} using A star algorithm.
     * @param aMazeModel the MazeModel to be solved
     * @return path the list of {@code Point2D} Objects which represents the path from the start to end block.
     */
    public static List<Point2D> solveAStar(MazeModel aMazeModel){
        return null;
    }

    /**
     * Solves the given {@code aMazeModel} using Dijkstra algorithm.
     * @param aMazeModel the MazeModel to be solved
     * @return path the list of {@code Point2D} Objects which represents the path from the start to end block.
     */
    public static List<Point2D> solveDijkstra(MazeModel aMazeModel){
        return null;
    }

    /**
     * Calculate of the cost of the solution returned by searching algorithms.
     * @param path list of {@code Point2D} Objects which represents the path from the start to end block.
     * @return cost the number of moves required to solve the puzzle with the given path.
     */
    public static int getPathCost(List<Point2D> path){
        return 0;
    }
}
