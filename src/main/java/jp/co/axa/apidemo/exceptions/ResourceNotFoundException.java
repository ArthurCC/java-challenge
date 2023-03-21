package jp.co.axa.apidemo.exceptions;

/**
 * Custom exception when an employee was not found
 * 
 * @author Arthur Campos Costa
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructor
     * 
     * @param msg error message
     */
    public ResourceNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Constructor
     * 
     * @param msg   error message
     * @param cause cause
     */
    public ResourceNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
