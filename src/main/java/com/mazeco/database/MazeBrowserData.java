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

public class MazeBrowserData {
    private static DefaultListModel<MazeRecord> listModel;
    // private static JDBCMazeBrowserDataSource dbMazeBrowserData;

    private static MazeBrowserData instance = null;

    private MazeBrowserData() throws SQLException {
        listModel = new DefaultListModel<MazeRecord>();
        // dbMazeBrowserData = JDBCMazeBrowserDataSource();

        for(MazeRecord aMazeRecord : JDBCMazeBrowserDataSource.getInstance().retrieveAllMazeRecords()){
            listModel.addElement(aMazeRecord);
        }
        instance = this;
    }

    public static MazeBrowserData getInstance() throws SQLException {
        if (instance == null)
            new MazeBrowserData() ;
       
        reSyncMazeRecords();
        return instance;
     }

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

    
    public static void sortMazeRecords(SortCriteria criteria, SortOrder order) throws SQLException {
        ArrayList<MazeRecord> mazeRecords = JDBCMazeBrowserDataSource.getInstance().retrieveAllMazeRecords();
        switch (criteria) {
            case BY_AUTHOR:
                break;
            
            case BY_NAME:
                Collections.sort(mazeRecords, Comparator
                .comparing(MazeRecord::getName));
                break;
            
            case BY_CREATED:
                Collections.sort(mazeRecords, Comparator
                .comparing(MazeRecord::getDateTimeCreated));
                break;

            case BY_MODIFIED:
                Collections.sort(mazeRecords, Comparator
                .comparing(MazeRecord::getDateTimeModified));
                break;
        }

        if(order == SortOrder.DSC)
            Collections.reverse(mazeRecords);

        listModel.removeAllElements();

        for (MazeRecord aMazeRecord : mazeRecords) {
            listModel.addElement(aMazeRecord);
        }

    }

    public void add(MazeRecord mazeRecord) throws SQLException {
        if (!listModel.contains(mazeRecord)) {
            listModel.addElement(mazeRecord);
            JDBCMazeBrowserDataSource.getInstance().insertMazeRecord(mazeRecord);
         }
    }

    public void update(UUID mazeID, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) throws SQLException {
        JDBCMazeBrowserDataSource.getInstance().updateMazeRecord(mazeID, mazeModel, cleanImage, solvedImage);
        reSyncMazeRecords();
    }

    public void remove(MazeRecord mazeRecord) throws SQLException {
        listModel.removeElement(mazeRecord);
        JDBCMazeBrowserDataSource.getInstance().deleteMazeRecord(mazeRecord.getId().toString());
    }

    public MazeRecord get(String id) throws SQLException {
        return JDBCMazeBrowserDataSource.getInstance().retrieveMazeRecord(id);
    }

    public int getSize() throws SQLException {
        return JDBCMazeBrowserDataSource.getInstance().getSize();
     }

    /**
    * Saves the data in the address book using a persistence
    * mechanism.
     * @throws SQLException
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
