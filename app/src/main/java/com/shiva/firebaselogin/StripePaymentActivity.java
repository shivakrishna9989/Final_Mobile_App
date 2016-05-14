//https://www.fabric.io/kits/android/stripe/features
package com.shiva.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class StripePaymentActivity extends BaseActivity implements View.OnClickListener {

    TextView totalHours;
    int totalCost = 0;
    EditText hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Set up a view here to display the service details*/
        setContentView(R.layout.strip_payment);
        final TextView serviceTypeTextView = (TextView) findViewById(R.id.stripe_paymentTextView);
        final TextView descServicesTextView = (TextView) findViewById(R.id.DescPayment);
        final Button paymentButton = (Button) findViewById(R.id.payment);
        totalHours = (TextView) findViewById(R.id.total_hours);
        totalHours.setText(getString(R.string.total_cost, 0));
        hours = (EditText) findViewById(R.id.hours);
        hours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String hours = s.toString();
                int totalhours = Integer.valueOf(hours) * 10 ;
                totalCost = totalhours * 100;
                totalHours.setText(getString(R.string.total_cost, totalhours));
            }
        });
        paymentButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String type = bundle.getString("type");
            BaseApplication.serviceType = type;
            serviceTypeTextView.setText(type);
            if (type.equalsIgnoreCase("Baby Sitting")) {
                descServicesTextView.setText("A babysitter's provide things like:Cooking, Performing light housekeeping,Doing laundry,Helping with homework,changing diapers, watching the sleeping child, preparing meals, and playing games.Keep the kitchen and its environs neat and in good condition during work hours Responsible for planning and organizing events in the family as directed by the parents Responsible for maintaining some family accounts and paying of bills Overseeing other workers and staff Professionally respond to calls from employers on checking up on their children to be rest assured that they are okay Take charge of feeding the pets during work hours Responsible for taking children to school and back.");
            } else if (type.equalsIgnoreCase("Old Care")) {
                descServicesTextView.setText("Home care nurses provide assistance with simple tasks such as bathing, grooming and eating.");
            } else if (type.equalsIgnoreCase("Grocery Pick")) {
                descServicesTextView.setText("The Order Filler/Picker is responsible for filling customer orders and delivering ... Equipment:Order Picking: Efficiently pick customer orders for shipment, ensuring that the correct number and\n" +
                        "type of product is loaded and shipped. Assemble various types of merchandise for shipment. Carry or\n" +
                        "transport orders to shipping locations or delivery platforms with material handling equipment. Stencil,\n" +
                        "tag, or mark orders as requested");
            }
        }
    }

    @Override
    public void onClick(View arg0) {
        payment(arg0);
    }


    public void payment(View arg0) {
        Intent intent = new Intent(this, StripeActivity.class);
        intent.putExtra("amount", totalCost);
        startActivity(intent);
    }
}