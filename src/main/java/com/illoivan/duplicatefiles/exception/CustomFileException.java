package com.illoivan.duplicatefiles.exception;


public class CustomFileException extends Exception{

	private static final long serialVersionUID = 1L;

	public CustomFileException() {
        super();
    }

    public CustomFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CustomFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomFileException(String message) {
        super(message);
    }

    public CustomFileException(Throwable cause) {
        super(cause);
    }

    
    
}
