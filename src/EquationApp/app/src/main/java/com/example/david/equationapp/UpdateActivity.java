package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.david.equationapp.models.DatabaseManager;
import com.example.david.equationapp.models.MyEquation;
import com.example.david.equationapp.models.PostfixCalculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by David on 9/25/2017.
 */

public class UpdateActivity extends AppCompatActivity {
    private DatabaseManager db;
    private HashMap<String,MyEquation> equations;
    private LinearLayout ll;
    private String currentEquation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManager(this);
        createView();
    }

    public void createView() {
        ll = new LinearLayout(this);
        equations = db.selectAll();
        Button button;
        ButtonHandler bh = new ButtonHandler();
        Collection<MyEquation> temp = equations.values();
        Iterator<MyEquation> it = temp.iterator();
        for (int i = 0, j = equations.size(); i < j; i++) {
            button = new Button(this);
            String name = it.next().getName();
            button.setText(name);
            button.setOnClickListener(bh);
            ll.addView(button);
        }
        setContentView(ll);
    }
    public void update(View v){
        EditText nameET = (EditText)findViewById(R.id.updateInputName);
        EditText descriptionET = (EditText) findViewById(R.id.updateInputDescription);
        EditText courseET = (EditText) findViewById(R.id.updateInputCourse);
        EditText equationET = (EditText) findViewById(R.id.updateInputEquation);
      /*  MyEquation equ = new MyEquation(equations.get(currentEquation).getId(),nameET.getText().toString(),
                descriptionET.getText().toString(),
                courseET.getText().toString(),
                equationET.getText().toString());
        db.updateById(equ);
       createView();*/
    }
    public void delete(View v){
        db.deleteById(equations.get(currentEquation).getId());
        createView();
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            //retrieve name and price of candy
            setContentView(R.layout.activity_update);
            EditText nameET = (EditText)findViewById(R.id.updateInputName);
            EditText descriptionET = (EditText) findViewById(R.id.updateInputDescription);
            EditText courseET = (EditText) findViewById(R.id.updateInputCourse);
            EditText equationET = (EditText) findViewById(R.id.updateInputEquation);
            Button button = (Button) v;
            currentEquation = button.getText().toString();
            MyEquation e = equations.get(currentEquation);
            nameET.setText(e.getName());
            descriptionET.setText(e.getDescription());
            courseET.setText(e.getCourse());
            equationET.setText(e.getEquation());

        }
    }
}