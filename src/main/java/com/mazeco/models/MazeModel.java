package com.mazeco.models;

public class MazeModel {
    private static final int MIN_WIDTH = 10;
    private static final int MIN_HEIGHT = 10;
    private static final int MAX_WIDTH = 100;
    private static final int MAX_HEIGHT = 100;

    private int width;
    private int height;
    private Block[][] data;


    public MazeModel(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new Block[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                data[i][j] = new Block();
            }
        } 
    }


    public int getWidth() {
        return width;
    }


    public void setWidth(int width) {
        this.width = width;
    }


    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        this.height = height;
    }


    public Block[][] getMetaData() {
        return data;
    }


    public void setMetaData(Block[][] metaData) {
        this.data = metaData;
    }

    public Block getBlock(int row, int col){
        return this.data[row][col];
    }

    public void setBlockType(BlockType type, int row, int col){
        this.data[row][col].setType(type);
    }

    public void setBlock(Block block, int row, int col){
        this.data[row][col] = block;
    }


    @Override
    public String toString() {
        if(data == null)
            return "null";
        
        String mazeModelString = "";

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Block aBlock = data[i][j];
                mazeModelString += aBlock.toString() + " " ;
            }
            mazeModelString += "\n" ;
        }
        
        return mazeModelString;
    }

    
}
