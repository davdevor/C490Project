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
    private IDatabase db = MainActivity.getDB();
    private String equationName, equationDescription, equationCourse, equation,values;
    private final String BUNDLE_STRING_EQUATION ="name";
    private final String BUNDLE_STRING_VALUES = "values";

    /**
     * this method gets the equation name from the bundle that was passed to the method
     * @param savedInstanceState the bundle has to have the equation name in a string with key "name"
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            currentEquation = db.selectAll().get(b.getString("name"));
            createView();
        }
    }

    /**
     * this method adds the database event listener
     */
    @Override
    protected void onResume(){
        super.onResume();
        db.addValueEventListener();
    }

    /**
     * this method removes the database event listener
     */
    @Override
    protected void onPause(){
        db.removeValueEventListener();
        super.onPause();
    }

    /**
     * this method saves the equation name and the data they inputted for the equation to be solved with
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putString(BUNDLE_STRING_EQUATION,currentEquation.getName());
        outState.putString(BUNDLE_STRING_VALUES,((EditText)findViewById(R.id.viewequationInput)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * this method restores the current equation and the values to solve with
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        currentEquation = db.selectAll().get(savedInstanceState.getString(BUNDLE_STRING_EQUATION));
        values = (savedInstanceState.getString(BUNDLE_STRING_VALUES));
        createView();
    }

    /**
     * this method brings up the view to solve the equation
     */
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

        //the if statements are use to put text in text boxes if the equation object has no info for it
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

    /**
     * this method handles the computation of the equation with the data entered
     * it uses the PostFix calculator model to solve the equation
     * @param v
     */
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
            boolean error = false;
            try {
                PostfixCalculator calc = new PostfixCalculator(currentEquation.getEquation(), varValue);
                answerTV.setText(Double.toString(calc.getResult()));
            }
            catch (MyParseException e){
                error = true;
            }
            finally {
                if(error)
                    Toast.makeText(this,R.string.ParseException,Toast.LENGTH_LONG).show();
                return;
            }

        }
    }
}