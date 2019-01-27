package com.example.chamod.coursemanagernibm;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class ResultTrack extends AppCompatActivity {

    BarChart barchart;
    String results;
    ArrayList<String> resultArray = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_track);

        // Get results from previous activity
        results = getIntent().getStringExtra("results");

        parseResults(); // parse JSON from response
        barchart = findViewById(R.id.chart);
        barchart.setDrawBarShadow(false);
        barchart.setDrawValueAboveBar(true);
        barchart.setMaxVisibleValueCount(100);
        barchart.setPinchZoom(false);
        barchart.setDrawGridBackground(true);
        barchart.getXAxis().setLabelCount(resultArray.size());
        barchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));// set labels to x axis
        barchart.getXAxis().setTextSize(10f);
        barchart.getXAxis().setTextColor(Color.RED);
        barchart.getXAxis().setLabelRotationAngle(330);
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0 ; i< resultArray.size();i++){
            barEntries.add(new BarEntry(i,convertResult(resultArray.get(i))));

        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Results");
        barDataSet.setColors(getResources().getColor(R.color.colorPrimary));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f); // to set the bar size

        barchart.setData(data);

    }

    public void parseResults(){
        String row = null;
        try{
            JSONArray jsonArray = new JSONArray(results);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);
                labels.add(shortenString(jo.getString("subject")));
                resultArray.add(jo.getString("result"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Float convertResult(String value){
        if (value.equals("A+")) {
            return 85f;
        } if (value.equals("A")) {
            return 70f;
        }  if (value.equals("A-")) {
            return 65f;
        }  if (value.equals("B+")) {
            return 60f;
        }  if (value.equals("B")) {
            return 55f;
        }  if (value.equals("B-")) {
            return 50f;
        }  if (value.equals("C+")) {
            return 45f;
        }  if (value.equals("C")) {
            return 40f;
        }  if (value.equals("C-")) {
            return 35f;
        }  if (value.equals("D+")) {
            return 30f;
        }  if (value.equals("D")) {
            return 25f;
        }  if (value.equals("E")) {
            return 20f;
        } if (value.equals("P")) {
            return 70f;
        }
        else{
            return 0f;
        }
    }

    public String shortenString(String string){

        String label = "";
        String[] array = string.split(" ");
        for(String val : array){
           label+= val.toString().charAt(0);
        }
        Log.e("debugging :",label);
        return label;
    }
}

