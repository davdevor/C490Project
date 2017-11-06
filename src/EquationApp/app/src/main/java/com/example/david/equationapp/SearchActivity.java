package com.example.david.equationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.example.david.equationapp.models.DatabaseController;
import com.example.david.equationapp.models.MyEquation;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by David on 9/25/2017.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseController db = MainActivity.getDB();
    private EditText searchBox;
    private  LinearLayout ll;
    private HashMap<String,MyEquation> equations = db.selectAll();
    private String BUNDLE_STRING_SEARCH = "search";

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
                btn.setOnClickListener(this);
                ll.addView(btn);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        EditText searchBox = findViewById(R.id.searchBoxSearchActivity);
        outState.putString(BUNDLE_STRING_SEARCH,searchBox.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        EditText searchBox = findViewById(R.id.searchBoxSearchActivity);
        searchBox.setText(savedInstanceState.getString(BUNDLE_STRING_SEARCH));
    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        b.putString("name",((Button)view).getText().toString());
        Intent myIntent = new Intent(this,ComputeActivity.class);
        myIntent.putExtras(b);
        this.startActivity(myIntent);
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
