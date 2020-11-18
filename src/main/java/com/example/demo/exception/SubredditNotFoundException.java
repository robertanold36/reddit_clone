package com.example.demo.exception;

public class SubredditNotFoundException extends RuntimeException{

    public SubredditNotFoundException(String message){
        super(message);
    }
}
