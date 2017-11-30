package com.example.david.equationapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.david.equationapp.models.DatabaseController;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import android.view.MenuItem;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 123;
    private  static DatabaseController db;
    private static String searchWord;
    private static String BUNDLE_STRING_SEARCH = "mainSearch";

    /**
     *
     * the onCreate method is used to initialize the FirebaseAuth object
     *
     * @param savedInstanceState the bundle passed to the method, its not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     *
     * this method inflates the xml file for the options menu
     *
     * @param menu
     * @return true, the menu gets inflated
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * in this method I call the signIn method
     */
    @Override
    protected void onResume(){
        super.onResume();
        signIn();

    }

    /**
     * in this method I remove the database listener when the activity pauses, so
     * it does not use up the battery
     */
    @Override
    protected void onPause(){
        // the user has to be not null to remove the database listener
        // the database listener only gets set once the user is signed in
        if(mAuth.getCurrentUser()!=null){
            db.removeValueEventListener();
        }
        super.onPause();
    }

    /**
     *
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        EditText searchBox = findViewById(R.id.mainSearchBox);
        if(searchBox!=null) {
            outState.putString(BUNDLE_STRING_SEARCH, searchBox.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        EditText searchBox = findViewById(R.id.mainSearchBox);
        searchBox.setText(savedInstanceState.getString(BUNDLE_STRING_SEARCH));
    }

    private void signIn(){
        if(mAuth.getCurrentUser()!=null){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            db = new DatabaseController();
            db.addValueEventListener();
        }
        else{
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()//.setIsSmartLockEnabled(false)
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),RC_SIGN_IN);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
            case R.id.app_bar_delete_account:{
                final MainActivity temp = this;
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Do you really want to delete your account?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                AuthUI.getInstance()
                                        .delete(temp)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Deletion succeeded
                                                    Toast.makeText(MainActivity.this, R.string.deleted, Toast.LENGTH_SHORT).show();
                                                    AuthUI.getInstance().signOut(temp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            startActivity(new Intent(new Intent(MainActivity.this, MainActivity.class)));
                                                            finish();
                                                        }
                                                    });
                                                } else {
                                                    // Deletion failed
                                                    Toast.makeText(MainActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                        return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainSearchButton: {
                searchWord = ((EditText)findViewById(R.id.mainSearchBox)).getText().toString();
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
    public static DatabaseController getDB(){
        return db;
    }
    public static String search(){
        return searchWord;
    }
}
