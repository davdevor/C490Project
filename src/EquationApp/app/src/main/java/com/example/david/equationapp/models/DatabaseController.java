package com.example.david.equationapp.models;

import com.example.david.equationapp.IDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

/**
 * Created by David on 10/21/2017.
 */

public class DatabaseController implements IDatabase {

    protected boolean notdeleted;
    private HashMap<String,MyEquation> list;
    private final String CHILD_EQUATION = "equations";
    private String CHILD_USER = FirebaseAuth.getInstance().getUid();;
    private ValueEventListener eventListener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child(CHILD_EQUATION).child(CHILD_USER);

    /**
     * this constructor initalizes the eventlistener object for the database, it doesn't add it
     * and it intializes the HashMap the the equations are stored in
     */
    public DatabaseController(){
        eventListener = new DatabaseChange();
        list = new HashMap<String,MyEquation>();
    }

    /**
     * adds the event listener at the level in the database where the equations are relevent to
     * the signed in user
     */
    @Override
    public void addValueEventListener(){
        mDatabase.addValueEventListener(eventListener);
    }

    /**
     * removes the event listener
     */
    @Override
    public void removeValueEventListener(){
        mDatabase.removeEventListener(eventListener);
    }

    /**
     * this method is supposed to insert the equation into the database
     * @param e the equation to add
     * @return boolean if it is succcessful
     */
    @Override
    public boolean insert(MyEquation e){
            mDatabase.child(e.getName()).setValue(e);
            return true;
    }

    /**
     * this method updates the equation in the database with the matching name
     * with the new data in the argument passed
     * @param e the equation object that holds the new info, but same equation name
     * @return boolean if successful
     */
    @Override
    public boolean updateByName(MyEquation e) {
        mDatabase.child(e.getName()).setValue(e);
        return true;

    }

    /**
     * this mehtod deletes equations by their name from the database
     * @param e the equation object that holds the equation anme to be deleted
     * @return boolean if successful
     */
    @Override
    public boolean deleteByName(MyEquation e) {
        mDatabase.child(e.getName()).removeValue();
        return true;
    }

    /**
     * this method is used to select all of the relevant equations
     * @return a map of the equations, the key is a string of the equation name
     */
    @Override
    public HashMap<String,MyEquation> selectAll() {
        //list is filled when the database controller is initialized and the event listener is added
        //because when you add the event listener it acts like data has benn changed so it calls the on
        //data change method in the DatabaseChange class
        return list;
    }

    /**
     * this class handles implements the ValueEventListener for the database
     */
    private class DatabaseChange implements ValueEventListener {
        /**
         * this method gets called anytime a data change happens on the level of the database the event
         * listener is attached to
         * @param dataSnapshot
         */
        public void onDataChange(DataSnapshot dataSnapshot) {
            //clear the list becuase the data in the list could have been changed
            list.clear();

            //for eachloop gets the equations from the datasnap shot and puts them into the list
            for (DataSnapshot equationDataSnapshot : dataSnapshot.getChildren()) {
                MyEquation equation = equationDataSnapshot.getValue(MyEquation.class);
                list.put(equation.getName(),equation);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
