package com.techkshetrainfo.htwoocustomer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        LinearLayout options_layout = (LinearLayout) findViewById(R.id.notification_list);
        options_layout.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);
        View to_add = inflater.inflate(R.layout.notification_element,
                options_layout, false);

        TextView not_title = (TextView) to_add.findViewById(R.id.notification_title);
        TextView not_msg = (TextView) to_add.findViewById(R.id.notification_msg);
        TextView not_date = (TextView) to_add.findViewById(R.id.notification_date);
    }
}
