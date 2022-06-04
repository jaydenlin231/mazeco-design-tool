package com.mazeco.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import javax.swing.ImageIcon;

import java.awt.Component;
import java.awt.image.BufferedImage;

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

    // Deserialise
    public MazeRecord(String id, String name, String author, ZonedDateTime dateTimeCreated, ZonedDateTime dateTimeModified, MazeModel mazeModel, ImageIcon cleanImage, ImageIcon solvedImage) {
        this.id = UUID.fromString(id);
        this.name = name;
        String[] authorNameSplit = author.split(" ");
        this.author = new User(authorNameSplit[0], authorNameSplit[1], "tba", "tba");

        this.mazeModel = mazeModel;
        this.dateTimeCreated = dateTimeCreated;
        this.dateTimeModified = dateTimeModified;
        this.mazeModel = mazeModel;
        this.cleanImage = cleanImage;
        this.solvedImage = solvedImage;
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

    public String getDateTimeCreatedString(String formatPattern){
        return dateTimeCreated.toLocalDateTime()
               .format(DateTimeFormatter.ofPattern(formatPattern));
    }

    public String getDateTimeModifiedString(String formatPattern){
        return dateTimeModified.toLocalDateTime()
               .format(DateTimeFormatter.ofPattern(formatPattern));
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
    public ImageIcon getCleanImage() {
        return cleanImage;
    }

    public ImageIcon getSolvedImage() {
        return solvedImage;
    }
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
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
