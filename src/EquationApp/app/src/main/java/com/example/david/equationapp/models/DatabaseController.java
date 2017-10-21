package com.example.david.equationapp.models;

import com.example.david.equationapp.IDatabase;
import com.example.david.equationapp.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 * Created by David on 10/21/2017.
 */

public class DatabaseController implements IDatabase {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ArrayList<MyEquation> list;

    public DatabaseController(){
        mDatabase.child("equations").child(mAuth.getUid()).addValueEventListener(new DatabaseChange());
        list = new ArrayList<MyEquation>();
    }
    @Override
    public void insert(MyEquation e){
        mDatabase.child("equations").child(mAuth.getUid()).child(e.getName()).setValue(e);
    }

    @Override
    public void updateById(MyEquation e) {

    }

    @Override
    public void deleteById(int id) {

    }

    @Override
    public Collection<MyEquation> selectAll() {
        return list;
    }
    private class DatabaseChange implements ValueEventListener {
        String userID = mAuth.getUid();

        public void onDataChange(DataSnapshot dataSnapshot) {
            list = new ArrayList<MyEquation>();
            for (DataSnapshot equationDataSnapshot : dataSnapshot.getChildren()) {
                MyEquation equation = equationDataSnapshot.getValue(MyEquation.class);
                list.add(equation);
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }
}
