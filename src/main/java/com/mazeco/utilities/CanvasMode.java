package com.mazeco.utilities;

import com.mazeco.userinterface.MazeCanvas;
import com.mazeco.userinterface.OptionsMenu;

/**
 * Enumerate to specify the mode of certain menus such as OptionsMenu and CanvasMenu
 *
 * @see OptionsMenu
 * @see MazeCanvas
 */
public enum CanvasMode {
    GENERATE("Generate"),
    DRAW("Draw"),
    EDIT("Edit");

    private final String label;

    /**
     * Default constructor to set the mode
     *
     * @param label Mode of the menu
     */
    private CanvasMode(String label) {
        this.label = label;
    }

    /**
     * @return Returns the current mode in string
     */
    @Override
    public String toString() {
        return label;
    }
}
