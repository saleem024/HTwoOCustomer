package com.techkshetrainfo.htwoocustomer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techkshetrainfo.htwoocustomer.Api.ApiClient;
import com.techkshetrainfo.htwoocustomer.Api.ApiInterface;
import com.techkshetrainfo.htwoocustomer.Models.LoginResponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MapView mMapView;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    double lat;
    private SessionManager session;
    private AlertDialog dialog;
    private String app_url3 = "Replace with playstore link";
    EditText new_pass, old_pass;
    double lng;
    TextView tv_message;
    private ProgressBar progress;
    private static final int MY_REQUEST_CODE = 103;
    EditText et_address, et_name, et_mobileNO, et_quantity;
    TextView tv_customerId;
    CheckBox cb_mon, cb_tue, cb_wed, cb_thur, cb_fri, cb_sat, cb_sun, cb_all;
    Button save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        et_address = (EditText) findViewById(R.id.et_address);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobileNO = (EditText) findViewById(R.id.et_mobileNO);
        session = new SessionManager(getApplicationContext());
        progress = (ProgressBar) findViewById(R.id.progress);
        et_quantity = (EditText) findViewById(R.id.et_quantity);

        cb_mon = (CheckBox) findViewById(R.id.cb_mon);
        cb_tue = (CheckBox) findViewById(R.id.cb_tue);
        cb_wed = (CheckBox) findViewById(R.id.cb_wed);
        cb_thur = (CheckBox) findViewById(R.id.cb_thur);
        cb_fri = (CheckBox) findViewById(R.id.cb_fri);
        cb_sat = (CheckBox) findViewById(R.id.cb_sat);
        cb_sun = (CheckBox) findViewById(R.id.cb_sun);
        cb_all = (CheckBox) findViewById(R.id.cb_all);


        cb_mon.setTag("0");
        cb_tue.setTag("0");
        cb_wed.setTag("0");
        cb_thur.setTag("0");
        cb_fri.setTag("0");
        cb_sat.setTag("0");
        cb_sun.setTag("0");

        cb_all.setChecked(false);

//        cb_mon.setChecked(true);
//        cb_tue.setChecked(false);
//        cb_wed.setChecked(false);
//        cb_thur.setChecked(false);
//        cb_fri.setChecked(false);
//        cb_sat.setChecked(false);
//        cb_sun.setChecked(false);

//        tv_customerId = findViewById(R.id.tv_customerId);
//        save_btn = (Button) findViewById(R.id.save_btn);


        mMapView = (MapView) findViewById(R.id.mapView_f);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
