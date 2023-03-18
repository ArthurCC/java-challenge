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

import jp.co.axa.apidemo.exceptions.ResourceNotFoundException;
import jp.co.axa.apidemo.model.Response;

@ControllerAdvice
public class CustomExceptionHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(CustomExceptionHandler.class);

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<Response<Void>> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                String errorMessage = String.format(
                                "Resource not found [method=%s][path=%s] : %s",
                                request.getMethod(),
                                request.getRequestURI(),
                                ex.getMessage());

                LOGGER.error(errorMessage, ex);

                return new ResponseEntity<>(
                                new Response<>(LocalDateTime.now(), HttpStatus.NOT_FOUND, errorMessage),
                                HttpStatus.NOT_FOUND);
        }

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

                String errorMessage = String.format(
                                "Invalid parameters [method=%s][path=%s] : %s",
                                request.getMethod(),
                                request.getRequestURI(),
                                fieldErrorMessages);

                LOGGER.error(errorMessage, ex);

                return new ResponseEntity<>(
                                new Response<>(LocalDateTime.now(), HttpStatus.BAD_REQUEST, errorMessage),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(NumberFormatException.class)
        public ResponseEntity<Response<Void>> handleNumberFormatException(NumberFormatException ex,
                        HttpServletRequest request) {
                String errorMessage = String.format(
                                "Path variable is not a number [method=%s][path=%s] : %s",
                                request.getMethod(),
                                request.getRequestURI(),
                                ex.getMessage());

                LOGGER.error(errorMessage, ex);

                return new ResponseEntity<>(
                                new Response<>(LocalDateTime.now(), HttpStatus.BAD_REQUEST, errorMessage),
                                HttpStatus.BAD_REQUEST);
        }
}
