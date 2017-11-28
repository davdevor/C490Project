package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.david.equationapp.models.DatabaseController;
import com.example.david.equationapp.models.MyEquation;
import com.example.david.equationapp.models.MyParseException;
import com.example.david.equationapp.models.PostfixCalculator;

import java.util.ArrayList;

/**
 * Created by David on 11/6/2017.
 */

public class ComputeActivity extends AppCompatActivity {
    private MyEquation currentEquation;
    private DatabaseController db = MainActivity.getDB();
    private String equationName, equationDescription, equationCourse, equation,values;
    private final String BUNDLE_STRING_EQUATION ="name";
    private final String BUNDLE_STRING_VALUES = "values";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        if(b != null) {
            currentEquation = db.selectAll().get(b.getString("name"));
            createView();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        db.addValueEventListener();
    }
    @Override
    protected void onPause(){
        db.removeValueEventListener();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(BUNDLE_STRING_EQUATION,currentEquation.getName());
        outState.putString(BUNDLE_STRING_VALUES,((EditText)findViewById(R.id.viewequationInput)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentEquation = db.selectAll().get(savedInstanceState.getString(BUNDLE_STRING_EQUATION));
        values = (savedInstanceState.getString(BUNDLE_STRING_VALUES));
        createView();
    }

    public void createView(){
        setContentView(R.layout.activity_view_equation);
        TextView nameTV = findViewById(R.id.viewequationName);
        TextView descriptionTV = findViewById(R.id.viewequationDescription);
        TextView courseTV = findViewById(R.id.viewequationCourse);
        TextView equationTV = findViewById(R.id.viewequationEquation);

        equationName = currentEquation.getName();
        equationDescription = currentEquation.getDescription();
        equationCourse = currentEquation.getCourse();
        equation = currentEquation.getEquation();
        nameTV.setText(equationName);

        if(equationDescription.length() == 0){
            descriptionTV.setText(R.string.noDescription);
        }
        else{
            descriptionTV.setText(equationDescription);
        }

        if(equationCourse.length() == 0){
            courseTV.setText(R.string.noCourse);
        }
        else{
            courseTV.setText(equationCourse);
        }

        if(equation.length() == 0){
            equationTV.setText(R.string.noEquation);
        }
        else {
            equationTV.setText(equation);
        }
        if(values != null){
            EditText valuesET = findViewById(R.id.viewequationInput);
            valuesET.setText(values);
        }
    }

    public void compute(View v){
        TextView answerTV =  findViewById(R.id.viewequationAnswer);
        EditText valuesET =  findViewById(R.id.viewequationInput);
        String valuesString = valuesET.getText().toString();
        if(valuesString.length() == 0){
            Toast.makeText(this,R.string.noValues,Toast.LENGTH_SHORT).show();
        }
        else {
            valuesString = valuesString.replaceAll("\\s", "");
            valuesString += ",";
            ArrayList<String> varValue = new ArrayList<>();
            StringBuilder temp = new StringBuilder();
            for (int i = 0, k = valuesString.length(); i < k; i++) {

                char digit = valuesString.charAt(i);
                while (digit != ',' && i < k) {
                    temp.append(digit);
                    digit = valuesString.charAt(++i);
                }
                varValue.add(temp.toString());
                temp = new StringBuilder();
            }
            try {
                PostfixCalculator calc = new PostfixCalculator(currentEquation.getEquation(), varValue);
                answerTV.setText(Double.toString(calc.getResult()));
            }
            catch (MyParseException e){

            }
            finally {
                Toast.makeText(this,R.string.ParseException,Toast.LENGTH_LONG).show();
                return;
            }

        }
    }
}