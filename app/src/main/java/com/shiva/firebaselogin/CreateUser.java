package com.shiva.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class CreateUser extends BaseActivity {

    private EditText mUserName, mPassword, mConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create);

        Button create = (Button) findViewById(R.id.create);
        mUserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mConfirm = (EditText) findViewById(R.id.confirm);
        if (create != null) {
            create.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.existng_user_login){
            Intent intent = new Intent(CreateUser.this,LoginActivity.class);
            startActivity(intent);
        }
        if ( v.getId() == R.id.create) {
           final String userName = mUserName.getText().toString();
            final String password = mPassword.getText().toString();
            final String confirm = mConfirm.getText().toString();
           Util helper= new Util();
            if (!helper.checkInternetConenction(this)) {
                Toast.makeText(getApplicationContext(),
                        R.string.networkerror,
                        Toast.LENGTH_LONG).show();
                return;
            }
            if(userName.equals("") || userName.equals(null)){
                mUserName.setError("userName should be user@firebase.com");
                mUserName.requestFocus();
                mUserName.setText("");
                return;

            }
            if (!helper.isValidEmail(userName) ) {

                mUserName.setError("Eg:user@firebase.com");
                mUserName.requestFocus();
                mUserName.setText("");
                return;
            }

            if(password.equals("") || password.equals(null)){
                mPassword.setError("Password should not be empty");
                mPassword.requestFocus();
                mPassword.setText("");
                return;

            }
            if(password.length()>=3){

            }else{
                mPassword.setError("Password must alteast 3 characters");
                mPassword.requestFocus();
                mPassword.setText("");
                return;

            }
            if(confirm.length()>=3) {
            }else{
                mConfirm.setError("Confirm Password must alteast 3 characters");
                mConfirm.requestFocus();
                mConfirm.setText("");
                return;

            }
            if(confirm.equals("") || confirm.equals(null)){
                mConfirm.setError("{Confirm Password should not be empty");
                mConfirm.requestFocus();
                mConfirm.setText("");
                return;

            }
            if ( !password.equalsIgnoreCase(confirm)) {
                mConfirm.setError("Password & Confirm Password should match");
                return;
            }
            mAuthProgressDialog.setMessage("Creating New"+ userName+"...");
            mAuthProgressDialog.show();
            mFirebase.createUser(userName, password, new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(CreateUser.this,userName+" created.", Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    mAuthProgressDialog.dismiss();
                    Toast.makeText(CreateUser.this,firebaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
