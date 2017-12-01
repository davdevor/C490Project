package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.david.equationapp.models.*;
import java.util.HashMap;

/**
 * Created by David on 9/25/2017.
 */

public class AddActivity extends AppCompatActivity {
    private IDatabase db = MainActivity.getDB();

    /**
     * this method is used to set the content view to activity_add.xml
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * this method is used to add the event listener to the database
     */
    @Override
    protected void onResume(){
        super.onResume();
        db.addValueEventListener();
    }

    /**
     * this method is used to remove the event listener from the database
     */
    @Override
    protected void onPause(){
        db.removeValueEventListener();
        super.onPause();
    }

    /**
     * this method is used to handle the click on the add button on the activity_add.xml screen
     * @param v the view that was clicked
     */
    public void setData(View v){
        //get references to the edit texts from the view
        EditText nameET = findViewById(R.id.addInputName);
        EditText descriptionET = findViewById(R.id.addInputDescription);
        EditText courseET = findViewById(R.id.addInputCourse);
        EditText equationET = findViewById(R.id.addInputEquation);

        String equationName = nameET.getText().toString();
        HashMap<String,MyEquation> temp = (HashMap<String,MyEquation>)db.selectAll();

        //check and see if the equation name already exists
        if(!temp.containsKey(equationName)){
            //make sure name isn't empty
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
