package com.ftn.sbnz.service.utils;
import com.ftn.sbnz.model.models.MessageResponse;
import com.ftn.sbnz.model.models.exceptions.CustomException;
import com.ftn.sbnz.model.models.exceptions.StorageException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value
            = CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody MessageResponse
    handleException(CustomException ex)
    {
        return new MessageResponse(ex.getMessage());
    }

    @ExceptionHandler(value
            = StorageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody MessageResponse
    handleException(StorageException ex)
    {
        return new MessageResponse(ex.getMessage());
    }

    @ExceptionHandler(value
            = ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody MessageResponse
    handleException(ExpiredJwtException ex)
    {
        return new MessageResponse("Token has expired. Login again!");
    }

}
