package com.mazeco.utilities;

public enum SortOrder {
    ASC("Ascending"),
    DSC("Descending");

    private final String label;

    private SortOrder(String label){
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}