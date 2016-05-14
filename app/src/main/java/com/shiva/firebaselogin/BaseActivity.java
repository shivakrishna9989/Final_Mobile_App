package com.shiva.firebaselogin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.client.Firebase;


public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    /* FireBase object to authenticate with the firebase account*/
    public Firebase mFirebase;

    /* Progress dialog to display to the user while doing network operations*/
    public ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Set up a context to the FireBase to work with the app*/
        Firebase.setAndroidContext(this);

        mFirebase = new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed while fetching data from Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {

    }
}
