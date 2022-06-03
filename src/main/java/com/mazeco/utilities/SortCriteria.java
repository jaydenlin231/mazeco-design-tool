package com.mazeco.utilities;

public enum SortCriteria {
    BY_AUTHOR("Author"), 
    BY_NAME("Name"), 
    BY_CREATED("Date Created"),
    BY_MODIFIED("Date Modified");

    private final String label;

    private SortCriteria(String label){
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}