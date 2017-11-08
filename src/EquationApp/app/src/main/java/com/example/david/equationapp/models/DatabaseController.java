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
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    protected boolean notdeleted;
    private HashMap<String,MyEquation> list;
    private final String CHILD_EQUATION = "equations";
    private String CHILD_USER = FirebaseAuth.getInstance().getUid();;
    private ValueEventListener eventListener;

    public DatabaseController(){
        eventListener = new DatabaseChange();
        list = new HashMap<String,MyEquation>();
    }

    @Override
    public void addValueEventListener(){
        mDatabase.child(CHILD_EQUATION).addValueEventListener(eventListener);
    }

    @Override
    public void removeValueEventListener(){
        mDatabase.child(CHILD_EQUATION).removeEventListener(eventListener);
    }

    @Override
    public boolean insert(MyEquation e){
            mDatabase.child(CHILD_EQUATION).child(CHILD_USER).child(e.getName()).setValue(e);
            return true;
    }

    @Override
    public boolean updateByName(MyEquation e) {
        mDatabase.child(CHILD_EQUATION).child(CHILD_USER).child(e.getName()).setValue(e);
        return true;

    }

    @Override
    public boolean deleteByName(MyEquation e) {
        mDatabase.child(CHILD_EQUATION).child(CHILD_USER).child(e.getName()).removeValue();
        return true;
    }
    @Override
    public HashMap<String,MyEquation> selectAll() {
        return list;
    }
    private class DatabaseChange implements ValueEventListener {
        public void onDataChange(DataSnapshot dataSnapshot) {
            list.clear();
            for (DataSnapshot equationDataSnapshot : dataSnapshot.child(CHILD_USER).getChildren()) {
                MyEquation equation = equationDataSnapshot.getValue(MyEquation.class);
                list.put(equation.getName(),equation);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
