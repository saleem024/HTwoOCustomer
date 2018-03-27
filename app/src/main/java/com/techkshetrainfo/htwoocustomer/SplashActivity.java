package com.techkshetrainfo.htwoocustomer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends AppCompatActivity {
    private Thread SplashActivityTread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l = (LinearLayout) findViewById(R.id.activity_splash);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.hyperspace_in);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        SplashActivityTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(50);
                        waited += 50;
                    }
                    SharedPreferences log_sesion = getSharedPreferences("log_session", MODE_PRIVATE);
                    String logged_in = log_sesion.getString("loggedin", "0");
                    String user_type = log_sesion.getString("user_type", "0");
                    if (logged_in.equals("1")) {
                        Intent inr = new Intent(SplashActivity.this, DashBoardActivity.class);
                        startActivity(inr);
                        finish();

                    } else {

                        Intent inr = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(inr);

                    }
                } catch (InterruptedException ignored) {
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        SplashActivityTread.start();

    }

}
