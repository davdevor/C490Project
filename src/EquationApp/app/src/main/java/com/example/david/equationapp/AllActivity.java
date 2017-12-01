package com.example.david.equationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.LinearLayout;
import com.example.david.equationapp.models.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;


/**
 * Created by David on 9/25/2017.
 */

public class AllActivity extends AppCompatActivity implements View.OnClickListener {
    private IDatabase db = MainActivity.getDB();
    private Collection<MyEquation> equationList;
    private HashMap<String,MyEquation> equations;
    private LinearLayout ll;
    private ScrollView sv;

    /**
     * this method is used to initalize the list of equations and
     * call the createview method
     * @param savedInstanceState not used
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equations = (HashMap<String,MyEquation>)db.selectAll();
        createView();
    }

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
     * this method is used to create the view
     * it is a list of all equations
     */
    public void createView(){
        sv = new ScrollView(this);
        ll = new LinearLayout(this);
        sv.addView(ll);
        ll.setOrientation(LinearLayout.VERTICAL);
        equationList = equations.values();
        Button button;
        Iterator<MyEquation> it = equationList.iterator();
        for (int i = 0, j = equationList.size(); i < j; i++) {
            button = new Button(this);
            String name = it.next().getName();
            button.setText(name);
            button.setAllCaps(false);
            button.setOnClickListener(this);
            ll.addView(button);
        }
        setContentView(sv);
    }

    @Override
    public void onClick(View view) {
        Bundle b = new Bundle();
        b.putString("name",((Button)view).getText().toString());
        Intent myIntent = new Intent(this,ComputeActivity.class);
        myIntent.putExtras(b);
        this.startActivity(myIntent);
    }
}
