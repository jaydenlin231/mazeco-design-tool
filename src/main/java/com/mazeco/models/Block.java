package com.mazeco.models;

/**
 * Represents a Block of the grid constrained maze representation,
 * the elemetary building blocks of the maze
 * 
 * @see MazeModel
 */
public enum Block {
    WALL, BLANK, START, END, LOGO;

     /**
     * Returns String representation of a Block,
     * the string representation is the starting letter of the enumerate.
     */
    @Override
    public String toString() {
        return name().substring(0, 1);
    }
}
