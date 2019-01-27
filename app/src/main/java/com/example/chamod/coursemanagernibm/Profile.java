package com.example.chamod.coursemanagernibm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    UserProfile profile;
    TextView lblEmail,
    lblCourseName,
    lblIndex,
    lblFullName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile = new UserProfile();
        // get user profile object and initialize it
        profile = (UserProfile) getIntent().getSerializableExtra("profile");


        lblCourseName = findViewById(R.id.lblCourseName);
        lblEmail = findViewById(R.id.lblEmail);
        lblIndex = findViewById(R.id.lblIndex);
        lblFullName = findViewById(R.id.lblFullName);

        lblEmail.setText(profile.getEmail());
        lblIndex.setText(profile.getIndex());
        lblCourseName.setText(profile.getCourse_name());
        lblFullName.setText(profile.getFirst_name()+" "+profile.getLast_name());


    }
}
