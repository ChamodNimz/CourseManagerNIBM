package com.example.chamod.coursemanagernibm;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Results extends AppCompatActivity {

    ListView resultList;
    private ArrayList<String> resultArray;
    ArrayAdapter adapter;
    TextView textView;
    ButtonRectangle btnTrack,btnGPA;
    private String resultResponse;
    UserProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        resultList = findViewById(R.id.resultList);
        textView = findViewById(R.id.textView);
        btnTrack = findViewById(R.id.btnTrack);
        btnGPA = findViewById(R.id.btnGpa);

        profile = new UserProfile();
        profile = (UserProfile) getIntent().getSerializableExtra("profile");

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://nibm-api.herokuapp.com/web/api/results/"+profile.getIndex();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultArray = new ArrayList<>();
                        String row=null;
                        resultResponse = response;
                        // Display the first 500 characters of the response string.
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                row = jo.getString("subject");
                                row+="  "+ jo.getString("result");
                                resultArray.add(row);
                            }

                            adapter = new ArrayAdapter<String>(Results.this,R.layout.row,R.id.row,resultArray);
                            resultList.setAdapter(adapter);
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("","Error occured");
            }
        }){

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", profile.getToken());
                return params;
            }

        };


        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Results.this,ResultTrack.class);
                intent.putExtra("results",resultResponse);
                startActivity(intent);
            }
        });

        // GPA view and suggestions
        btnGPA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                try {
                    JSONObject postObj = new JSONObject();
                    JSONArray jsonArray = new JSONArray(resultResponse);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        object.put("course_name",profile.getCourse_name());
                        array.put((JSONObject)object);
                    }
                    postObj.put("modules",array);


                    // send POST req
                    // Instantiate the RequestQueue.
                    String url = "https://nibm-api.herokuapp.com/web/api/gpa";
                    RequestQueue q = Volley.newRequestQueue(Results.this);
                    JsonObjectRequest jobReq = new JsonObjectRequest(Request.Method.POST, url, postObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.e("GPA : ",jsonObject.toString());
                                    startActivity(new Intent(Results.this,Gpa.class).putExtra("data",jsonObject.toString()));
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });

                    q.add(jobReq);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
