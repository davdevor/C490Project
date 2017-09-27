package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.example.david.equationapp.models.DatabaseManager;
import com.example.david.equationapp.models.MyEquation;
import com.example.david.equationapp.models.PostfixCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by David on 9/25/2017.
 */

public class AllActivity extends AppCompatActivity {
    private DatabaseManager db;
    private HashMap<String,MyEquation> equations;
    private LinearLayout ll;
    private MyEquation currentEquation;
    private PostfixCalculator calc = new PostfixCalculator();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseManager(this);
        createView();
    }
    public void createView(){
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

    public void compute(View v){
        TextView answerTV = (TextView) findViewById(R.id.viewequationAnswer);
        EditText valuesET = (EditText) findViewById(R.id.viewequationInput);
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            //retrieve name and price of candy
            setContentView(R.layout.activity_view_equation);
            TextView nameTV = (TextView)findViewById(R.id.viewequationName);
            TextView descriptionTV = (TextView) findViewById(R.id.viewequationDescription);
            TextView courseTV = (TextView) findViewById(R.id.viewequationCourse);
            TextView equationTV = (TextView) findViewById(R.id.viewequationEquation);
            Button button = (Button) v;
            currentEquation = equations.get(button.getText().toString());
            nameTV.setText(currentEquation.getName());
            descriptionTV.setText(currentEquation.getDescription());
            courseTV.setText(currentEquation.getCourse());
            equationTV.setText(currentEquation.getEquation());

        }
    }
}
