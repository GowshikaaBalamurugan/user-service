package com.dkart.userservice.advice;

import com.dkart.userservice.dto.CustomErrorResponse;
import com.dkart.userservice.exceptions.UnAuthorizedUserException;
import com.dkart.userservice.exceptions.UserAlreadyExistsException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class UserServiceGlobalExceptionHandler {


    private final static String USER_ALREADY_EXISTS="USER-SERVICE:USER_ALREADY_EXISTS:409";
    private final static String UNAUTH_USER="USER-SERVICE:UNAUTHORIZED_USER:403";
    private final static String BAD_REQUEST_EX="USER-SERVICE:BAD_REQUEST:400";
    private final static String CONSTRAINT_VIOLATION_EX="USER-SERVICE:CONSTRAINT_VIOLATION:400";
    private final static String METHOD_ARG_NOT_VALID_EX="USER-SERVICE:METHOD_ARG_NOT_VALID:400";
    private final static String ILLEGAL_BASE64="USER-SERVICE:ILLEGAL_BASE64:400";






    @ExceptionHandler(UnAuthorizedUserException.class)
    public ResponseEntity<?> handleUnAuthorizedUserException(UnAuthorizedUserException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN)
                .errorCode(UNAUTH_USER)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("UserServiceGlobalExceptionHandler::handleUnAuthorizedUserException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.CONFLICT)
                .errorCode(USER_ALREADY_EXISTS)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("UserServiceGlobalExceptionHandler::handleUserAlreadyExistsException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(BAD_REQUEST_EX)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("UserServiceGlobalExceptionHandler::handleBadRequestException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(CONSTRAINT_VIOLATION_EX)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("UserServiceGlobalExceptionHandler::handleConstraintViolationException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        String err=ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(","));
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(METHOD_ARG_NOT_VALID_EX)
                .errorMessage(err)
                .build();
        log.error("UserServiceGlobalExceptionHandler::handleMethodArgumentNotValidException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DecodingException.class)
    public ResponseEntity<?> handleDecodingException(DecodingException ex){
        CustomErrorResponse errorResponse= CustomErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .errorCode(ILLEGAL_BASE64)
                .errorMessage(ex.getMessage())
                .build()  ;
        log.error("UserServiceGlobalExceptionHandler::handleDecodingException exception caught {}",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
