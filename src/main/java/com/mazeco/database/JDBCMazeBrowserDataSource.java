package com.mazeco.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;

public class JDBCMazeBrowserDataSource {
    private Connection connection;

    public static final String CREATE_DB = "CREATE DATABASE mazeco;";

    public static final String CREATE_TABLE =
           "CREATE TABLE IF NOT EXISTS maze ("
                   + "id VARCHAR(36) PRIMARY KEY NOT NULL UNIQUE,"
                   + "name VARCHAR(30),"
                   + "author VARCHAR(30),"
                   + "dateTimeCreated TEXT,"
                   + "dateTimeModified TEXT,"
                   + "mazeModel BLOB" + ");";
    
    private static final String INSERT_MAZE_RECORD = "INSERT INTO maze (id, name, author, dateTimeCreated, dateTimeModified, mazeModel) VALUES (?, ?, ?, ?, ?, ?);";

    private static final String GET_MAZE_RECORD = "SELECT * FROM maze WHERE id=?";

    private static final String GET_ALL_MAZE_RECORD = "SELECT * FROM maze";

    private static final String DELETE_MAZE_RECORD = "DELETE FROM maze WHERE id=?";

    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM maze";


    private PreparedStatement insertMazeRecord;

    private PreparedStatement getMazeRecord;

    private PreparedStatement getAllMazeRecords;

    private PreparedStatement deleteMazeRecord;

   private PreparedStatement rowCount;

    
    public JDBCMazeBrowserDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            // st.execute(CREATE_DB);
            st.execute(CREATE_TABLE);
            // connection.commit();
            insertMazeRecord = connection.prepareStatement(INSERT_MAZE_RECORD);
            getMazeRecord = connection.prepareStatement(GET_MAZE_RECORD);
            getAllMazeRecords = connection.prepareStatement(GET_ALL_MAZE_RECORD);
            deleteMazeRecord = connection.prepareStatement(DELETE_MAZE_RECORD);
            rowCount = connection.prepareStatement(COUNT_ROWS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void insertMazeRecord(MazeRecord mazeRecord){
        try {
            insertMazeRecord.setString(1, mazeRecord.getId().toString());
            insertMazeRecord.setString(2, mazeRecord.getName());
            insertMazeRecord.setString(3, mazeRecord.getAuthor().getFirstName() + " " + mazeRecord.getAuthor().getLastName() );
            insertMazeRecord.setObject(4, mazeRecord.getDateTimeCreated().toInstant());
            insertMazeRecord.setObject(5, mazeRecord.getDateTimeModified().toInstant());

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

            objectStream.writeObject(mazeRecord.getMazeModel());
            byte[] data = byteStream.toByteArray();
            insertMazeRecord.setBinaryStream(6, new ByteArrayInputStream(data),
            data.length);

            insertMazeRecord.execute();

         } catch (SQLException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MazeRecord retrieveMazeRecord(String id) {
        ResultSet rs = null;
        byte[] data;
        MazeModel mazeModel = null;
        MazeRecord mazeRecord = null;
        try {
            getMazeRecord.setString(1, id);
            rs = getMazeRecord.executeQuery();

            if(!rs.next())
                throw new InvalidParameterException();

            String resultName = rs.getString("name");
            String resultAuthor = rs.getString("author");

            Instant resultInstantCreated = Instant.parse(rs.getString("dateTimeCreated"));
            Instant resultInstantModified = Instant.parse(rs.getString("dateTimeModified"));
            
            ZoneId zid = ZoneId.systemDefault();
            ZonedDateTime resultDateTimeCreated = resultInstantCreated.atZone(zid);
            ZonedDateTime resultDateTimeModified = resultInstantModified.atZone(zid);

            byte[] mazeModelbyteArr = rs.getBytes("mazeModel");
            // data = blob.getBytes(1, (int) blob.length());
            // convert bytes back to object stream
            ByteArrayInputStream byteStream = new ByteArrayInputStream(mazeModelbyteArr);
            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
            mazeModel = (MazeModel) objectStream.readObject();

            mazeRecord = new MazeRecord(id, resultName, resultAuthor, resultDateTimeCreated,  resultDateTimeModified, mazeModel);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return mazeRecord;
     }

     public  ArrayList<MazeRecord> retrieveAllMazeRecords() throws SQLException {
        ResultSet rs = null;
        byte[] data;
        ArrayList<MazeRecord> mazeRecords = new ArrayList<MazeRecord>();
        
        // getMazeRecord.setString(1, id);
        rs = getAllMazeRecords.executeQuery();

        while(rs.next()){
            String resultId = rs.getString("id");
            MazeRecord resultMazeRecord = retrieveMazeRecord(resultId);

            mazeRecords.add(resultMazeRecord);
        }

        return mazeRecords;
    }

    public void deleteMazeRecord(String id){
        try {
            deleteMazeRecord.setString(1, id);
            deleteMazeRecord.executeUpdate();
         } catch (SQLException e) {
            e.printStackTrace();
         }
    }

    public int getSize() {
        ResultSet rs = null;
        int rows = 0;
  
        /* BEGIN MISSING CODE */
        try {
           rs = rowCount.executeQuery();
           rs.next();
           rows = rs.getInt(1);
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        /* END MISSING CODE */
  
        return rows;
     }

    public void close() {
        /* BEGIN MISSING CODE */
        try {
           connection.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
        /* END MISSING CODE */
     }
}   
