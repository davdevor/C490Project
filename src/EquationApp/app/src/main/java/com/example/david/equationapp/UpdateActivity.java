package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.david.equationapp.models.DatabaseController;
import com.example.david.equationapp.models.DatabaseManager;
import com.example.david.equationapp.models.MyEquation;
import com.example.david.equationapp.models.PostfixCalculator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by David on 9/25/2017.
 */

public class UpdateActivity extends AppCompatActivity {
    private DatabaseController db = MainActivity.getDB();
    private HashMap<String,MyEquation> equations = db.selectAll();
    private LinearLayout ll;
    private String currentEquation;
    private ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
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
            ll.addView(button);
        }
        setContentView(sv);
    }
    public void update(View v){
        EditText nameET = (EditText)findViewById(R.id.updateInputName);
        EditText descriptionET = (EditText) findViewById(R.id.updateInputDescription);
        EditText courseET = (EditText) findViewById(R.id.updateInputCourse);
        EditText equationET = (EditText) findViewById(R.id.updateInputEquation);
        MyEquation equ = new MyEquation(currentEquation,
                descriptionET.getText().toString(),
                courseET.getText().toString(),
                equationET.getText().toString());
        db.updateByName(equ);
        createView();
    }
    public void delete(View v){
        db.deleteByName(equations.get(currentEquation));
        //remove this and then on change works
        createView();

    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            //retrieve name and price of candy
            Button button = (Button) v;
            currentEquation = button.getText().toString();
            MyEquation e = equations.get(currentEquation);
            if(e != null) {
                setContentView(R.layout.activity_update);
                EditText nameET = (EditText) findViewById(R.id.updateInputName);
                EditText descriptionET = (EditText) findViewById(R.id.updateInputDescription);
                EditText courseET = (EditText) findViewById(R.id.updateInputCourse);
                EditText equationET = (EditText) findViewById(R.id.updateInputEquation);
                nameET.setText(e.getName());
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