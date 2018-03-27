package com.techkshetrainfo.htwoocustomer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class PaymentAddressActivity extends AppCompatActivity {
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnPlaceOrder = (Button) findViewById(R.id.place_order);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PaymentAddressActivity.this, R.style.MyAlertDialogStyle);

                // Setting Dialog Title
                alertDialog.setTitle("Confirm Order");


                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to proceed and place the order?");

                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);

                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PaymentAddressActivity.this, DashBoardActivity.class));
//                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                    }
                });

                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        //Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();

            }
        });


        ImageView editImageView = (ImageView) findViewById(R.id.add_edit);
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent detail = new Intent(DashboardActivity.this, ProfileActivity.class);
                Intent detail = new Intent(PaymentAddressActivity.this, RegisterActivity.class);
                startActivity(detail);
            }
        });
    }
}
