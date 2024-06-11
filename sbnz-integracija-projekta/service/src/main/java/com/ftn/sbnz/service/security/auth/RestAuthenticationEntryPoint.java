package com.ftn.sbnz.service.security.auth;

import com.ftn.sbnz.model.models.exceptions.PasswordMismatchException;
import com.ftn.sbnz.model.models.exceptions.StorageException;
import com.ftn.sbnz.model.models.exceptions.UserAlreadyExistsException;
import com.ftn.sbnz.model.models.exceptions.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Throwable cause = authException.getCause();
        if (cause instanceof ExpiredJwtException) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Token has expired. Login again!");
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        // 403
        setResponseError(response, HttpServletResponse.SC_FORBIDDEN, String.format("Message: %s", accessDeniedException.getMessage()));
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, BadCredentialsException badCredentialsException) throws IOException {
        // 403
        setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, String.format(" Message: %s", badCredentialsException.getMessage()));
    }

    @ExceptionHandler(value = {StorageException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, StorageException storageException) throws IOException {
        setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, String.format(" Message: %s", storageException.getMessage()));
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, UserNotFoundException userNotFoundException) throws IOException {
        setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, String.format(" Message: %s", userNotFoundException.getMessage()));
    }

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, UserAlreadyExistsException userAlreadyExistsException) throws IOException {
        setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, String.format(" Message: %s", userAlreadyExistsException.getMessage()));
    }

    @ExceptionHandler(value = {PasswordMismatchException.class})
    public void commence(HttpServletRequest request, HttpServletResponse response, PasswordMismatchException passwordMismatchException) throws IOException {
        setResponseError(response, HttpServletResponse.SC_BAD_REQUEST, String.format(" Message: %s", passwordMismatchException.getMessage()));
    }

    private void setResponseError(HttpServletResponse response, int errorCode, String errorMessage) throws IOException{
        response.setStatus(errorCode);
        response.getWriter().write(errorMessage);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
