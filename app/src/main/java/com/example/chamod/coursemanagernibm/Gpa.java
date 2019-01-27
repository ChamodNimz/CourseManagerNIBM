package com.example.chamod.coursemanagernibm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Gpa extends AppCompatActivity {

    TextView lblGpa, lblSuggestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpa);

        String data = getIntent().getStringExtra("data");

        lblGpa = findViewById(R.id.lblGpa);
        lblSuggestions = findViewById(R.id.lblSuggestions);

        try {
            JSONObject jsonResponse = new JSONObject(data);
            String suggestions = "";
            lblGpa.setText(jsonResponse.getString("data"));
            JSONArray array = jsonResponse.getJSONArray("suggestions");
            if(array.length()!=0){
                for(int i=0; i<array.length();i++){
                    suggestions+="#"+array.getString(i)+"\n";
                }
                lblSuggestions.setText(suggestions);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Error :",e.toString());
        }
    }
}
