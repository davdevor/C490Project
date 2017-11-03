package com.example.david.equationapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.david.equationapp.models.DatabaseController;
import com.example.david.equationapp.models.MyEquation;
import com.example.david.equationapp.models.PostfixCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by David on 9/25/2017.
 */

public class SearchActivity extends AppCompatActivity {
    private DatabaseController db = MainActivity.getDB();
    private EditText searchBox;
    private  LinearLayout ll;
    private HashMap<String,MyEquation> equations = db.selectAll();
    private MyEquation currentEquation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBox = findViewById(R.id.searchBoxSearchActivity);
        ll = findViewById(R.id.linearLayoutSearchActivity);
        searchBox.setText(MainActivity.search().toLowerCase());
        searchBox.addTextChangedListener(new TextChangeListenr());
        updateView();
    }
    public void updateView() {
        ll.removeAllViews();
        Iterator<MyEquation> it = equations.values().iterator();
        String search = searchBox.getText().toString().toLowerCase();
        while (it.hasNext()) {
            MyEquation temp = it.next();
            if (temp.getName().toLowerCase().contains(search)) {
                Button btn = new Button(this);
                btn.setAllCaps(false);
                btn.setText(temp.getName());
                btn.setOnClickListener(new OnClick());
                ll.addView(btn);
            }
        }
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
    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            setContentView(R.layout.activity_view_equation);
            TextView nameTV = (TextView)findViewById(R.id.viewequationName);
            TextView descriptionTV = (TextView) findViewById(R.id.viewequationDescription);
            TextView courseTV = (TextView) findViewById(R.id.viewequationCourse);
            TextView equationTV = (TextView) findViewById(R.id.viewequationEquation);
            Button button = (Button) view;
            currentEquation = equations.get(button.getText().toString());
            nameTV.setText(currentEquation.getName());
            descriptionTV.setText(currentEquation.getDescription());
            courseTV.setText(currentEquation.getCourse());
            equationTV.setText(currentEquation.getEquation());
        }
    }

    class TextChangeListenr implements TextWatcher {
        @Override
        public void afterTextChanged(Editable e)
        {
            updateView();
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }
    }
}
