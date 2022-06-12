package com.mazeco.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.UUID;
import javax.swing.ImageIcon;

import com.mazeco.models.MazeModel;
import com.mazeco.models.MazeRecord;
import com.mazeco.utilities.MazeExporter;

/**
 * Class for retrieving data from SQL database storing {@code MazeRecord} Objects.
 */
public class JDBCMazeBrowserDataSource {
    private static Connection connection;

    public static final String CREATE_DB = "CREATE DATABASE mazeco;";

    public static final String CREATE_TABLE =
           "CREATE TABLE IF NOT EXISTS maze ("
                   + "id VARCHAR(36) PRIMARY KEY NOT NULL UNIQUE,"
                   + "name VARCHAR(30),"
                   + "author VARCHAR(30),"
                   + "dateTimeCreated TEXT,"
                   + "dateTimeModified TEXT,"
                   + "mazeModel BLOB," 
                   + "cleanImage BLOB," 
                   + "solveImage BLOB" 
                   + ");";
    
    private static final String INSERT_MAZE_RECORD = "INSERT INTO maze (id, name, author, dateTimeCreated, dateTimeModified, mazeModel, cleanImage, solveImage) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_MAZE_RECORD = "UPDATE maze SET dateTimeModified = ?, mazeModel = ?, cleanImage = ?, solveImage = ? WHERE id = ?";
    private static final String GET_MAZE_RECORD = "SELECT * FROM maze WHERE id=?";
    private static final String GET_ALL_MAZE_RECORD = "SELECT * FROM maze";
    private static final String DELETE_MAZE_RECORD = "DELETE FROM maze WHERE id=?";
    private static final String COUNT_ROWS = "SELECT COUNT(*) FROM maze";

    private PreparedStatement insertMazeRecord;
    private PreparedStatement updatetMazeRecord;
    private PreparedStatement getMazeRecord;
    private PreparedStatement getAllMazeRecords;
    private PreparedStatement deleteMazeRecord;
    private PreparedStatement rowCount;

    private static JDBCMazeBrowserDataSource instance = null;

