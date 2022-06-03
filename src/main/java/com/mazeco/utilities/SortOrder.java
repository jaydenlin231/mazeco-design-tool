package com.mazeco.utilities;

public enum SortOrder {
    ASC("Ascending"),
    DSC("Descending");

    public final String label;

    private SortOrder(String label){
        this.label = label;
    }
}