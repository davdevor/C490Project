package com.example.david.equationapp.models;

/**
 * Created by David on 9/25/2017.
 */

public class MyEquation {
    private String name;
    private String description;
    private String course;
    private String equation;

    public MyEquation(){

    }

    public MyEquation(String name, String description, String course, String equation){
        this.name = name;
        this.description = description;
        this.course = course;
        this.equation = equation;
    }

    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getCourse(){
        return course;
    }
    public String getEquation(){
        return equation;
    }
}
