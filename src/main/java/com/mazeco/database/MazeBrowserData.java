package com.mazeco.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.mazeco.models.MazeRecord;
import com.mazeco.utilities.SortCriteria;
import com.mazeco.utilities.SortOrder;

public class MazeBrowserData {
    private static DefaultListModel<MazeRecord> listModel;
    private static JDBCMazeBrowserDataSource dbMazeBrowserData;

    private static MazeBrowserData instance = null;

    private MazeBrowserData() throws SQLException{
        listModel = new DefaultListModel<MazeRecord>();
        dbMazeBrowserData = new JDBCMazeBrowserDataSource();

        for(MazeRecord aMazeRecord : dbMazeBrowserData.retrieveAllMazeRecords()){
            listModel.addElement(aMazeRecord);
        }

    }

    public static void reSyncMazeRecords() throws SQLException {
        for(MazeRecord aMazeRecord : dbMazeBrowserData.retrieveAllMazeRecords()){
            if(listModel.contains(aMazeRecord)){
                int existingRecordIndex = listModel.indexOf(aMazeRecord);
                listModel.removeElementAt(existingRecordIndex);
                listModel.add(existingRecordIndex, aMazeRecord);
            } else {
                listModel.addElement(aMazeRecord);
            }
        }
    }

    
    public static void sortMazeRecords(SortCriteria criteria, SortOrder order) throws SQLException{
        ArrayList<MazeRecord> mazeRecords = dbMazeBrowserData.retrieveAllMazeRecords();
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
            System.out.println(aMazeRecord);
        }
        System.out.println("------");

    }

    public static void reverseMazeRecords() throws SQLException{
        ArrayList<MazeRecord> mazeRecords = dbMazeBrowserData.retrieveAllMazeRecords();
        
        listModel.removeAllElements();

        for (MazeRecord aMazeRecord : mazeRecords) {
            listModel.addElement(aMazeRecord);
            System.out.println(aMazeRecord);
        }
        System.out.println("------");
        
    }

    public void add(MazeRecord mazeRecord){
        if (!listModel.contains(mazeRecord)) {
            listModel.addElement(mazeRecord);
            dbMazeBrowserData.insertMazeRecord(mazeRecord);
         }
    }

    public void remove(MazeRecord mazeRecord){
        listModel.removeElement(mazeRecord);
        dbMazeBrowserData.deleteMazeRecord(mazeRecord.getId().toString());
    }

    public MazeRecord get(String id) {
        return dbMazeBrowserData.retrieveMazeRecord(id);
    }

    public int getSize() {
        return dbMazeBrowserData.getSize();
     }

    /**
    * Saves the data in the address book using a persistence
    * mechanism.
    */
    public void persist() {
        dbMazeBrowserData.close();
    }

    /**
    * Accessor for the list model.
    * 
    * @return the listModel to display.
    */
    public ListModel<MazeRecord> getModel() {
        return listModel;
    }

    public static MazeBrowserData getInstance() throws SQLException {
        if (instance == null)
            return instance = new MazeBrowserData() ;
       
        reSyncMazeRecords();
        return instance;
     }

}
