package com.example.david.equationapp.models;

/**
 * Created by David on 9/21/2017.
 */

public class myDouble {
    private double d;
    public myDouble(){
        d = 0.0;
    }
    public myDouble(double x){
        d = x;
    }
    public void set(double x){
        d = x;
    }
    public double get(){
        return d;
    }
    @Override
    public String toString(){
        return ""+d;
    }
}
