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

    @Override
    public void removeValueEventListener(){
        mDatabase.removeEventListener(eventListener);
    }

    @Override
    public boolean insert(MyEquation e){
            mDatabase.child(e.getName()).setValue(e);
            return true;
    }

    @Override
    public boolean updateByName(MyEquation e) {
        mDatabase.child(e.getName()).setValue(e);
        return true;

    }

    @Override
    public boolean deleteByName(MyEquation e) {
        mDatabase.child(e.getName()).removeValue();
        return true;
    }
    @Override
    public HashMap<String,MyEquation> selectAll() {
        return list;
    }
    private class DatabaseChange implements ValueEventListener {
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
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
