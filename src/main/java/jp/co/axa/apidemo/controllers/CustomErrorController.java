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

@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public ResponseEntity<Response<Void>> error(
            HttpServletRequest request, HttpServletResponse response) {

        if (response.getStatus() == 401) {
            return buildErrorResponse(request, "Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        if (response.getStatus() == 403) {
            return buildErrorResponse(request, "Forbidden", HttpStatus.FORBIDDEN);
        }

        String errorMessage = String.format(
                "No handler found [method=%s][path=%s]",
                request.getMethod(),
                request.getRequestURI());

        return buildErrorResponse(request, errorMessage, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Response<Void>> buildErrorResponse(HttpServletRequest request,
            String errorMessage, HttpStatus httpStatus) {

        LOGGER.error(errorMessage);

        return new ResponseEntity<>(
                new Response<>(LocalDateTime.now(), httpStatus, errorMessage),
                httpStatus);
    }
}
