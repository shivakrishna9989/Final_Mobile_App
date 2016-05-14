package com.shiva.firebaselogin;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Welcome extends BaseActivity implements View.OnClickListener,RequestsHistoryAdapter.RequestListener {

    String userName = "";
    RecyclerView recyclerView;
    RequestsHistoryAdapter adapter;
    List<Request> requestsList = new ArrayList<>();
    List<String> keysList = new ArrayList<>();
    boolean isProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        /* Set up a view here to display the logged in user details*/
        Bundle bundle = getIntent().getExtras();
        String role = "";
        if (bundle != null) {
            userName = bundle.getString("user");
            role = bundle.getString("role");
        }

        if (role != null && role.equalsIgnoreCase("provider")){
            isProvider = true;
            LinearLayout servicesLayout = (LinearLayout) findViewById(R.id.user_service_layout);
            if ( servicesLayout != null) {
                servicesLayout.setVisibility(View.GONE);
            }
        }
        final Button babysittingButton=(Button)findViewById(R.id.button);
        final Button oldCareButton=(Button)findViewById(R.id.button2);
        final Button groceryPickButton=(Button)findViewById(R.id.button3);

        babysittingButton.setOnClickListener(this);
        oldCareButton.setOnClickListener(this);
        groceryPickButton.setOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter = new RequestsHistoryAdapter(requestsList, Welcome.this, isProvider);
        recyclerView.setAdapter(adapter);

        TextView welcomeText = (TextView)findViewById(R.id.name);
        if (welcomeText != null) {
            welcomeText.setText(getString(R.string.welcome_user, userName));
        }

        mFirebase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Request> data = new ArrayList<>();
                for (DataSnapshot snapShot: dataSnapshot.getChildren()) {
                    keysList.add(snapShot.getKey());
                    Request request = snapShot.getValue(Request.class);
                    data.add(request);
                }
                requestsList = filterRequestData(data);
                recyclerView.removeAllViews();
                recyclerView.invalidate();
                adapter.refreshList(requestsList);
            }

            @Override
            public void onCancelled(FirebaseError error) {
                Log.d("Failed","The read failed: " + error.getMessage());
            }
        });
    }

    public List<Request> filterRequestData(List<Request> requestData) {
        List<Request> data = new ArrayList<>();
        List<Request> completed = new ArrayList<>();
        List<Request> pending = new ArrayList<>();
        List<Request> inProgress = new ArrayList<>();
        for (int i=0; i<requestData.size(); i++) {
            Request request = requestData.get(i);
            if (request.getCompleted().equalsIgnoreCase("completed")) {
                completed.add(request);
            }
            if (request.getCompleted().equalsIgnoreCase("inprogress")) {
                inProgress.add(request);
            }
            if (request.getCompleted().equalsIgnoreCase("pending")) {
                pending.add(request);
            }
        }
        data.addAll(pending);
        data.addAll(inProgress);
        data.addAll(completed);
        return data;
    }
    @Override
    public void onClick(View arg0) {
        stripePayment(arg0);
    }
    public void stripePayment(View v){// Perform action on click
        Button b = (Button)v;
        switch(v.getId()) {
            case R.id.button:
              //Util.myDialog("Do you want to Proceed for Payment?",this,"Yes","No");//myDialog(String title, final Activity activity, final String yes,final String no)
                //baby
                /*Intent babyintent = new Intent(Welcome.this,StripeActivity.class);*/
                Intent babyintent = new Intent(Welcome.this,StripePaymentActivity.class);
                // 1) Possibly check for instance of first
                babyintent.putExtra("type",b.getText().toString());
                startActivity(babyintent);
                break;
            case R.id.button2:
                //Util.myDialog("Do you want to Proceed for Payment?",this,"Yes","No");//myDialog(String title, final Activity activity, final String yes,final String no)
                //oldcare
                //Intent oldcareintent = new Intent(Welcome.this,StripeActivity.class);
                Intent oldcareintent = new Intent(Welcome.this,StripePaymentActivity.class);
                oldcareintent.putExtra("type",b.getText().toString());
                startActivity(oldcareintent );
                break;

            case R.id.button3:
               // Util.myDialog("Do you want to Proceed for Payment?",this,"Yes","No");//myDialog(String title, final Activity activity, final String yes,final String no)
                //grocerypick
                /*Intent grocerypickintent = new Intent(Welcome.this,StripeActivity.class);*/
                Intent grocerypickintent = new Intent(Welcome.this,StripePaymentActivity.class);
                grocerypickintent.putExtra("type",b.getText().toString());
                startActivity(grocerypickintent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            finishAffinity();
            BaseApplication.userId = "";
            BaseApplication.serviceType = "";
            Intent logoutIntent = new Intent(Welcome.this,LoginActivity.class);
            startActivity(logoutIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClaimPressed(int position, String type) {
        Firebase firebase = mFirebase.child(keysList.get(position));
        Map<String, Object> rowData = new HashMap<>();
        rowData.put("completed", type);
        rowData.put("provider", BaseApplication.userId);
        firebase.updateChildren(rowData);
    }
}
