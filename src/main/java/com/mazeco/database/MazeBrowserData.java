package com.mazeco.database;

import java.sql.SQLException;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.mazeco.models.MazeRecord;

public class MazeBrowserData {
    static DefaultListModel<MazeRecord> listModel;
    static JDBCMazeBrowserDataSource dbMazeBrowserData;

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
            try {
                return instance = new MazeBrowserData() ;
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        reSyncMazeRecords();
        return instance;
     }

}
