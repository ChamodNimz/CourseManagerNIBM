package com.example.chamod.coursemanagernibm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gc.materialdesign.views.ButtonRectangle;


public class Home extends AppCompatActivity {

    ButtonRectangle btnLogin,btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnLogin = findViewById(R.id.login);
        btnSignUp = findViewById(R.id.signUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,SignUp.class));
            }
        });
    }


}
