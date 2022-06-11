package com.mazeco.models;

/**
 * Represents a user of the MazeCo Design Tool GUI Application with generic access level
 */
public class User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String username;
    protected String password;

    /**
     * Construct a User with the given the {@code firstName}, {@code lastName}, {@code username} and {@code password} of the User.
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     */
    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Deletes a MazeRecord with admin permissions, 
     * the MazeRecord can only be deleted if the User was the author of the MazeRecord.
     * 
     * @param aMazeRecord the MazeRecord to be deleted.
     * @return true if the MazeRecord has been succesfully deleted, false otherwise.
     */
    public boolean deleteMaze(MazeRecord aMazeRecord){
        return true;
    }

    /**
     * Returns String representation of the User in the format {@code "lastName, firstName"}
     *
     * @return string representation of the User
     */
    @Override
    public String toString(){
        return firstName + "," + lastName;
    }
    
}
