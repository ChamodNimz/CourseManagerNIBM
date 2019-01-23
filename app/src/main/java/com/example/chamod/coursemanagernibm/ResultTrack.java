package com.example.chamod.coursemanagernibm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ResultTrack extends AppCompatActivity {

    BarChart barchart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_track);

        barchart = findViewById(R.id.chart);
        barchart.setDrawBarShadow(false);
        barchart.setDrawValueAboveBar(true);
        barchart.setMaxVisibleValueCount(100);
        barchart.setPinchZoom(false);
        barchart.setDrawGridBackground(true);

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        barEntries.add(new BarEntry(1,75f));
        barEntries.add(new BarEntry(2,77f));
        barEntries.add(new BarEntry(3,99f));
        barEntries.add(new BarEntry(4,90f));
        barEntries.add(new BarEntry(5,89f));
        barEntries.add(new BarEntry(6,86f));
        barEntries.add(new BarEntry(7,80f));
        barEntries.add(new BarEntry(8,78f));
        barEntries.add(new BarEntry(9,95f));
        barEntries.add(new BarEntry(10,75f));

        BarDataSet barDataSet = new BarDataSet(barEntries,"Results");
        barDataSet.setColors(getResources().getColor(R.color.colorPrimary));

        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);

        barchart.setData(data);





    }
}
