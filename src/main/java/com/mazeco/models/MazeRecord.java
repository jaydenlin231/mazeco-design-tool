package com.mazeco.models;

import java.util.Date;
/**
 *  Stores the record and the metadata of a successfully created {@code MazeModel}.
 * 
 *  @see MazeModel
 */
public class MazeRecord {
    private String id;
    private String name;
    private User author;
    private Date dateCreated;
    private Date dateModified;
    private MazeModel mazeModel;

    /**
     * Constructs a MazeRecord with the given {@code name}, {@code author}, and {@code mazeModel}.
     * @param name name of this {@code MazeRecord}
     * @param author author of this {@code MazeRecord}
     * @param mazeModel data and configuration of the maze 
     * 
     * @see MazeModel
     */
    public MazeRecord(String name, User author, MazeModel mazeModel) {
        this.name = name;
        this.author = author;
        this.mazeModel = mazeModel;
        this.dateCreated = new Date();
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public User getAuthor() {
        return author;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public Date getDateModified() {
        return dateModified;
    }
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
    public MazeModel getMazeModel() {
        return mazeModel;
    }
}
