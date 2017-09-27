package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.david.equationapp.models.DatabaseManager;
import com.example.david.equationapp.models.MyEquation;

/**
 * Created by David on 9/25/2017.
 */

public class AddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }
    public void setData(View v){
        EditText nameET = (EditText)findViewById(R.id.addInputName);
        EditText descriptionET = (EditText) findViewById(R.id.addInputDescription);
        EditText courseET = (EditText) findViewById(R.id.addInputCourse);
        EditText equationET = (EditText) findViewById(R.id.addInputEquation);
        MyEquation equ = new MyEquation(0,nameET.getText().toString(),
                descriptionET.getText().toString(),
                courseET.getText().toString(),
                equationET.getText().toString());
        DatabaseManager db = new DatabaseManager(this);
        db.insert(equ);
        Toast.makeText(this,"added",Toast.LENGTH_LONG).show();
    }
}
