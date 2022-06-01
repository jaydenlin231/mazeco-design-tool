package com.mazeco.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

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

    /**
     * Constructs a MazeRecord with the given {@code name}, {@code author}, and {@code mazeModel}.
     * @param name name of this {@code MazeRecord}
     * @param author author of this {@code MazeRecord}
     * @param mazeModel data and configuration of the maze 
     * 
     * @see MazeModel
     */
    public MazeRecord(String name, User author, MazeModel mazeModel) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.author = author;
        this.mazeModel = mazeModel;
        Instant instant = Instant.now();
        ZoneId zid = ZoneId.systemDefault();
        this.dateTimeCreated = instant.atZone(zid);
        this.dateTimeModified = instant.atZone(zid);
    }

    // Deserialise
    public MazeRecord(String id, String name, String author, ZonedDateTime dateTimeCreated, ZonedDateTime dateTimeModified, MazeModel mazeModel) {
        this.id = UUID.fromString(id);
        this.name = name;
        String[] authorNameSplit = author.split(" ");
        this.author = new User(authorNameSplit[0], authorNameSplit[1], "tba", "tba");

        this.mazeModel = mazeModel;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeModified = dateTimeModified;
        this.mazeModel = mazeModel;
    }

    public UUID getId() {
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
    public ZonedDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }
    public ZonedDateTime getDateTimeModified() {
        return dateTimeModified;
    }
    public void setDateModified(ZonedDateTime dateModified) {
        this.dateTimeModified = dateModified;
    }
    public MazeModel getMazeModel() {
        return mazeModel;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return name + " - " + author + "        " + "MazeCo#" + id.toString();
    }
}
