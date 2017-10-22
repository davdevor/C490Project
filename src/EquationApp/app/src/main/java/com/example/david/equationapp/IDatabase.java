package com.example.david.equationapp;

import com.example.david.equationapp.models.MyEquation;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by David on 10/21/2017.
 */

public interface IDatabase {

    public void insert(MyEquation e);
    public void updateByName(MyEquation e);
    public void deleteByName(MyEquation e);
    public AbstractMap<String,MyEquation> selectAll();
}
