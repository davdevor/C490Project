package com.example.david.equationapp.models;

/**
 * Created by David on 9/25/2017.
 */

public class MyEquation {
    private int id;
    private String name;
    private String description;
    private String course;
    private String equation;

    public MyEquation(int id,String name, String description, String course, String equation){
        this.id = id;
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
    public int getId(){
        return id;
    }
}
