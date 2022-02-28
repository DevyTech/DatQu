package com.example.database.Sistem_Manual;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.database.DB_Controller.DataHelperManual;
import com.example.database.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class HomeManualFragment extends Fragment {
    BarChart barChart;
    DataHelperManual dataHelperManual;
    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_manual, container, false);

        dataHelperManual = new DataHelperManual(getContext());

        barChart = v.findViewById(R.id.barchart);
        ArrayList<BarEntry> data = new ArrayList<>();
        Cursor cursor = dataHelperManual.setbarchartdata();
        if (cursor.getCount()==0){
            barChart.setVisibility(View.INVISIBLE);
        }else{
            cursor.moveToFirst();
            for (int i=0;i < cursor.getCount(); i++){
                cursor.moveToPosition(i);
                data.add(new BarEntry(cursor.getInt(0), cursor.getInt(cursor.getColumnIndex("total"))));
            }
        }

        BarDataSet barDataSet = new BarDataSet(data, "Data Penjualan");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);
        barChart.getAxisRight().setEnabled(false);
        barChart.invalidate();


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MainActivity_Manual.mainActivitymanual.go_default_menu();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return v;
    }
}