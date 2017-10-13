package com.example.david.equationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import  com.google.firebase.auth.*;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onStart(){
        super.onStart();
        if(mAuth.getCurrentUser()!=null){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }
        else{
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),RC_SIGN_IN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id){
            case R.id.app_bar_signout:{
                AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(new Intent(MainActivity.this, MainActivity.class)));
                        finish();
                    }
                });
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
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
