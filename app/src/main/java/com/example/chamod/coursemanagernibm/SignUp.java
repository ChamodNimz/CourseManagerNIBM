package com.example.chamod.coursemanagernibm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SignUp extends AppCompatActivity {

    MaterialEditText
            txtFirstName,
            txtLastName,
            txtEmail,
            txtIndex,
            txtRegPassword,
            txtRegPasswordRecheck,
            txtCourseName;
    ButtonRectangle btnSignUp;
    SweetAlertDialog pDialog= null;
    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initialize();
        pDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.PROGRESS_TYPE);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // sweet alert spinner
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                //sign up call
                signUp(getRegistryDetails());
            }
        });

    }

    public void signUp(final UserProfile profile){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        url = "https://nibm-api.herokuapp.com/web/api/profile";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        pDialog.hide();
                        new SweetAlertDialog(SignUp.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Your account created")
                                .setContentText("account creation successful please login")
                                .show();
                        Toast.makeText(SignUp.this, "Account created", Toast.LENGTH_LONG).show();


                        // delay before throwing user to login
                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        startActivity(new Intent(SignUp.this,Home.class));
                                    }
                                },
                                5000);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("course_name", profile.getCourse_name());
                params.put("email", profile.getEmail());
                params.put("password", profile.getPassword());
                params.put("index", profile.getIndex());
                params.put("first_name", profile.getFirst_name());
                params.put("last_name", profile.getLast_name());

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void initialize(){
        txtCourseName = findViewById(R.id.txtCourseName);
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtEmail = findViewById(R.id.txtEmail);
        txtIndex = findViewById(R.id.txtIndex);
        txtRegPassword  = findViewById(R.id.txtRegPassword);
        txtRegPasswordRecheck = findViewById(R.id.txtRegPasswordRecheck);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    public UserProfile getRegistryDetails(){
       UserProfile userProfile = new UserProfile();

       userProfile.setCourse_name(txtCourseName.getText().toString());
       userProfile.setEmail(txtEmail.getText().toString());
       userProfile.setPassword(txtRegPassword.getText().toString());
       userProfile.setFirst_name(txtFirstName.getText().toString());
       userProfile.setLast_name(txtLastName.getText().toString());
       userProfile.setIndex(txtIndex.getText().toString());

       return userProfile;
    }

    // sign up button click

}
