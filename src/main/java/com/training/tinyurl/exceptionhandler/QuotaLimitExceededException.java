package com.training.tinyurl.exceptionhandler;

public class QuotaLimitExceededException extends Exception{
    public QuotaLimitExceededException(String message){
        super(message);
    }
}
