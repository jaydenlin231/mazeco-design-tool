package com.mazeco.utilities;

/**
 * Enumerate to specify the criteria to sort the MazeRecords by in the Maze Browser Window.
 *
 * @see BrowseWindow
 * @see SortOrder
 */
public enum SortCriteria {
    BY_AUTHOR("Author"), 
    BY_NAME("Name"), 
    BY_CREATED("Date Created"),
    BY_MODIFIED("Date Modified");

    private final String label;

    /**
     * Default constructor to set the SortCriteria
     *
     * @param label SortCriteria for the MazeRecords in the Maze Browser Window.
     */
    private SortCriteria(String label){
        this.label = label;
    }

    /**
     * @return Returns the current SortCriteria in string
     */
    @Override
    public String toString() {
        return label;
    }
}