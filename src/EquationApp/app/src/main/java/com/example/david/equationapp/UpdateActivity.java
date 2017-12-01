package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.david.equationapp.models.DatabaseController;
import com.example.david.equationapp.models.MyEquation;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */

public class UpdateActivity extends AppCompatActivity {
    private IDatabase db = MainActivity.getDB();
    private AbstractMap<String,MyEquation> equations = db.selectAll();
    private LinearLayout ll;
    private String currentEquation;
    private final String BUNDLE_STRING_EQUATION = "currentequation";
    private final String BUNDLE_STRING_NEW_EQUATION = "newEquation";
    private final String BUNDLE_STRING_DESCRIPTION ="description";
    private final String BUNDLE_STRING_COURSE = "course";
    private ScrollView sv;

    /**
     * this methos calls createview if bundle is null
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if savedInstanceState is null that means you are sill on the screen with list of equations
        //instead of on screen where you are editing the equation
        if(savedInstanceState==null){
            createView();
        }

    }

    /**
     * this method is used to add the eventlistener on the database
     */
    @Override
    protected void onResume(){
        super.onResume();
        db.addValueEventListener();
    }

    /**
     * this method is used to remove the eventlistener on the database
     */
    @Override
    protected void onPause(){
        db.removeValueEventListener();
        super.onPause();
    }

    /**
     * this method is used to save the data on the editing screen
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        try{
            outState.putString(BUNDLE_STRING_EQUATION,currentEquation);
            outState.putString(BUNDLE_STRING_DESCRIPTION,((EditText)findViewById(R.id.updateInputDescription)).getText().toString());
            outState.putString(BUNDLE_STRING_COURSE,((EditText)findViewById(R.id.updateInputCourse)).getText().toString());
            outState.putString(BUNDLE_STRING_NEW_EQUATION,((EditText)findViewById(R.id.updateInputEquation)).getText().toString());
        }
        catch (NullPointerException e){
            //if nullpointer happened the app wasn't on the editing screen so do nothing
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * this method is used to restore the data on the editing screen
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentEquation = savedInstanceState.getString(BUNDLE_STRING_EQUATION);
        if(currentEquation!=null){
            MyEquation e = equations.get(currentEquation);
            setContentView(R.layout.activity_update);
            TextView nameET = findViewById(R.id.updateInputName);
            EditText descriptionET = findViewById(R.id.updateInputDescription);
            EditText courseET = findViewById(R.id.updateInputCourse);
            EditText equationET = findViewById(R.id.updateInputEquation);
            nameET.setText(e.getName());
            descriptionET.setText(savedInstanceState.getString(BUNDLE_STRING_DESCRIPTION));
            courseET.setText(savedInstanceState.getString(BUNDLE_STRING_COURSE));
            equationET.setText(savedInstanceState.getString(BUNDLE_STRING_NEW_EQUATION));
        }
        else{
            createView();
        }
    }

    /**
     * this method is used to build the list view of all the equations that the user can edit
     */
    public void createView() {
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        sv.addView(ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        Log.d("Look",equations.toString());
        Button button;
        ButtonHandler bh = new ButtonHandler();
        for (Map.Entry<String,MyEquation> x:equations.entrySet()) {
            button = new Button(this);
            String name = x.getValue().getName();
            button.setText(name);
            button.setOnClickListener(bh);
            button.setAllCaps(false);
            ll.addView(button);
        }
        setContentView(sv);
    }

    /**
     * this method is used to handle updating the database with the new information
     * @param v
     */
    public void update(View v){
        EditText descriptionET = findViewById(R.id.updateInputDescription);
        EditText courseET = findViewById(R.id.updateInputCourse);
        EditText equationET = findViewById(R.id.updateInputEquation);
        MyEquation equ = new MyEquation(currentEquation,
                descriptionET.getText().toString(),
                courseET.getText().toString(),
                equationET.getText().toString());
        db.updateByName(equ);
        createView();
    }

    /**
     * this method handles deleting equation from the database
     * @param v
     */
    public void delete(View v){
        //delete from database
        db.deleteByName(equations.get(currentEquation));
        //you have to delete from the current list of equations
        //becuase the view gets brought up before the database controller updates the list for you
        equations.remove(currentEquation);
        createView();

    }

    /**
     * this class handles the button clicks in the listview
     * when a button is clicked it changes the view to the editing screen
     */
    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            Button button = (Button) v;
            currentEquation = button.getText().toString();
            MyEquation e = equations.get(currentEquation);
            if(e != null) {
                setContentView(R.layout.activity_update);
                TextView nameTV = findViewById(R.id.updateInputName);
                EditText descriptionET = findViewById(R.id.updateInputDescription);
                EditText courseET = findViewById(R.id.updateInputCourse);
                EditText equationET = findViewById(R.id.updateInputEquation);
                nameTV.setText(e.getName());
                descriptionET.setText(e.getDescription());
                courseET.setText(e.getCourse());
                equationET.setText(e.getEquation());
            }
            else{
                createView();
            }
        }
    }
}