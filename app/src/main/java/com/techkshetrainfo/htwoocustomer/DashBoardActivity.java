package com.techkshetrainfo.htwoocustomer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techkshetrainfo.htwoocustomer.Api.ApiClient;
import com.techkshetrainfo.htwoocustomer.Api.ApiInterface;
import com.techkshetrainfo.htwoocustomer.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashBoardActivity extends AppCompatActivity {

    private String app_url3 = "Replace with playstore link";
    ImageButton ibBilling, ibInvoice;
    EditText new_pass, old_pass;    //, comfirn_pass;
    private AlertDialog dialog;
    private SessionManager session;
    private ProgressBar progress;
    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        //     name
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent notificationIntent = new Intent(DashBoardActivity.this, PlaceOrderActivity.class);
                startActivity(notificationIntent);
            }
        });
        ibBilling = (ImageButton) findViewById(R.id.billingImageButton);
        ibInvoice = (ImageButton) findViewById(R.id.invoiceImageButton);
        ibBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interntBilling = new Intent(DashBoardActivity.this, BillingActivity.class);
                startActivity(interntBilling);
            }
        });
        ibInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interntInvoice = new Intent(DashBoardActivity.this, InvoiceActivity.class);
                startActivity(interntInvoice);
            }
        });
    }

    private void change_password() {

        SharedPreferences log_sesion = getSharedPreferences("log_session", MODE_PRIVATE);
        String customer_contact = log_sesion.getString("customer_contact", "");
        String old_password = old_pass.getText().toString();
        String new_password = new_pass.getText().toString();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.change_password(customer_contact, old_password, new_password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String success_code = response.body().getCode().toString();

                if (success_code.equals("101")) {
                    Toast.makeText(DashBoardActivity.this, R.string.pass_change_success, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                } else {

                    Toast.makeText(DashBoardActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(DashBoardActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showDialogs() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        old_pass = (EditText) view.findViewById(R.id.et_old_password);
        new_pass = (EditText) view.findViewById(R.id.et_new_password);
//        comfirn_pass = (EditText) view.findViewById(R.id.confirm_password);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String old_password = old_pass.getText().toString();
                String new_password = new_pass.getText().toString();
//                String confirm_password = comfirn_pass.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {  //&& !confirm_password.isEmpty()

                    progress.setVisibility(View.VISIBLE);
                    if (isNetworkAvailable()) {
                        change_password();


                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
                        builder.setCancelable(false);
                        builder.setTitle("No Internet");
                        builder.setMessage("Please Check Your Internet Connection!");

                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                // finish();
                            }
                        });


                        AlertDialog dialog = builder.create(); // calling builder.create after adding buttons
                        dialog.show();
                        Toast.makeText(DashBoardActivity.this, "Network Unavailable!", Toast.LENGTH_LONG).show();
                    }

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Fields are empty");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, app_url3 + " ");
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;
            case R.id.notification:
                Intent notificationIntent = new Intent(DashBoardActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
                return true;
            case R.id.profileSetting:
                Intent profileIntent = new Intent(DashBoardActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            case R.id.changePassword:
                showDialogs();
                return true;
            case R.id.contactUs:
                Intent contactIntent = new Intent(DashBoardActivity.this, ContactUsActivity.class);
                startActivity(contactIntent);
                return true;
            case R.id.logout:
                SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
                editor.putString("loggedin", "0");
                editor.putString("user_email", "");
                editor.putString("user_name", "");
                editor.putString("user_phone", "");
                editor.commit();
                session.logoutUser();
                Toast.makeText(this, "Logged Out", Toast.LENGTH_LONG).show();
                finish();
//            startActivity(getIntent());
                Intent change_intent = new Intent(DashBoardActivity.this, LoginActivity.class);
                startActivity(change_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}