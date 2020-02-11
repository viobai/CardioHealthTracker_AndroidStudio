package com.example.tbai2_cardiobook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<Measurement> {

    private ArrayList<Measurement> measurements;
    private Context context;

    public CustomList(Context context, ArrayList<Measurement> measurements) {
        super(context,0,measurements);
        this.measurements = measurements;
        this.context = context;
    }


    //set layout of each adapter view in ListView
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content,parent,false);
        }

        Measurement measurement = measurements.get(position);

        TextView datetimeData = view.findViewById(R.id.date_time_text);
        TextView systolicData = view.findViewById(R.id.systolic_text);
        TextView diastolicData = view.findViewById(R.id.diastolic_text);
        TextView heartRateData = view.findViewById(R.id.heart_rate_text);
        TextView commentData = view.findViewById(R.id.comment_text);

        String dateTime = measurement.getDate() + "  " + measurement.getTime();
        String systolic = "Systolic Pressure: " + measurement.getSystolic() + " mmHg";
        String diastolic = "Diastolic Pressure: " + measurement.getDiastolic() + " mmHg";
        String heartRate = "Heart Rate: " + measurement.getHeartRate();
        String comment = measurement.getComment();

        //calls abnormality check on pressure
        checkAbnormality(view, measurement.getSystolic(), measurement.getDiastolic());

        datetimeData.setText(dateTime);
        systolicData.setText(systolic);
        diastolicData.setText(diastolic);
        heartRateData.setText(heartRate);
        commentData.setText(comment);

        return view;
    }

    //show warning sign beside abnormal pressure inputs
    private void checkAbnormality(View v, int sys, int dias){
        ImageView sysWarning = v.findViewById(R.id.sys_warning);
        ImageView diasWarning = v.findViewById(R.id.dias_warning);

        if (sys < 90 || sys > 140){
            sysWarning.setVisibility(View.VISIBLE);
        } else {
            sysWarning.setVisibility(View.INVISIBLE);
        }

        if (dias < 60 || dias > 90){
            diasWarning.setVisibility(View.VISIBLE);
        } else {
            diasWarning.setVisibility(View.INVISIBLE);
        }
    }
}
