package jp.co.axa.apidemo.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Immutable generic custom controller response model.
 * This is the object being returned as body for every response.
 * Having a consistant data structure throughout all your endpoint response
 * makes it easier to use for clients in my experience.
 * 
 * @param <T> response type of data
 */
@Getter
public class Response<T> {
    /** timestamp of the request processing */
    private final LocalDateTime timestamp;

    /** response status code */
    private final int statusCode;

    /** response status */
    private final String statusMessage;

    /** error message, exclusive with data */
    private final String errorMessage;

    /**
     * output data, exclusive with errorMessage
     * 
     * The key is the JSON field containing the queried response, and the value is
     * the response itself
     * Possible to return multiple keys though it is not currently used in the
     * application
     * 
     * Example : "employee": {// data}
     * "employees": [// list of data]
     */
    private final Map<String, T> data;

    /**
     * Default constructor for Integration test deserialization
     */
    public Response() {
        this.timestamp = null;
        this.statusCode = 0;
        this.statusMessage = null;
        this.errorMessage = null;
        this.data = null;
    }

    /**
     * Response OK constructor
     * 
     * @param timestamp timestamp
     * @param status    http status
     * @param data      data
     */
    public Response(LocalDateTime timestamp, HttpStatus status, Map<String, T> data) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = null;
        this.data = data;
    }

    /**
     * Response NG constructor
     * 
     * @param timestamp    timestamp
     * @param status       status
     * @param errorMessage error message
     */
    public Response(LocalDateTime timestamp, HttpStatus status, String errorMessage) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = errorMessage;
        this.data = null;
    }

    /**
     * No content Response
     * 
     * @param timestamp timestamp
     * @param status    status
     */
    public Response(LocalDateTime timestamp, HttpStatus status) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = null;
        this.data = null;
    }
}
