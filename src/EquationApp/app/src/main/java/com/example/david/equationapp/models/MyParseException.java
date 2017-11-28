package com.example.david.equationapp.models;

/**
 * Created by David on 11/28/2017.
 */

public class MyParseException extends Exception{
    private String message;
    public MyParseException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
