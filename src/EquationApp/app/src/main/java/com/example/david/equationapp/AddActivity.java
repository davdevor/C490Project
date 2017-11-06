package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.david.equationapp.models.*;
import com.google.firebase.auth.FirebaseAuth;
import java.util.HashMap;

/**
 * Created by David on 9/25/2017.
 */

public class AddActivity extends AppCompatActivity {
    private IDatabase db = MainActivity.getDB();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }
    public void setData(View v){

        EditText nameET = findViewById(R.id.addInputName);
        EditText descriptionET = findViewById(R.id.addInputDescription);
        EditText courseET = findViewById(R.id.addInputCourse);
        EditText equationET = findViewById(R.id.addInputEquation);

        String equationName = nameET.getText().toString();
        HashMap<String,MyEquation> temp = (HashMap<String,MyEquation>)db.selectAll();

        if(!temp.containsKey(equationName)){
            if(!equationName.equals("")){
                MyEquation equ = new MyEquation(equationName,
                descriptionET.getText().toString(),
                courseET.getText().toString(),
                equationET.getText().toString());
                db.insert(equ);
                Toast.makeText(this,R.string.equationAdded,Toast.LENGTH_LONG).show();
                this.finish();
            }
            else{
                Toast.makeText(this,R.string.equationNameEmpty,Toast.LENGTH_LONG).show();
            }

        }
        else{
            Toast.makeText(this,R.string.equationExists,Toast.LENGTH_LONG).show();
        }
    }
}
