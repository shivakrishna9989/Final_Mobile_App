package com.shiva.firebaselogin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class

LoginActivity extends BaseActivity {

    private EditText mUserName, mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Connect to FireBase with a url to handle the data in it*/

        Button loginButton = (Button) findViewById(R.id.login);
        mUserName = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        if (loginButton != null) {
            loginButton.setOnClickListener(this);
        }

        Button createUser = (Button) findViewById(R.id.create);
        if (createUser != null) {
            createUser.setOnClickListener(this);
        }


    }


    @Override
    public void onClick(View v) {
        final String username = mUserName.getText().toString();
        final String password = mPassword.getText().toString();
        Util helper=new Util();
        if (!helper.checkInternetConenction(this)) {
            Toast.makeText(getApplicationContext(),
                    R.string.networkerror,
                    Toast.LENGTH_LONG).show();
            return;
        }
		else{

        if ( v.getId() == R.id.login) {
            if (!helper.isValidEmail(username) ) {

                mUserName.setError("Invalid Username");
                mUserName.requestFocus();
                mUserName.setText("");
                return;
            }
            if(!helper.isValidPassword(password)){
                mPassword.setError("Invalid Password");
                mPassword.setText("");
                mPassword.requestFocus();
                return;}
            mAuthProgressDialog.show();

            mFirebase.authWithPassword(username, password, new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    Log.d("login","success");
                    View view = findViewById(R.id.coordinatorlayout);
                    mAuthProgressDialog.dismiss();
                    Util.closeKeyBoard(getApplicationContext());
                    if (view != null) {
                       // Snackbar.make(view, R.string.login_success, Snackbar.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this,Welcome.class);
                        intent.putExtra("user",username);
                        BaseApplication.userId = authData.getUid();
                        if(username.contains("provider@firebase.com")){
                            intent.putExtra("role","provider");
                        }else{
                            intent.putExtra("role","user");
                        }

                        startActivity(intent);
                    }
                }

                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    Log.d("login","failed");
                    mAuthProgressDialog.dismiss();
                    Util.closeKeyBoard(getApplicationContext());
                    View view = findViewById(R.id.coordinatorlayout);
                    if (view != null) {
                        Snackbar.make(view, R.string.lgoin_fail, Snackbar.LENGTH_LONG).show();
                        Util.myDialog("Login Fail",LoginActivity.this,"Ok","");//myDialog(String title, final Activity activity, final String yes,final String no)
                    }
                }
            });
        } else if (v.getId() == R.id.create) {
            Intent createUserIntent = new Intent(LoginActivity.this, CreateUser.class);
            startActivity(createUserIntent);
        }
    }}
}
