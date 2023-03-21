package jp.co.axa.apidemo.controllers;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.model.Response;

/**
 * Global exception handler for our controllers
 * 
 * @author Arthur Campos Costa
 */
@ControllerAdvice
public class CustomExceptionHandler {

        /** logger */
        private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

        /**
         * ResourceNotFoundException handler.
         * This exception is thrown when an employee was not found
         * 
         * @param ex      ResourceNotFoundException
         * @param request http servlet request
         * @return ResponseEntity with custom response and status 404
         */
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Response<Void>> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.NOT_FOUND, "Resource not found");
        }

        /**
         * MethodArgumentNotValidException handler.
         * This exception is thrown when Employee body is invalid.
         * 
         * @param ex      MethodArgumentNotValidException
         * @param request http servlet request
         * @return ResponseEntity with custom response and status 400
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Response<Void>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                String fieldErrorMessages = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                // for some reason, null salary error is duplicated so we use distinct here
                                .distinct()
                                .map(err -> String.format("%s %s", err.getField(), err.getDefaultMessage()))
                                .collect(Collectors.joining(","));

                return buildErrorResponse(
                                ex, request, HttpStatus.BAD_REQUEST, fieldErrorMessages);
        }

        /**
         * MethodArgumentTypeMismatchException and IllegalArgumentException handler.
         * These exceptions are thrown when query or path parameters are invalid
         * 
         * @param ex      MethodArgumentTypeMismatchException or
         *                IllegalArgumentException
         * @param request http servlet request
         * @return ResponseEntity with custom response and status 400
         */
        @ExceptionHandler({
                        MethodArgumentTypeMismatchException.class,
                        IllegalArgumentException.class
        })
        public ResponseEntity<Response<Void>> handleMethodArgumentTypeMismatchException(
                        RuntimeException ex, HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.BAD_REQUEST, "Parameter conversion error");
        }

        /**
         * Handler for any other unexpected Exception.
         * 
         * @param ex      Exception
         * @param request http servlet request
         * @return @return ResponseEntity with custom response and status 500
         */
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Response<Void>> handleException(Exception ex,
                        HttpServletRequest request) {

                return buildErrorResponse(
                                ex, request, HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server error");
        }

        /**
         * Build custom error response
         * 
         * @param ex            exception
         * @param request       http servlet request
         * @param httpStatus    http status
         * @param customMessage custom error message
         * @return ResponseEntity with custom response
         */
        private ResponseEntity<Response<Void>> buildErrorResponse(Exception ex,
                        HttpServletRequest request, HttpStatus httpStatus, String customMessage) {
                String errorMessage = String.format(
                                "%s [method=%s][path=%s] : %s",
                                customMessage,
                                request.getMethod(),
                                request.getRequestURI(),
                                ex.getMessage());

                LOGGER.error(errorMessage, ex);

                return new ResponseEntity<>(
                                new Response<>(LocalDateTime.now(), httpStatus, errorMessage),
                                httpStatus);
        }
}
