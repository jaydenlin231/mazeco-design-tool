package com.mazeco.database;

import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.mazeco.models.MazeRecord;

public class MazeBrowserData {
    DefaultListModel<MazeRecord> listModel;
    JDBCMazeBrowserDataSource dbMazeBrowserData;

    public MazeBrowserData() throws SQLException{
        listModel = new DefaultListModel<MazeRecord>();
        dbMazeBrowserData = new JDBCMazeBrowserDataSource();

        for(MazeRecord aMazeRecord : dbMazeBrowserData.retrieveAllMazeRecords()){
            listModel.addElement(aMazeRecord);
        }

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

}
