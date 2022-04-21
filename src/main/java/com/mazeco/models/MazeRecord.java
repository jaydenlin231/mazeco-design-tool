package com.mazeco.models;

import java.util.Date;

public class MazeRecord {

    private String id;
    private String name;
    // add user class
    private String author;
    private Date dateCreated;
    private Date dateModified;
    private String[][] mazeModel;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public Date getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Date getDateModified() {
        return dateModified;
    }
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }
    public String[][] getMazeModel() {
        return mazeModel;
    }
    public void setMazeModel(String[][] mazeModel) {
        this.mazeModel = mazeModel;
    }

}
