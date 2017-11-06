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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by David on 9/25/2017.
 */

public class UpdateActivity extends AppCompatActivity {
    private DatabaseController db = MainActivity.getDB();
    private HashMap<String,MyEquation> equations = db.selectAll();
    private LinearLayout ll;
    private String currentEquation;
    private final String BUNDLE_STRING_EQUATION = "currentequation";
    private final String BUNDLE_STRING_NEW_EQUATION = "newEquation";
    private final String BUNDLE_STRING_DESCRIPTION ="description";
    private final String BUNDLE_STRING_COURSE = "course";
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState==null){
            createView();
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        try{
            outState.putString(BUNDLE_STRING_EQUATION,currentEquation);
            outState.putString(BUNDLE_STRING_DESCRIPTION,((EditText)findViewById(R.id.updateInputDescription)).getText().toString());
            outState.putString(BUNDLE_STRING_COURSE,((EditText)findViewById(R.id.updateInputCourse)).getText().toString());
            outState.putString(BUNDLE_STRING_NEW_EQUATION,((EditText)findViewById(R.id.updateInputEquation)).getText().toString());
        }
        catch (NullPointerException e){

        }

        super.onSaveInstanceState(outState);
    }

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

    public void createView() {
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        sv.addView(ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        Log.d("Look",equations.toString());
        Button button;
        ButtonHandler bh = new ButtonHandler();
        Collection<MyEquation> temp = equations.values();
        Iterator<MyEquation> it = temp.iterator();
        for (int i = 0, j = equations.size(); i < j; i++) {
            button = new Button(this);
            String name = it.next().getName();
            button.setText(name);
            button.setOnClickListener(bh);
            button.setAllCaps(false);
            ll.addView(button);
        }
        setContentView(sv);
    }
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
    public void delete(View v){
        db.deleteByName(equations.get(currentEquation));
        equations.remove(currentEquation);
        createView();

    }

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