package jp.co.axa.apidemo.model;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;

import lombok.Getter;

/**
 * Immutable generic controller response
 * 
 * @param <T> response type of data
 */
@Getter
public class Response<T> {
    private final LocalDateTime timestamp;
    private final int statusCode;
    private final String statusMessage;
    private final String errorMessage;
    private final Map<String, T> data;

    /**
     * Response OK constructor
     * 
     * @param timestamp
     * @param status
     * @param data
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
     * @param timestamp
     * @param status
     * @param errorMessage
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
     * @param timestamp
     * @param status
     */
    public Response(LocalDateTime timestamp, HttpStatus status) {
        this.timestamp = timestamp;
        this.statusCode = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = null;
        this.data = null;
    }
}
