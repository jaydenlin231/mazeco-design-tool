package com.mazeco.models;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import com.mazeco.exception.UnsolvableMazeException;
import com.mazeco.utilities.MazeProblem;
import com.mazeco.utilities.MazeSolver;
import com.mazeco.utilities.Node;
/**
 *  Represents the data and configuration of a maze to be stored in {@code MazeRecord}.
 * 
 *  @see MazeRecord
 */
public class MazeModel implements Serializable{
    private static final long serialVersionUID = -4565786099626384425L;
    
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
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
    private double solutionPercentage;

    private String logo;
    private String startImage;
    private String endImage;

    /**
     * Construct a MazeModel with the given the {@code width} and {@code height} of the maze representation in blocks.
     *
     * @param width  width of the maze representation in blocks in the range 10 to 100
     * @param height height of the maze representation in blocks in the range 10 to 100
     * @throws IllegalArgumentException
     */
    public MazeModel(int width, int height) {
        if (width < MIN_WIDTH || width > MAX_WIDTH)
            throw new IllegalArgumentException();

        if (height < MIN_HEIGHT || height > MAX_HEIGHT)
            throw new IllegalArgumentException();

        this.width = width;
        this.height = height;
        this.data = new Matrix<Block>(width, height, Block.BLANK);
    }

    public MazeModel(int width, int height, int start, int end, String logo, String startImage, String endImage) {
        this(width, height);
        startX = start;
        endX = end;
        this.logo = logo;
        this.startImage = startImage;
        this.endImage = endImage;
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

    public MazeModel() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, 1, 7, null, null, null);
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

    private boolean logoClearanceWarning(int x, int y, int logoSize){
        for (int i = y - 2; i < y + logoSize + 2; i++) {
            for (int j = x - 2; j < x + logoSize + 2; j++) {
                if(getBlock(j, i).equals(Block.START) || getBlock(j, i).equals(Block.END)||getBlock(j, i).equals(Block.PATH))
                    return true;
            }
        }
        return false;
    }

    public void prepForLogo() {
        clearSolution();

        int logoSize = 0;

        if (width <= 15)
            logoSize = 2;
        else if (width <= 30)
            logoSize = 4;
        else if (width <= 45)
            logoSize = 6;
        else if (width <= 60)
            logoSize = 8;
        else if (width <= 75)
            logoSize = 10;
        else if (width <= 90)
            logoSize = 13;
        else if (width <= 100)
            logoSize = 15;

        Random rand = new Random();
        int x = rand.nextInt(width - logoSize * 2) + logoSize;
        int y = rand.nextInt(height - logoSize * 2) + logoSize;
        while(((x + logoSize) >= width - 1 || (y + logoSize) >= height - 1) || logoClearanceWarning(x, y, logoSize)){
            x = rand.nextInt(width - logoSize * 2) + logoSize;
            y = rand.nextInt(height - logoSize * 2) + logoSize;
        }

        for (int i = y; i < y + logoSize; i++) {
            for (int j = x; j < x + logoSize; j++) {
                data.insert(Block.LOGO, j, i);
            }
        }
    }

    public void removeLogo() {
        Point start = getStartLogoPoint();
        Point end = getEndLogoPoint();
        for (int i = start.y; i <= end.y; i++) {
            for (int j = start.x; j <= end.x; j++) {
                data.insert(Block.WALL, j, i);
            }
        }

    }

    public Point getStartLogoPoint() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data.get(j, i).equals(Block.LOGO)) {
                    return new Point(j, i);
                }
            }
        }
        return null;
    }

    public Point getEndLogoPoint() {
        Point point = null;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (data.get(j, i).equals(Block.LOGO)) {
                    point = new Point(j, i);
                }
            }
        }
        return point;
    }

    public synchronized void setLogoArea(Point start, Point end) {
        int minX = Math.min(start.x, end.x);
        int minY = Math.min(start.y, end.y);
        int maxX = Math.max(start.x, end.x);
        int maxY = Math.max(start.y, end.y);
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                if(getBlock(j, i) == Block.START || getBlock(j, i) == Block.END || getBlock(j, i) == Block.PATH){
                    return;
                }
            }
        }
        for (int i = minY; i <= maxY; i++) {
            for (int j = minX; j <= maxX; j++) {
                setBlock(Block.LOGO, j, i);
            }
        }
    }

    public Point getStartPosition() {
        return startPosition;
    }

    public Point getEndPosition() {
        return endPosition;
    }

    public int getStartX() {
        return startX;
    }

    public int getEndX() {
        return endX;
    }

    public String getLogo() {
        return logo;
    }

    public String getStartImage() {
        return startImage;
    }

    public String getEndImage() {
        return endImage;
    }

    public void setStartImage(String value) {
        startImage = value;
    }

    public void setEndImage(String value) {
        endImage = value;
    }

    public void setLogoImage(String value) {
        logo = value;
    }

    public void solve() throws UnsolvableMazeException {
        Node solutionNode = MazeSolver.aStarGraphSearch(new MazeProblem(this));

        solution = solutionNode.getSolutionPoints();

        for (Point aPathPoint : solution) {
            int currentCol = (int) aPathPoint.getX();
            int currentRow = (int) aPathPoint.getY();

            if (this.getBlock(currentCol, currentRow).equals(Block.START)
                    || this.getBlock(currentCol, currentRow).equals(Block.END))
                continue;

            this.setBlock(Block.PATH, currentCol, currentRow);
        }
    }

    public Double getSolutionPercentage() throws UnsolvableMazeException{
        solve();
        int pathCount = 0;
        int explorableblockCount = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (getBlock(j, i) == Block.PATH) {
                    pathCount++;
                    explorableblockCount++;
                }
                else if (getBlock(j, i) == Block.BLANK) {
                    explorableblockCount++;
                }
            }
        }
        
        solutionPercentage = (((double) pathCount / (double) explorableblockCount) * 100);
        // truncate
        int temp = (int)(solutionPercentage*100.0);
        solutionPercentage = ((double)temp)/100.0;
        return solutionPercentage;
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
