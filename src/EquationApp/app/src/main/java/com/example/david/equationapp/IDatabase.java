package com.example.david.equationapp;

import com.example.david.equationapp.models.MyEquation;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by David on 10/21/2017.
 */

/**
 * this class is the template for how the database controller is supposed to work
 */
public interface IDatabase {
    /**
     * this method is supposed to insert the equation into the database
     * @param e the equation to add
     * @return boolean if it is succcessful
     */
    public boolean insert(MyEquation e);

    /**
     * this method updates the equation in the database with the matching name
     * with the new data in the argument passed
     * @param e the equation object that holds the new info, but same equation name
     * @return boolean if successful
     */
    public boolean updateByName(MyEquation e);

    /**
     * this mehtod deletes equations by their name from the database
     * @param e the equation object that holds the equation anme to be deleted
     * @return boolean if successful
     */
    public boolean deleteByName(MyEquation e);

    /**
     * this method is used to select all of the relevant equations
     * @return a map of the equations, the key is a string of the equation name
     */
    public AbstractMap<String,MyEquation> selectAll();

    /**
     * this method is used to add the evnet listener to the database
     */
    public void addValueEventListener();

    /**
     * this method is used to remove the event listener
     */
    public void removeValueEventListener();
}
