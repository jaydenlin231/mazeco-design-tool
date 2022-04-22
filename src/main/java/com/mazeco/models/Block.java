package com.mazeco.models;

import javax.swing.*;

public class Block {
    private BlockType type;

    public Block(){
        this.type = BlockType.BLANK;
    }

    public Block(BlockType type){
        this.type = type;
    }

    public BlockType getType() {
        return type;
    }

    public void setType(BlockType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        switch (type) {
            case WALL:
                return "W";

            case BLANK:
                return "B";

            case START:
                return "S";

            case END:
                return "E";
            
            case LOGO:
                return "L";
        
            default:
                return "";
        }
    }

    
}
