package com.mazeco.models;

/**
 * Represents a user of the MazeCo Design Tool GUI Application with generic access level
 */
public class User {
    protected String id;
    protected String firstName;
    protected String lastName;

    /**
     * Construct a User with the given the {@code firstName}, {@code lastName}, {@code username} and {@code password} of the User.
     * @param firstName
     * @param lastName
     * @param username
     * @param password
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Gets the first name of the User.
     * 
     * @return the first name of the User.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the User.
     * 
     * @param firstName the first name of the User.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the User.
     * 
     * @return the last name of the User.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the last name of the User.
     * 
     * @param lastName the last name of the User.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
