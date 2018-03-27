package com.techkshetrainfo.htwoocustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techkshetrainfo.htwoocustomer.Api.ApiClient;
import com.techkshetrainfo.htwoocustomer.Api.ApiInterface;
import com.techkshetrainfo.htwoocustomer.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvForgot, tvRegister;
    EditText et_phone, et_password;
    private SessionManager session;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(getApplicationContext());

        btnLogin = (Button) findViewById(R.id.btn_login);
        tvForgot = (TextView) findViewById(R.id.tv_reset_password);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        progress = (ProgressBar) findViewById(R.id.progress);
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            et_phone.setText(loginPreferences.getString("username", ""));
            et_password.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate_user();

            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentForgot = new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(intentForgot);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interntRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(interntRegister);
            }
        });

    }

    private void validate_user() {

        if (et_phone.getText().toString().length() <= 9 || !CommonFunctions.isValidMobile(et_phone.getText().toString())) {
            et_phone.setError(getString(R.string.phone_error));
        } else if (et_password.getText().toString().length() == 0 || et_password.length() < 5) {
            et_password.setError("Password cannot be less than 5 characters!");
        } else {

            if (CommonFunctions.isnetworkavailable(LoginActivity.this)) {
                // refresh_database();
                progress.setVisibility(View.VISIBLE);
                login_user(et_phone.getText().toString().trim(), et_password.getText().toString().trim());
            } else {
                Toast.makeText(this, R.string.internet_error, Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void login_user(String number, String password) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);


        Call<LoginResponse> call = apiService.login_user(number, password);
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", number);
            loginPrefsEditor.putString("password", password);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getCode().equals("101")) {
                    SharedPreferences.Editor editor = getSharedPreferences("log_session", MODE_PRIVATE).edit();
                    editor.putString("loggedin", "1");
                    editor.putString("user_phone", et_phone.getText().toString().trim());
                    editor.putString("user_name", "");
                    editor.putString("user_phone", "");
                    editor.commit();
                    session.createLoginSession("", et_phone.getText().toString().trim());
                    Intent inr = new Intent(LoginActivity.this, DashBoardActivity.class);
                    startActivity(inr);
                    progress.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_credential_msg, Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, R.string.internet_error, Toast.LENGTH_LONG).show();

            }
        });

    }

}
