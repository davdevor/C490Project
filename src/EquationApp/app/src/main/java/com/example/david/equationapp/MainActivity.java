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
    private  static IDatabase db;
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
     * this method is used to save the string in the search box in the activity_main.xml view
     *
     * @param outState the bundle object holding the out state
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        EditText searchBox = findViewById(R.id.mainSearchBox);
        if(searchBox!=null) {
            outState.putString(BUNDLE_STRING_SEARCH, searchBox.getText().toString());
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * this method is used to restore the string in the search box
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        EditText searchBox = findViewById(R.id.mainSearchBox);
        searchBox.setText(savedInstanceState.getString(BUNDLE_STRING_SEARCH));
    }

    /**
     * this method is used to handle signing in the user
     */
    private void signIn(){
        //if current user is not null then they are signed in
        //so bring them to the main screen for the app
        if(mAuth.getCurrentUser()!=null){
            //setcontentview and toolbar
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            //initialize the database controller object that all the other activities use
            db = new DatabaseController();
            db.addValueEventListener();
        }
        else{
            //else not signed in start firebase AuthUI to let user sign in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(), //only builds the google signin and email
                                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                            .build(),RC_SIGN_IN);
        }
    }

    /**
     * this method handles the clicks on the options menu
     * @param item
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.app_bar_signout:{
                // if they clicked sign out call the firebase signout
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
                //bring up an alert dialog to confirm the user deleting their account
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Do you really want to delete your account?")
                        //here it sets the positivie buttons action which is the firebase account deletion
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

    /**
     * this method handles the clicks for the buttons on the activity_main.xml view
     * @param v the view that got clicked
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainSearchButton: {
                searchWord = ((EditText)findViewById(R.id.mainSearchBox)).getText().toString();
                //if search box clicked go to SearchActivity
                Intent myIntent = new Intent(this, SearchActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainAllButton: {
                //go to AllActivity
                Intent myIntent = new Intent(this, AllActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainAddButton: {
                //go to AddActivity
                Intent myIntent = new Intent(this, AddActivity.class);
                this.startActivity(myIntent);
                break;
            }
            case R.id.mainUpdateButton: {
                //go to UpdateActivity
                Intent myIntent = new Intent(this, UpdateActivity.class);
                this.startActivity(myIntent);
                break;
            }
            default:
                break;
        }
    }

    /**
     * this method is used by all activites so they can get a reference to the database
     * @return a DatabaseController that all the activities will use
     */
    public static IDatabase getDB(){
        return db;
    }

    /**
     * this method is used by the search activity, so it can know what the initial search is
     * @return a string of the search word in the search box
     */
    public static String search(){
        return searchWord;
    }
}
