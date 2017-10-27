package com.example.david.equationapp;

import com.example.david.equationapp.models.MyEquation;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by David on 10/21/2017.
 */

public interface IDatabase {

    public boolean insert(MyEquation e);
    public boolean updateByName(MyEquation e);
    public boolean deleteByName(MyEquation e);
    public AbstractMap<String,MyEquation> selectAll();
}
