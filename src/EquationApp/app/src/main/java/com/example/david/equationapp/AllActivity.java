package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.example.david.equationapp.models.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by David on 9/25/2017.
 */

public class AllActivity extends AppCompatActivity {
    private IDatabase db = MainActivity.getDB();
    private Collection<MyEquation> equationList;
    private HashMap<String,MyEquation> equations;
    private LinearLayout ll;
    private MyEquation currentEquation;
    //private PostfixCalculator calc = new PostfixCalculator();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equations = (HashMap<String,MyEquation>)db.selectAll();
        createView();
    }
    public void createView(){
        ll = new LinearLayout(this);
        equationList = equations.values();
        Button button;
        ButtonHandler bh = new ButtonHandler();
        Iterator<MyEquation> it = equationList.iterator();
        for (int i = 0, j = equationList.size(); i < j; i++) {
            button = new Button(this);
            String name = it.next().getName();
            button.setText(name);
            button.setAllCaps(false);
            button.setOnClickListener(bh);
            ll.addView(button);
        }
        setContentView(ll);
    }

    public void compute(View v){
        TextView answerTV = (TextView) findViewById(R.id.viewequationAnswer);
        EditText valuesET = (EditText) findViewById(R.id.viewequationInput);
        String valuesString = valuesET.getText().toString();
        valuesString = valuesString.replaceAll("\\s","");;
        valuesString+=",";
        ArrayList<String> varValue = new ArrayList<>();
        StringBuilder temp = new StringBuilder();
        for(int i = 0, k = valuesString.length(); i < k;i++){

            char digit = valuesString.charAt(i);
            while (digit!=','&& i < k){
                temp.append(digit);
                digit = valuesString.charAt(++i);
            }
            varValue.add(temp.toString());
            temp = new StringBuilder();
        }
        PostfixCalculator calc = new PostfixCalculator(currentEquation.getEquation(),varValue);
        answerTV.setText(Double.toString(calc.getResult()));
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
