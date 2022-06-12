package com.mazeco.exception;


/***
 * An exception thrown when a particular maze configuration cannot be solved.
 */
public class UnsolvableMazeException extends Exception {
	private static final long serialVersionUID = 1L;
	public UnsolvableMazeException(String message) {
        super(message);
    }

    public UnsolvableMazeException (Throwable cause) {
        super (cause);
    }

    public UnsolvableMazeException (String message, Throwable cause) {
        super (message, cause);
    }
}