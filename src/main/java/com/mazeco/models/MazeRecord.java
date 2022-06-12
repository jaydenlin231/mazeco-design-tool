package com.mazeco.models;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import javax.swing.ImageIcon;

/**
 *  Stores the record and the metadata of a successfully created {@code MazeModel}.
 * 
 *  @see MazeModel
 */
public class MazeRecord {
    private static final long serialVersionUID = -102877324682862507L;

    private UUID id;
    private String name;
    private User author;
    private ZonedDateTime dateTimeCreated;
    private ZonedDateTime dateTimeModified;
    private MazeModel mazeModel;
    private ImageIcon cleanImage;
    private ImageIcon solvedImage;

    /**
     * Constructs a MazeRecord with the given {@code name}, {@code author}, and {@code mazeModel}.
     * @param name name of this {@code MazeRecord}
     * @param author author of this {@code MazeRecord}
     * @param mazeModel data and configuration of the maze 
     * @param cleanImage the {@code ImageIcon} representation of the unsolved {@code MazeModel}
     * @param solvedImage the {@code ImageIcon} representation of the solved {@code MazeModel}
     * 
     * @see MazeModel
     */
    public MazeRecord(String name, User author, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.mazeModel = mazeModel;
        Instant instant = Instant.now();
        ZoneId zid = ZoneId.systemDefault();
        this.dateTimeCreated = instant.atZone(zid);
        this.dateTimeModified = instant.atZone(zid);
        this.cleanImage = cleanImage;
        this.solvedImage = solvedImage;
    }

    /**
     * Constructor used for data retrieval from database.
     * 
     * @param id the unique identifier of the {@code MazeRecord}
     * @param name name of the {@code MazeRecord}
     * @param author author of the {@code MazeRecord}
     * @param dateTimeCreated date and time the {@code MazeRecord} was created
     * @param dateTimeModified date and time the {@code MazeRecord} was last modified
     * @param mazeModel data and configuration of the maze 
     * @param cleanImage the updated {@code ImageIcon} representation of the unsolved {@code MazeModel}
     * @param solvedImage the updated {@code ImageIcon} representation of the solved {@code MazeModel}
     * 
     * @see JDBCMazeBrowserDataSource#retrieveMazeRecord
     */
    public MazeRecord(String id, String name, String author, ZonedDateTime dateTimeCreated, ZonedDateTime dateTimeModified, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) {
        this.id = UUID.fromString(id);
        this.name = name;
        String[] authorNameSplit = author.split(" ");
        this.author = new User(authorNameSplit[0], authorNameSplit[1]);

        this.mazeModel = mazeModel;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeModified = dateTimeModified;
        this.mazeModel = mazeModel;
        this.cleanImage = cleanImage;
        this.solvedImage = solvedImage;
    }

    /**
     * Gets the ID of the {@code MazeRecord}.
     * 
     * @return the {@code UUID} Object presenting the unique identification.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Gets the name of the {@code MazeRecord}.
     * 
     * @return the name of the maze.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the {@code MazeRecord}.
     * 
     * @param name the name of the maze.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the author of the {@code MazeRecord}.
     * 
     * @return the author {@code User} Object of the {@code MazeRecord}.
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Gets the the date and time the {@code MazeRecord} was created.
     * 
     * @return {@code ZonedDateTime} Object representing the date and time the {@code MazeRecord} was created.
     */
    public ZonedDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    /**
     * Gets the the date and time the {@code MazeRecord} was last modified.
     * 
     * @return {@code ZonedDateTime} Object representing the date and time the {@code MazeRecord} was last modified.
     */
    public ZonedDateTime getDateTimeModified() {
        return dateTimeModified;
    }
    /**
     * 
     * @param dateModified
     */
    public void setDateModified(ZonedDateTime dateModified) {
        this.dateTimeModified = dateModified;
    }
    /**
     * Gets the the String representation of the date and time the {@code MazeRecord} was created.
     * 
     * @param formatPattern {@code DateTime} formatting pattern String.
     * @return String representing the date and time the {@code MazeRecord} was created.
     */
    public String getDateTimeCreatedString(String formatPattern){
        return dateTimeCreated.toLocalDateTime()
               .format(DateTimeFormatter.ofPattern(formatPattern));
    }

    /**
     * Gets the the String representation of the date and time the {@code MazeRecord} was last modified.
     * 
     * @param formatPattern {@code DateTime} formatting pattern String.
     * @return String representing the date and time the {@code MazeRecord} was last modified.
     */
    public String getDateTimeModifiedString(String formatPattern){
        return dateTimeModified.toLocalDateTime()
               .format(DateTimeFormatter.ofPattern(formatPattern));
    }

   
    /**
     * Get the {@code MazeModel} Object of the {@code MazeRecord}.
     * 
     * @return {@code MazeModel} Object of the {@code MazeRecord}.
     */
    public MazeModel getMazeModel() {
        return mazeModel;
    }


    /**
     * Get the {@code ImageIcon} Object representation of the {@code MazeModel} in its unsolved state.
     * 
     * @return {@code ImageIcon} Object representation of the {@code MazeModel} in its unsolved state.
     */
    public ImageIcon getCleanImage() {
        return cleanImage;
    }

     /**
     * Get the {@code ImageIcon} Object representation of the {@code MazeModel} in its solved state.
     * 
     * @return {@code ImageIcon} Object representation of the {@code MazeModel} in its solved state.
     */
    public ImageIcon getSolvedImage() {
        return solvedImage;
    }

    
    @Override
    public boolean equals(Object obj) {
        return id.equals(((MazeRecord) obj).getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name + " - " + author;
    }
}
