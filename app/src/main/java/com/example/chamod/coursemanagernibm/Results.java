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

import javax.xml.transform.Result;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Results extends AppCompatActivity {

    ListView resultList;
    private ArrayList<String> resultArray;
    ArrayAdapter adapter;
    TextView textView;
    ButtonRectangle btnTrack;

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://nibm-api.herokuapp.com/web/api/results/cohdse181f-008";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        resultArray = new ArrayList<>();
                        String row=null;
                        // Display the first 500 characters of the response string.
                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jo = jsonArray.getJSONObject(i);
                                row = jo.getString("subject");
                                row+="  "+ jo.getString("result");
                                resultArray.add(row);
                            }
                            Log.e("response:",response);

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
                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjNDcyZTdkZDA3M2Y0MDAxNzIzNDAzZSIsImVtYWlsIjoiY2hhbW9kQHlhaG9vLmNvbSIsImlhdCI6MTU0ODE2OTM5MCwiZXhwIjoxNTQ4Nzc0MTkwfQ.2YfuWLNO7OnXlHMN4kjCVMv94fpfY6CwGS7Wi7GCm8o");
                return params;
            }

        };


        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Results.this,ResultTrack.class));
            }
        });
    }

}
