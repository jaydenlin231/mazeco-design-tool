package com.mazeco.models;

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

        if(height < MIN_HEIGHT || height > MAX_HEIGHT)
            throw new IllegalArgumentException();

        this.width = width;
        this.height = height;
        this.data = new Matrix<Block>(width, height, Block.BLANK);

    }

    
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resetData(){
        data.reset(Block.BLANK);
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
