package com.techkshetrainfo.htwoocustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlaceOrderActivity extends AppCompatActivity {
    int minteger = 0;
    Button btnPlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnPlaceOrder = (Button) findViewById(R.id.place_order);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(PlaceOrderActivity.this, PaymentAddressActivity.class));


            }
        });


//        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlaceOrderActivity.this, R.style.MyAlertDialogStyle);
//
//                // Setting Dialog Title
//                alertDialog.setTitle("Confirm Order");
//
//
//                // Setting Dialog Message
//                alertDialog.setMessage("Are you sure you want to proceed and place the order?");
//
//                // Setting Icon to Dialog
//                //alertDialog.setIcon(R.drawable.delete);
//
//                // Setting Positive "Yes" Button
//                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivity(new Intent(PlaceOrderActivity.this, DashBoardActivity.class));
////                        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
//                    }
//                });
//
//                // Setting Negative "NO" Button
//                alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Write your code here to invoke NO event
//                        //Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
//                        dialog.cancel();
//                    }
//                });
//
//                // Showing Alert Message
//                alertDialog.show();
//
//            }
//        });
    }


    public void increaseOrder1(View view) {
        minteger = minteger + 1;
        displayOrderQty1(minteger);

    }

    public void decreaseOrder1(View view) {
        minteger = minteger - 1;
        displayOrderQty1(minteger);
    }

    private void displayOrderQty1(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.number_order_quantity1);
        displayInteger.setText("" + number);
    }

    public void increaseOrder2(View view) {
        minteger = minteger + 1;
        displayOrderQty2(minteger);

    }

    public void decreaseOrder2(View view) {
        minteger = minteger - 1;
        displayOrderQty2(minteger);
    }

    private void displayOrderQty2(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.number_order_quantity2);
        displayInteger.setText("" + number);
    }
    public void increaseOrder3(View view) {
        minteger = minteger + 1;
        displayOrderQty3(minteger);

    }

    public void decreaseOrder3(View view) {
        minteger = minteger - 1;
        displayOrderQty3(minteger);
    }

    private void displayOrderQty3(int number) {
        TextView displayInteger = (TextView) findViewById(
                R.id.number_order_quantity3);
        displayInteger.setText("" + number);
    }
}