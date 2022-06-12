package com.mazeco.utilities;


/**
 * Enumerate to specify the criteria to sort the MazeRecords by in the Maze Browser Window.
 *
 * @see BrowseWindow
 * @see SortCriteria
 */
public enum SortOrder {
    ASC("Ascending"),
    DSC("Descending");

    private final String label;

    /**
     * Default constructor to set the SortOrder
     *
     * @param label SortOrder for the MazeRecords in the Maze Browser Window.
     */
    private SortOrder(String label){
        this.label = label;
    }

    /**
     * @return Returns the current SortOrder in string
     */
    @Override
    public String toString() {
        return label;
    }
}