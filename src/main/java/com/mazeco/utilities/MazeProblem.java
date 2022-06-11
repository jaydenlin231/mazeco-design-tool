package com.mazeco.utilities;

import com.mazeco.models.Block;
import com.mazeco.models.MazeModel;

import java.awt.Point;
import java.util.ArrayList;

public final class MazeProblem {
    MazeModel mazeModel;
    Point initial;

    public MazeProblem(MazeModel mazeModel){
        this.mazeModel = mazeModel;
        this.initial = mazeModel.getStartPosition();
    }

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

    public boolean goalTest(Point state){
        return state.equals(mazeModel.getEndPosition());
    }

    public int pathCost(int cost){
        return cost += 1;
    }
}
