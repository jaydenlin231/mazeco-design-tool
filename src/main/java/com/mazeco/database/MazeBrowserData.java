package com.mazeco.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.ListModel;

import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.utilities.SortCriteria;
import com.mazeco.utilities.SortOrder;

/***
 *  Data Structure to store, manipulate and display the retrieved {@code MazeRecord} Objects from database.
 * 
 *  @see JDBCMazeBrowserDataSource
 */
public class MazeBrowserData {
    private static DefaultListModel<MazeRecord> listModel;

    private static MazeBrowserData instance = null;

    /**
    * Constructor initializes the list model that holds <code>MazeRecord</code> and
    * attempts to read any data saved in the database from previous invocations of 
    * the application.
    * 
    */
    private MazeBrowserData() throws SQLException {
        listModel = new DefaultListModel<MazeRecord>();
        // add the retrieved data to the list model
        for(MazeRecord aMazeRecord : JDBCMazeBrowserDataSource.getInstance().retrieveAllMazeRecords()){
            listModel.addElement(aMazeRecord);
        }
        instance = this;
    }

    /**
     * Provides global access to the singleton instance of {@code MazeBrowserData}.
     * 
     * @return a singleton instance of {@code MazeBrowserData}.
     */
    public static MazeBrowserData getInstance() throws SQLException {
        if (instance == null)
            new MazeBrowserData() ;
       
        reSyncMazeRecords();
        return instance;
    }

    /**
     * Synchronises the list model with any data saved in the database from 
     * previous invocations of the application.
     * 
     * @throws SQLException if a database error occurs.
     */
    public static void reSyncMazeRecords() throws SQLException {
        for(MazeRecord aMazeRecord : JDBCMazeBrowserDataSource.getInstance().retrieveAllMazeRecords()){
            if(listModel.contains(aMazeRecord)){
                int existingRecordIndex = listModel.indexOf(aMazeRecord);
                listModel.removeElementAt(existingRecordIndex);
                listModel.add(existingRecordIndex, aMazeRecord);
            } else {
                listModel.addElement(aMazeRecord);
            }
        }
    }

    /**
     * Sorts the{@code MazeRecord} elements in list model 
     * according to the input sort criteria and order.
     * 
     * @param criteria the {@code MazeRecord} field to sort by.
     *          {@code BY_AUTHOR} or {@code BY_NAME} or 
     *          {@code BY_CREATED} or {@code BY_MODIFIED}.
     * @param order the order to sort by. {@code ASC} or {@code DSC}.
     * @throws SQLException SQLException if a database error occurs.
     */
    public static void sortMazeRecords(SortCriteria criteria, SortOrder order) throws SQLException {
        ArrayList<MazeRecord> mazeRecords = JDBCMazeBrowserDataSource.getInstance().retrieveAllMazeRecords();
        switch (criteria) {
            case BY_AUTHOR:
            Collections.sort(mazeRecords, (a, b) -> a.getAuthor().toString().compareToIgnoreCase(b.getAuthor().toString()) );
                break;
            
            case BY_NAME:
                Collections.sort(mazeRecords, Comparator.comparing(MazeRecord::getName));
                break;
            
            case BY_CREATED:
                Collections.sort(mazeRecords, Comparator.comparing(MazeRecord::getDateTimeCreated));
                break;

            case BY_MODIFIED:
                Collections.sort(mazeRecords, Comparator.comparing(MazeRecord::getDateTimeModified));
                break;
        }

        if(order == SortOrder.DSC)
            Collections.reverse(mazeRecords);

        listModel.removeAllElements();

        for (MazeRecord aMazeRecord : mazeRecords) {
            listModel.addElement(aMazeRecord);
        }

    }

    /**
     * Adds a {@code MazeRecord} Object to the Maze Browser. 
     * 
     * @param mazeRecord {@code MazeRecord} Object to be added.
     * @throws SQLException if a database error occurs.
     */
    public void add(MazeRecord mazeRecord) throws SQLException {
        if (!listModel.contains(mazeRecord)) {
            listModel.addElement(mazeRecord);
            JDBCMazeBrowserDataSource.getInstance().insertMazeRecord(mazeRecord);
         }
    }

    /**
     * Updates a {@code MazeRecord} Object in the Maze Browser.
     * 
     * @param mazeID the UUID of the {@code MazeRecord} to update.
     * @param mazeModel the updated {@code MazeModel} Object for the record.
     * @param cleanImage the updated {@code ImageIcon} representation of the unsolved.
     * @param solvedImage the updated {@code ImageIcon} representation of the solved MazeModel.
     * @throws SQLException if a database error occurs.
     */
    public void update(UUID mazeID, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) throws SQLException {
        JDBCMazeBrowserDataSource.getInstance().updateMazeRecord(mazeID, mazeModel, cleanImage, solvedImage);
        reSyncMazeRecords();
    }

    /**
     * Deletes a {@code MazeRecord} Object from the Maze Browser.
     * 
     * @param mazeRecord {@code MazeRecord} Object to be deleted.
     * @throws SQLException if a database error occurs.
     */
    public void remove(MazeRecord mazeRecord) throws SQLException {
        listModel.removeElement(mazeRecord);
        JDBCMazeBrowserDataSource.getInstance().deleteMazeRecord(mazeRecord.getId().toString());
    }


    /**
     * Gets the number of {@code MazeRecord} Objects in the Maze Browser.
     * 
     * @return the number of {@code MazeRecord} Objects in the Maze Browser.
     * @throws SQLException if a database error occurs.
     */
    public int getSize() throws SQLException {
        return JDBCMazeBrowserDataSource.getInstance().getSize();
     }

    /**
     * Saves the data in the Maze Browser using a persistence mechanism.
     *
     * @throws SQLException if a database error occurs.
     */
    public static void persist() throws SQLException  {
        JDBCMazeBrowserDataSource.getInstance().close();
    }

    /**
    * Accessor for the list model.
    * 
    * @return the listModel to display.
    */
    public ListModel<MazeRecord> getModel() {
        return listModel;
    }

}