    /**
     * Constructor intialises the database connection, creates table if it doesn't exist,
     * and initiliases JDBC prepared statements.
     */
    private JDBCMazeBrowserDataSource() {
        connection = DBConnection.getInstance();
        try {
            Statement st = connection.createStatement();
            st.execute(CREATE_TABLE);
            insertMazeRecord = connection.prepareStatement(INSERT_MAZE_RECORD);
            updatetMazeRecord = connection.prepareStatement(UPDATE_MAZE_RECORD);
            getMazeRecord = connection.prepareStatement(GET_MAZE_RECORD);
            getAllMazeRecords = connection.prepareStatement(GET_ALL_MAZE_RECORD);
            deleteMazeRecord = connection.prepareStatement(DELETE_MAZE_RECORD);
            rowCount = connection.prepareStatement(COUNT_ROWS);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        instance = this;
    }

    /**
     * Provides global access to the singleton instance of {@code JDBCMazeBrowserDataSource}.
     * 
     * @return a singleton instance of {@code JDBCMazeBrowserDataSource}.
     */
    public static JDBCMazeBrowserDataSource getInstance() {
        if (instance == null){
            new JDBCMazeBrowserDataSource();
        } else {
            connection = DBConnection.getInstance();
        }
        return instance;
    }

    /**
     * Saves a {@code MazeRecord} Object to the database.
     * 
     * @param mazeRecord a {@code MazeRecord} Object to add to the database.
     * @throws SQLException if a database error occurs.
     */
    public void insertMazeRecord(MazeRecord mazeRecord) throws SQLException {
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

            ByteArrayOutputStream cleanImgByteStream = new ByteArrayOutputStream();
            ObjectOutputStream cleanImgObjectStream = new ObjectOutputStream(cleanImgByteStream);

            cleanImgObjectStream.writeObject(mazeRecord.getCleanImage());
            byte[] cleanImageData = cleanImgByteStream.toByteArray();
            insertMazeRecord.setBinaryStream(7, new ByteArrayInputStream(cleanImageData),
            cleanImageData.length);
            
            ByteArrayOutputStream solveImgByteStream = new ByteArrayOutputStream();
            ObjectOutputStream solveImgOjectStream = new ObjectOutputStream(solveImgByteStream);

            solveImgOjectStream.writeObject(mazeRecord.getSolvedImage());
            byte[] solveImageData = solveImgByteStream.toByteArray();
            insertMazeRecord.setBinaryStream(8, new ByteArrayInputStream(solveImageData),
            solveImageData.length);

            insertMazeRecord.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing {@code MazeRecord} Object in the database.
     * 
     * @param mazeID the UUID of the {@code MazeRecord} to update.
     * @param mazeModel the updated {@code MazeModel} Object for the record.
     * @param cleanImage the updated {@code ImageIcon} representation of the unsolved {@code MazeModel}.
     * @param solvedImage the updated {@code ImageIcon} representation of the solved {@code MazeModel}.
     * @throws SQLException if a database error occurs.
     */
    public void updateMazeRecord(UUID mazeID, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) throws SQLException {
        try {
            updatetMazeRecord.setObject(1, ZonedDateTime.now().toInstant());

            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);

            objectStream.writeObject(mazeModel);
            byte[] data = byteStream.toByteArray();
            updatetMazeRecord.setBinaryStream(2, new ByteArrayInputStream(data),
            data.length);

            ByteArrayOutputStream cleanImgByteStream = new ByteArrayOutputStream();
            ObjectOutputStream cleanImgObjectStream = new ObjectOutputStream(cleanImgByteStream);

            cleanImgObjectStream.writeObject(cleanImage);
            byte[] cleanImageData = cleanImgByteStream.toByteArray();
            updatetMazeRecord.setBinaryStream(3, new ByteArrayInputStream(cleanImageData),
            cleanImageData.length);
            
            ByteArrayOutputStream solveImgByteStream = new ByteArrayOutputStream();
            ObjectOutputStream solveImgOjectStream = new ObjectOutputStream(solveImgByteStream);

            solveImgOjectStream.writeObject(solvedImage);
            byte[] solveImageData = solveImgByteStream.toByteArray();
            updatetMazeRecord.setBinaryStream(4, new ByteArrayInputStream(solveImageData),
            solveImageData.length);

            updatetMazeRecord.setString(5, mazeID.toString());

            updatetMazeRecord.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the {@code MazeRecord} Object from the database given an ID.
     * 
     * @param id the UUID of the {@code MazeRecord} to retrieve.
     * @return the queried {@code MazeRecord} Object.
     * @throws SQLException if a database error occurs.
     */
    public MazeRecord retrieveMazeRecord(String id) throws SQLException {
        ResultSet rs = null;
        MazeModel mazeModel = new MazeModel();
        MazeRecord mazeRecord = null;
        ImageIcon cleanImage = new ImageIcon(MazeExporter.paint(mazeModel, 32));
        ImageIcon solveImage = new ImageIcon(MazeExporter.paint(mazeModel, 32));
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
            ByteArrayInputStream byteStream;

            try {
                byteStream = new ByteArrayInputStream(mazeModelbyteArr);
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                mazeModel = (MazeModel) objectStream.readObject();

                byte[] cleanImageByteArr = rs.getBytes("cleanImage");
                ByteArrayInputStream cleanImgByteStream = new ByteArrayInputStream(cleanImageByteArr);
                ObjectInputStream cleanImgObjectStream = new ObjectInputStream(cleanImgByteStream);
                cleanImage = (ImageIcon) cleanImgObjectStream.readObject();

                byte[] solveImageByteArr = rs.getBytes("solveImage");
                ByteArrayInputStream solveImageByteStream = new ByteArrayInputStream(solveImageByteArr);
                ObjectInputStream solveImageObjectStream = new ObjectInputStream(solveImageByteStream);
                solveImage = (ImageIcon) solveImageObjectStream.readObject();

            } catch (NullPointerException e) {
                resultName = "CORRUPTED RECORD";
            }

            mazeRecord = new MazeRecord(id, resultName, resultAuthor, resultDateTimeCreated,  resultDateTimeModified, mazeModel, cleanImage, solveImage);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        return mazeRecord;
     }

     /**
      * Retrieves all the {@code MazeRecord} Objects from the database.
      *
      * @return an ArrayList of {@code MazeRecord} Objects.
      * @throws SQLException if a database error occurs.
      */
     public  ArrayList<MazeRecord> retrieveAllMazeRecords() throws SQLException {
        ResultSet rs = null;
        ArrayList<MazeRecord> mazeRecords = new ArrayList<MazeRecord>();
        
        rs = getAllMazeRecords.executeQuery();

        while(rs.next()){
            String resultId = rs.getString("id");
            MazeRecord resultMazeRecord = retrieveMazeRecord(resultId);

            mazeRecords.add(resultMazeRecord);
        }

        return mazeRecords;
    }

    /**
     * Deletes the {@code MazeRecord} Object from the database given an ID.
     * 
     * @param id the UUID of the {@code MazeRecord} to delete.
     * @throws SQLException if a database error occurs.
     */
    public void deleteMazeRecord(String id) throws SQLException{
        deleteMazeRecord.setString(1, id);
        deleteMazeRecord.executeUpdate();
    }

    /**
     * Query for the number of {@code MazeRecord} Objects in the database.
     * 
     * @return the number of {@code MazeRecord} Object in the database.
     * @throws SQLException if a database error occurs.
     */
    public int getSize() throws SQLException {
        ResultSet rs = null;
        int rows = 0;
  
        rs = rowCount.executeQuery();
        rs.next();
        rows = rs.getInt(1);
  
        return rows;
     }

    /**
     * Finalizes any resources used by the database connection
     * and ensures data is persisited.   
     *
     * @throws SQLException if a database access error occurs.
     */
    public void close() throws SQLException {
        connection.close();
     }
}   
