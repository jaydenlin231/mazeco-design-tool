package com.mazeco.utilities;

public enum CanvasMode {
    GENERATE("Generate"),
    DRAW("Draw"),
    EDIT("Edit");

    private final String label;

    private CanvasMode(String label){
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