//        String[] array = {"Daily", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat"};
//        MultiSelectionSpinner multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.spinner_delivery);
//        multiSelectionSpinner.setItems(array);
//        multiSelectionSpinner.setSelection(
//                new int[]{1}
//        );
//        multiSelectionSpinner.setListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        save_btn = (Button) findViewById(R.id.save_btn);
//        save_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent interntLogin = new Intent(ProfileActivity.this, DashBoardActivity.class);
//                startActivity(interntLogin);
//            }
//        });
        if ((int) Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(
                            new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                    android.Manifest.permission.ACCESS_FINE_LOCATION},
                            101);
                }
                //return;

            } else {
                load_map();

            }
        } else {
            load_map();

        }

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CommomFunctions.isnetworkavailable(ProfileActivity.this)) {

                    validate_complaint();


                } else {

                    CommomFunctions.show_dialog(ProfileActivity.this, getString(R.string.internet_error_title), getString(R.string.internet_error));
                }


            }

        });

    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch (view.getId()) {
            case R.id.cb_all:
                if (checked) {
//                    Boolean checkBoxState = simpleCheckBox.isChecked();
                    cb_mon.setChecked(true);
                    cb_tue.setChecked(true);
                    cb_wed.setChecked(true);
                    cb_thur.setChecked(true);
                    cb_fri.setChecked(true);
                    cb_sat.setChecked(true);
                    cb_sun.setChecked(true);
                    cb_mon.setTag("1");
                    cb_tue.setTag("1");
                    cb_wed.setTag("1");
                    cb_thur.setTag("1");
                    cb_fri.setTag("1");
                    cb_sat.setTag("1");
                    cb_sun.setTag("1");


                } else {
                    cb_mon.setChecked(false);
                    cb_tue.setChecked(false);
                    cb_wed.setChecked(false);
                    cb_thur.setChecked(false);
                    cb_fri.setChecked(false);
                    cb_sat.setChecked(false);
                    cb_sun.setChecked(false);
                    cb_mon.setTag("0");
                    cb_tue.setTag("0");
                    cb_wed.setTag("0");
                    cb_thur.setTag("0");
                    cb_fri.setTag("0");
                    cb_sat.setTag("0");
                    cb_sun.setTag("0");


                }
                break;
            case R.id.cb_mon:
                if (checked) {

                    cb_mon.setTag("1");

                } else {

                    cb_mon.setTag("0");
                }
                break;
            case R.id.cb_tue:
                if (checked) {

                    cb_tue.setTag("1");

                } else {

                    cb_tue.setTag("0");
                }
                break;
            case R.id.cb_wed:
                if (checked) {

                    cb_wed.setTag("1");

                } else {

                    cb_wed.setTag("0");
                }
                break;
            case R.id.cb_thur:
                if (checked) {
//                    checkedThur = "1";
                    //cb_thur.setChecked(true);
                    cb_thur.setTag("1");


                } else {

                    cb_thur.setTag("0");
                }
                break;
            case R.id.cb_fri:
                if (checked) {

                    cb_fri.setTag("1");

                } else {

                    cb_fri.setTag("0");
                }
                break;
            case R.id.cb_sat:
                if (checked) {
                    cb_sat.setTag("1");

                } else {

                    cb_sat.setTag("0");
                }
                break;
            case R.id.cb_sun:
                if (checked) {

                    cb_sun.setTag("1");
                } else {

                    cb_sun.setTag("0");
                }
                break;
        }
    }

    public void validate_complaint() {

        if (et_address.getText().length() == 0) {
            et_address.setError(getString(R.string.empty_location));


//        } else if (et_mobileNO.getText().toString().length() <= 9 || !CommonFunctions.isValidMobile(et_mobileNO.getText().toString())) {
//            et_mobileNO.setError(getString(R.string.empty_field));


        } else if (et_name.getText().length() == 0) {
            et_name.setError(getString(R.string.empty_field));


        } else {
            if (CommonFunctions.isnetworkavailable(ProfileActivity.this)) {
                progress.setVisibility(View.VISIBLE);
                upadte_customerprofile();
            } else {

                Toast.makeText(this, R.string.internet_error, Toast.LENGTH_SHORT).show();
            }


        }
    }

    private void upadte_customerprofile() {
        SharedPreferences log_sesion = getSharedPreferences("log_session", MODE_PRIVATE);
        String customer_contact = log_sesion.getString("user_email", "0");


        String u_name = et_name.getText().toString();
        String u_address = et_address.getText().toString();
        String u_mon = cb_mon.getText().toString();
        String u_tue = cb_tue.getText().toString();
        String u_wed = cb_wed.getText().toString();
        String u_thur = cb_thur.getText().toString();
        String u_fri = cb_fri.getText().toString();
        String u_sat = cb_sat.getText().toString();
        String u_sun = cb_sun.getText().toString();
        String u_quantity = et_quantity.getText().toString();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiService.upadte_customerprofile(customer_contact, u_name, u_address, u_mon, u_tue, u_wed, u_thur, u_fri, u_sat, u_sun, u_quantity);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                String success_code = response.body().getCode().toString();

                if (success_code.equals("101")) {
                    Toast.makeText(ProfileActivity.this, R.string.pass_change_success, Toast.LENGTH_SHORT).show();
                    et_name.setText("");
                    et_address.setText("");
                    cb_mon.setText("");
                    cb_tue.setText("");
                    cb_wed.setText("");
                    cb_thur.setText("");
                    cb_fri.setText("");
                    cb_sat.setText("");
                    cb_sun.setText("");
                    et_quantity.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                } else {

                    Toast.makeText(ProfileActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();
                    et_name.setText("");
                    et_address.setText("");
                    cb_mon.setText("");
                    cb_tue.setText("");
                    cb_wed.setText("");
                    cb_thur.setText("");
                    cb_fri.setText("");
                    cb_sat.setText("");
                    cb_sun.setText("");
                    et_quantity.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                }
//                if (response.body().getCode().equals("101")) {
//                    SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
//                    editor.putString("loggedin", "1");
//                    editor.putString("customer_name", et_name.getText().toString().trim());
//                    editor.putString("customer_address", et_address.getText().toString().trim());
//                    editor.putString("monday", cb_mon.getTag().toString().trim());
//                    editor.putString("tuesday", cb_tue.getTag().toString().trim());
//                    editor.putString("wednusday", cb_wed.getTag().toString().trim());
//                    editor.putString("thursday", cb_thur.getTag().toString().trim());
//                    editor.putString("friday", cb_fri.getTag().toString().trim());
//                    editor.putString("saturday", cb_sat.getTag().toString().trim());
//                    editor.putString("sunday", cb_sun.getTag().toString().trim());
//                    editor.putString("quantity", et_quantity.getText().toString().trim());
//                    editor.commit();
////                    session.createLoginSession("", et_phone.getText().toString().trim());
//                    Intent inr = new Intent(ProfileActivity.this, LoginActivity.class);
//                    startActivity(inr);
//                    progress.setVisibility(View.INVISIBLE);
//                    Toast.makeText(ProfileActivity.this, "Updated Successfully!", Toast.LENGTH_LONG).show();
//                    finish();
//                } else {
//
//                    //Toast.makeText(ProfileActivity.this, R.string.invalid_credential_msg, Toast.LENGTH_LONG).show();
//                    Toast.makeText(ProfileActivity.this, "User Already Exists", Toast.LENGTH_LONG).show();
//                    progress.setVisibility(View.INVISIBLE);
//                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, "Updated Successfully!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(ProfileActivity.this, R.string.pass_change_success, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                } else {

                    Toast.makeText(ProfileActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();
                    old_pass.setText("");
                    new_pass.setText("");
//                    comfirn_pass.setText("");
                    dialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, R.string.pass_not_change, Toast.LENGTH_SHORT).show();

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
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
                        Toast.makeText(ProfileActivity.this, "Network Unavailable!", Toast.LENGTH_LONG).show();
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
                Intent notificationIntent = new Intent(ProfileActivity.this, NotificationActivity.class);
                startActivity(notificationIntent);
                return true;

            case R.id.changePassword:
                showDialogs();
                return true;
            case R.id.contactUs:

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
                Intent change_intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(change_intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void load_map() {


        // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                buildGoogleApiClient();
                mGoogleApiClient.connect();

                // For showing a move to my location button
                /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {

            mMapView.onDestroy();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    load_map();


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Toast.makeText(this, "Current Location not available Please Turn On Location Service ", Toast.LENGTH_SHORT).show();
                    //CommonFunctions.showAlertDialog(Maps_activity.this,"Current Location","Current Location not available Please Turn On Location Service ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }
            case MY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    clickpic();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                // return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
        Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onConnected(@Nullable Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //lblLocation.setText(String.valueOf(mLastLocation.getLatitude())+String.valueOf(mLastLocation.getLongitude()));
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();

        } else {
            //Toast.makeText(this,"no servce",Toast.LENGTH_LONG).show();
            lat = 30.7085;
            lng = 76.7037;

        }
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this);

        try {
            addresses = geocoder.getFromLocation(lat,
                    lng, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getAddressLine(1);
            String country = addresses.get(0).getAddressLine(2);
            String c_loc = address + " - " + city + " - " + country;
            //Toast.makeText(getActivity(), country, Toast.LENGTH_LONG).show();
            et_address.setText(c_loc);
            //System.out.println(address+" - "+city+" - "+country);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LatLng sydney = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
