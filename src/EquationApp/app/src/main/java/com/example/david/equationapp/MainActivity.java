package com.example.david.equationapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainSearchButton: {
                Intent myIntent = new Intent(this, SearchActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainAllButton: {
                Intent myIntent = new Intent(this, AllActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainAddButton: {
                Intent myIntent = new Intent(this, AddActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainUpdateButton: {
                Intent myIntent = new Intent(this, UpdateActivity.class);
                this.startActivity(myIntent);
                break;
            }
            default:
                break;
        }
    }
}
