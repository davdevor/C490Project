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
import java.util.Map;

/**
 * Created by David on 9/25/2017.
 */

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseController db = MainActivity.getDB();
    private EditText searchBox;
    private LinearLayout ll;
    private HashMap<String,MyEquation> equations = db.selectAll();
    private String BUNDLE_STRING_SEARCH = "search";

    /**
     * this method is used to add the database event listener
     */
    @Override
    protected void onResume(){
        super.onResume();
        db.addValueEventListener();
    }

    /**
     * this method is used to remove the database event listener
     */
    @Override
    protected void onPause(){
        db.removeValueEventListener();
        super.onPause();
    }

    /**
     * this method is used to set the content view and set the text of the search box with the
     * search word from the main activity, also calls update view
     * @param savedInstanceState not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchBox = findViewById(R.id.searchBoxSearchActivity);
        ll = findViewById(R.id.linearLayoutSearchActivity);
        searchBox.setText(MainActivity.search().toLowerCase());
        //adds a new TextChangeListener so when the text changes it will search again with new word
        searchBox.addTextChangedListener(new TextChangeListener());
        updateView();
    }

    /**
     * this method updates the listview with a list of equations similar to the search term
     */
    public void updateView() {
        //reset the list on screen
        ll.removeAllViews();
        //gets an iterator from the hashmap of equations
        Iterator<MyEquation> it = equations.values().iterator();
        //get the search term
        String search = searchBox.getText().toString().toLowerCase();
        //for each loop going over each item in the equation hashmap
        for (Map.Entry<String,MyEquation> temp: equations.entrySet()) {
            //if equation name contains the search string then add it to list view
            if (temp.getValue().getName().toLowerCase().contains(search)) {
                Button btn = new Button(this);
                btn.setAllCaps(false);
                btn.setText(temp.getValue().getName());
                btn.setOnClickListener(this);
                ll.addView(btn);
            }
        }
    }

    /**
     * this method is to save the search string in the bundle
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        EditText searchBox = findViewById(R.id.searchBoxSearchActivity);
        outState.putString(BUNDLE_STRING_SEARCH,searchBox.getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * this method is used to restore the search string in the search box
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        EditText searchBox = findViewById(R.id.searchBoxSearchActivity);
        searchBox.setText(savedInstanceState.getString(BUNDLE_STRING_SEARCH));
    }

    /**
     * this method handles the the clicks on the buttons in the list view
     * @param view
     */
    @Override
    public void onClick(View view) {
        // create a new bundle object and put the name of the equation clicked in the bundle
        Bundle b = new Bundle();
        b.putString("name",((Button)view).getText().toString());
        //create a new intent of the ComputeActivity and putExtras the custom bundle object
        //so when the activity starts it knows what equation it should display
        Intent myIntent = new Intent(this,ComputeActivity.class);
        myIntent.putExtras(b);
        this.startActivity(myIntent);
    }

    class TextChangeListener implements TextWatcher {
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
