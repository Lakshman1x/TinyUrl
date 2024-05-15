package com.training.tinyurl.exceptionhandler;

public class TooManyCollisionsException extends Exception{
    public TooManyCollisionsException(String message){
        super(message);
    }
}
