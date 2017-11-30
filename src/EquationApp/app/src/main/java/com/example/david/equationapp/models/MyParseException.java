package com.example.david.equationapp.models;

/**
 * Created by David on 11/28/2017.
 */

/**
 * this class is a exception i made to throw a parse exception in the PostfixCalculator
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
