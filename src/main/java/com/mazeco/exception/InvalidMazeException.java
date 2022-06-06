package com.mazeco.exception;

public class InvalidMazeException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public InvalidMazeException(String message) {
        super(message);
    }

    public InvalidMazeException (Throwable cause) {
        super (cause);
    }

    public InvalidMazeException (String message, Throwable cause) {
        super (message, cause);
    }
}