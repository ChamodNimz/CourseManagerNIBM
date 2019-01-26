package com.example.chamod.coursemanagernibm;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity {

    private String token="";
    private UserProfile profile;
    ButtonRectangle btnProgress, btnProfile,btnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        profile = new UserProfile();
        profile.setToken(getIntent().getStringExtra("token"));
        btnProfile = findViewById(R.id.btnProfile);
        btnProgress = findViewById(R.id.btnprogress);
        btnLogout = findViewById(R.id.btnLogout);

        // Get user profile from server
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(Dashboard.this);
        String url ="https://nibm-api.herokuapp.com/web/api/profile/myProfile";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            // Initialize UserProfile Object
                            JSONObject jsonResponse = new JSONObject(response).getJSONObject("data");
                            profile.setFirst_name(jsonResponse.getString("first_name"));
                            profile.setLast_name(jsonResponse.getString("last_name"));
                            profile.setEmail(jsonResponse.getString("email"));
                            profile.setIndex(jsonResponse.getString("index"));
                            profile.setCourse_name(jsonResponse.getString("course_name"));

                        }catch (JSONException e){
                            e.printStackTrace();
                            Log.e("Error JSON",e.toString() );
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error :","Error occured");
            }
        }){

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization",profile.getToken());
                return params;
            }

        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        // btn progress event
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Dashboard.this,Results.class);
                intent.putExtra("profile",profile);
                startActivity(intent);
            }
        });

        //btn Profile event
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Dashboard.this,Profile.class);
                intent.putExtra("profile",profile);
                startActivity(intent);
            }
        });

        //logout event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    SQLiteDatabase database = openOrCreateDatabase("users.db", MODE_PRIVATE, null);
                    database.execSQL("drop table tokens");

                    Intent intent = new Intent(Dashboard.this,Home.class);// New activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish(); // Call once you redirect to another activity
                }catch(SQLException e){
                    e.printStackTrace();
                    Log.e("Log out error :",e.toString());
                }
            }
        });


    }
    public void closeApplication(View view) {
        finish();
        moveTaskToBack(true);
    }

}
