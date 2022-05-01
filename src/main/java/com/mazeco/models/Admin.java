package com.mazeco.models;

/**
 * Represents a super user of the MazeCo Design Tool GUI Application with admin access level
 */
public class Admin extends User{
    /**
     * Construct an Admin with the given the {@code firstName}, {@code lastName}, {@code username} and {@code password} of the User.
     */
    public Admin(String firstName, String lastName, String username, String password){
        super(firstName, lastName, username, password);
    }

    /**
     * Deletes a MazeRecord with admin permissions
     * 
     * @param aMazeRecord the MazeRecord to be deleted.
     * @return true if the MazeRecord has been succesfully deleted, false otherwise.
     */
    public boolean deleteMaze(MazeRecord aMazeRecord){
        return true;
    }

    /**
     * Exports a MazeRecord with admin permissions
     * 
     * @param aMazeRecord the MazeRecord to be exported.
     * @return true if the MazeRecord has been succesfully exported, false otherwise.
     */
    public boolean exportMaze(MazeRecord aMazeRecord){
        return true;
    }
}
