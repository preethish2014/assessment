package com.localstack.exception.handler;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.localstack.constants.Constants;
import com.localstack.exception.ListenerException;
import com.localstack.exception.PolicyException;
import com.localstack.exception.SNSException;
import com.localstack.exception.SQSException;
import com.localstack.model.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {


    @Autowired
    ObjectMapper objectMapper;



    @org.springframework.web.bind.annotation.ExceptionHandler(SQSException.class)
    public @ResponseBody ResponseEntity<String> handleSQSException(SQSException ex, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), request.getDescription(false), Constants.SQS_ERROR_CODE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SNSException.class)
    public @ResponseBody ResponseEntity<String> handleSNSException(SNSException ex, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), request.getDescription(false), Constants.SQS_ERROR_CODE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.NOT_FOUND);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public @ResponseBody ResponseEntity<String> handleGenericException(Exception ex, WebRequest request) throws JsonProcessingException {

        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), ex.getMessage(), "400");
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = org.springframework.http.HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException methodArgumentNotValidException, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Fields are not valid", "400");
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(PolicyException.class)
    public @ResponseBody ResponseEntity<String> handlePolicyException(PolicyException ex, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "UnAuthorized", "401");
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.UNAUTHORIZED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ListenerException.class)
    public @ResponseBody ResponseEntity<String> handleListenerException(ListenerException ex, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "Listener Error", "500");
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public @ResponseBody ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, WebRequest request) throws JsonProcessingException {
        ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), "UnAuthorized", "401");
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return new ResponseEntity<>(objectMapper.writeValueAsString(errorResponse),setContentType(), HttpStatus.UNAUTHORIZED);
    }

    private HttpHeaders setContentType(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
