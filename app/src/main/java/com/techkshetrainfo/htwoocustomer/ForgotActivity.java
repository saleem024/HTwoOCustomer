package com.techkshetrainfo.htwoocustomer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotActivity extends AppCompatActivity {

    TextView tv_back;
    EditText et_phone;
    Button btn_forgot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tv_back = findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent interntLogin = new Intent(ForgotActivity.this, LoginActivity.class);
                startActivity(interntLogin);
            }
        });
        et_phone = findViewById(R.id.et_phone);
        btn_forgot = findViewById(R.id.forget_btn);
        btn_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_complaint();
            }
        });

    }
    public boolean validate_complaint() {

       if (et_phone.getText().length() == 0) {
           et_phone.setError(getString(R.string.empty_field));
            return false;

        }
       else {
            Intent interntLogin = new Intent(ForgotActivity.this, LoginActivity.class);
            startActivity(interntLogin);
            Toast.makeText(ForgotActivity.this, "Password sent to your Mobile NO!", Toast.LENGTH_SHORT).show();
        }
        return true;

    }

}
