package jp.co.axa.apidemo.controllers;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.axa.apidemo.model.Response;

/**
 * Custom error handling controller
 * Due to some conflicts with Swagger, I was not able to configure and intercept
 * NoHandlerFoundException in {@link CustomExceptionHandler}, this Custom
 * controller is a workaround for this. Also handles authentication and
 * authorization errors
 * 
 * @author Arthur Campos Costa
 */
@Controller
public class CustomErrorController implements ErrorController {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    /**
     * Endpoint for error handling. Spring redirects to this endpoint for any
     * unhandled error.
     * 
     * @param request  http servlet request
     * @param response http servlet response
     * @return ResponseEntity with custom error response
     */
    @RequestMapping("/error")
    public ResponseEntity<Response<Void>> error(
            HttpServletRequest request, HttpServletResponse response) {

        // Unauthorized
        if (response.getStatus() == 401) {
            return buildErrorResponse(request, "Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        // Forbidden
        if (response.getStatus() == 403) {
            return buildErrorResponse(request, "Forbidden", HttpStatus.FORBIDDEN);
        }

        // No handler found
        String errorMessage = String.format(
                "No handler found [method=%s][path=%s]",
                request.getMethod(),
                request.getRequestURI());

        return buildErrorResponse(request, errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Build custom error response
     * 
     * @param request      http servlet request
     * @param errorMessage error message
     * @param httpStatus   http status
     * @return ResponseEntity with custom error response
     */
    private ResponseEntity<Response<Void>> buildErrorResponse(HttpServletRequest request,
            String errorMessage, HttpStatus httpStatus) {

        LOGGER.error(errorMessage);

        return new ResponseEntity<>(
                new Response<>(LocalDateTime.now(), httpStatus, errorMessage),
                httpStatus);
    }
}
