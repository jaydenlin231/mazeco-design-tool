package com.mazeco.models;

import java.awt.Point;
import java.util.ArrayList;

import com.mazeco.utilities.MazeProblem;
import com.mazeco.utilities.MazeSolver;
import com.mazeco.utilities.Node;
/**
 *  Represents the data and configuration of a maze to be stored in {@code MazeRecord}.
 * 
 *  @see MazeRecord
 */
public class MazeModel {
    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = 100;

    private int width;
    private int height;
    private Matrix<Block> data;

    private int startX;
    private int endX;
    private Point startPosition;
    private Point endPosition;
    private ArrayList<Point> solution;

    /**
     * Construct a MazeModel with the given the {@code width} and {@code height} of the maze representation in blocks.
     *
     * @param width width of the maze representation in blocks in the range 10 to 100
     * @param height height of the maze representation in blocks in the range 10 to 100
     * 
     * @throws IllegalArgumentException
     */
    public MazeModel(int width, int height) {
        if(width < MIN_WIDTH || width > MAX_WIDTH)
            throw new IllegalArgumentException();

        if (height < MIN_HEIGHT || height > MAX_HEIGHT)
            throw new IllegalArgumentException();

        this.width = width;
        this.height = height;
        this.data = new Matrix<Block>(width, height, Block.BLANK);

    }

    public MazeModel(int width, int height, int start, int end) {
        this(width, height);
        startX = start;
        endX = end;
        this.startPosition = new Point(startX, 0);
        this.endPosition = new Point(endX, height - 1);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0 && j == startX) {
                    data.insert(Block.START, j, i);
                }
                if (i == data.getHeight() - 1 && j == endX) {
                    data.insert(Block.END, j, i);
                }
            }
        }

    }


    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resetData() {
        data.reset(Block.BLANK);
        data.insert(Block.START, startX, 0);
        data.insert(Block.END, endX, height - 1);
    }

    public void prepForGenerator() {
        data.reset(Block.WALL);
        data.insert(Block.START, startX, 0);
        data.insert(Block.END, endX, height - 1);
    }

    public Point getStartPosition() {
        return startPosition;
    }

    public Point getEndPosition() {
        return endPosition;
    }

    public void solve() {
        Node solutionNode = MazeSolver.aStarGraphSearch(new MazeProblem(this));

        solution = solutionNode.getSolutionPoints();

        for (Point aPathPoint : solution) {
            int currentCol = (int) aPathPoint.getX();
            int currentRow = (int) aPathPoint.getY();

            if(this.getBlock(currentCol, currentRow).equals(Block.START) 
            || this.getBlock(currentCol, currentRow).equals(Block.END))
                continue;

            this.setBlock(Block.PATH, currentCol, currentRow);
        }
    }

    public ArrayList<Point> getSolution(){
        return solution;
    }

    public void clearSolution(){
        if(solution == null)
            return; 
            
        for (Point aPathPoint : solution) {
            int currentCol = (int) aPathPoint.getX();
            int currentRow = (int) aPathPoint.getY();

            if(this.getBlock(currentCol, currentRow).equals(Block.START) 
            || this.getBlock(currentCol, currentRow).equals(Block.END))
                continue;

            this.setBlock(Block.BLANK, currentCol, currentRow);
        }

        solution = null;
    }

    /**
     * Gets the {@code Block} object with the given row and column index from the maze representation.
     *
     * @param row index of the row to access
     * @param col index of the column to access
     * @return Block {@code Block} object at the given row and column index of the maze representation
     */
    public Block getBlock(int col, int row){
        if(col > width - 1)
            throw new IndexOutOfBoundsException();
        if(row > height - 1)
            throw new IndexOutOfBoundsException();

        return data.get(col, row);
    }

    /**
     * Sets the {@code Block} object of the given row and column index in the maze representation.
     *
     * @param block {@code Block} Object to set
     * @param row index of the row to access
     * @param col index of the column to access
     */
    public void setBlock(Block block, int col, int row){
        if(col > width - 1)
            throw new IndexOutOfBoundsException();
        if(row > height - 1)
            throw new IndexOutOfBoundsException();

        data.insert(block, col, row);

        if(block.equals(Block.START))
            startPosition = new Point(col, row);

        if(block.equals(Block.END))
            endPosition = new Point(col, row);
    }


     /**
     * Returns String representation of the maze in a grid like fashion,
     * each non space character in the string represents a {@code Block}.
     * 
     * @return string string representation of the maze 
     * @see Block#toString()
     */
    @Override
    public String toString() {
       return data.toString();
    }

    
}
