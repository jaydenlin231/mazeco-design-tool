package com.mazeco.utilities;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import java.awt.Point;
import java.util.ArrayList;


/**
 * Represents an instance of a maze solving problem and defines the operation logic for {@code MazeSolver}.
 * 
 * @see MazeSolver
 */
public final class MazeProblem {
    MazeModel mazeModel;
    Point initial;

    public MazeProblem(MazeModel mazeModel){
        this.mazeModel = mazeModel;
        this.initial = mazeModel.getStartPosition();
    }

    /**
     * Gets a list of legal actions from the current state.
     * 
     * @param state {@code Point} Object representing the (x,y) coordinates of the currently visiting block.
     * @return ArrayList of Strings representing the legal actions from the current state. 
     *         i.e. 'Up', 'Down', 'Left', 'Right'
     */
    public ArrayList<String> actions(Point state){
        ArrayList<String> availableActions = new ArrayList<String>();

        int currentCol = (int) state.getX();
        int currentRow = (int) state.getY();
        
        if((currentCol + 1) < mazeModel.getWidth()){
            if(!mazeModel.getBlock(currentCol + 1, currentRow).equals(Block.WALL)&&!mazeModel.getBlock(currentCol + 1, currentRow).equals(Block.LOGO)){
                availableActions.add("Right");
            }
        }
        if((currentCol - 1) >= 0){
            if(!mazeModel.getBlock(currentCol - 1, currentRow).equals(Block.WALL)&&!mazeModel.getBlock(currentCol - 1, currentRow).equals(Block.LOGO)){
                availableActions.add("Left");
            }
        }
        if((currentRow + 1) < mazeModel.getHeight()){
            if(!mazeModel.getBlock(currentCol, currentRow + 1).equals(Block.WALL)&&!mazeModel.getBlock(currentCol, currentRow + 1).equals(Block.LOGO)){
                availableActions.add("Down");
            }
        }
        if((currentRow - 1) >= 0){
            if(!mazeModel.getBlock(currentCol, currentRow - 1).equals(Block.WALL)&&!mazeModel.getBlock(currentCol, currentRow - 1).equals(Block.LOGO)){
                availableActions.add("Up");
            }
        }
        
        return availableActions;
    }


    /**
     * Gets the resulting {@code Point} Object after an action has been applied to a problem state.
     * 
     * @param state {@code Point} Object representing the (x,y) coordinates of the currently visiting block.
     * @param action String representing the action to apply to the current state.
     *        i.e. 'Up', 'Down', 'Left', 'Right'
     * @return the resulting {@code Point} Object after an action has been applied if the move was successful.
     *         Else throws IllegalArgumentException.
     * 
     * @throws IllegalArgumentException if the action cannot be applied to the current state.
     */
    public Point result(Point state, String action){
        int currentCol = (int) state.getX();
        int currentRow = (int) state.getY();

        switch (action) {
            case "Right":
                return new Point(currentCol + 1, currentRow);

            case "Left":
                return new Point(currentCol - 1, currentRow);

            case "Down":
                return new Point(currentCol, currentRow + 1);

            case "Up":
                return new Point(currentCol, currentRow - 1);
        
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Tests if a given problem state is the desired goal state. i.e. find the maze exit.
     * 
     * @param state {@code Point} Object representing the (x,y) coordinates of the currently visiting block to test.
     * @return {@code true} if the given state is the goal state, {@code false} otherwise.
     */
    public boolean goalTest(Point state){
        return state.equals(mazeModel.getEndPosition());
    }

    /**
     * Deternines the path cost of the problem. Each move for the maze solving problem costs 1 unit.
     * 
     * @param cost the current cost to reach the problem state.
     * @return  the current cost plus 1 unit. i.e. all moves cost equally as 1 unit. 
     */
    public int pathCost(int cost){
        return cost += 1;
    }
}
