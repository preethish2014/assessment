package com.localstack.exception;

public class SQSException extends Exception{

    public SQSException(String message)
    {
        super(message);
    }
}
