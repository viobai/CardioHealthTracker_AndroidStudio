package com.example.tbai2_cardiobook;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class AddEditMeasurement extends DialogFragment {
    private Button dateData;
    private Button timeData;
    private EditText systolicData;
    private EditText diastolicData;
    private EditText heartRateData;
    private EditText commentData;
    private OnFragmentInteractionListener listener;
    private Measurement measurement;

    //constructor for edit mode, given existing measurement
    public AddEditMeasurement(Measurement selectedMeasurement){ measurement = selectedMeasurement; }

    //constructor for add mode
    public AddEditMeasurement(){ measurement = null; }

    public interface OnFragmentInteractionListener { void onOkPressed(Measurement newMeasurement);}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() +
                    " must implement OnFragmentInteractionListener");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.add_edit_measurement_fragment, null);
        dateData = view.findViewById(R.id.date_button);
        timeData = view.findViewById(R.id.time_button);
        systolicData = view.findViewById(R.id.systolic_editText);
        diastolicData = view.findViewById(R.id.diastolic_editText);
        heartRateData = view.findViewById(R.id.heart_rate_editText);
        commentData = view.findViewById(R.id.comment_editText);

        // set date and time picker dialog on buttons
        dateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOnClick(v);
            }
        });
        timeData.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                timeOnClick(v);
            }
        });

        //auto fill data is in edit mode
        if (measurement != null){ autoFillData();}

        //set alert dialog builder with add, delete and cancel button
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        builder.setTitle("Add/Edit Measurement");
        builder.setNeutralButton("Cancel", null);
        builder.setNegativeButton("Delete",
                //enable delete button to delete measurement
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ((MainActivity)getActivity()).onDeletePressed(measurement);
                    }
                });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return builder.create();
    }


    //enable validity check on inputs
    //add or edit measurement
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog)getDialog();
        if(d != null) {
            //disable delete button if adding new measurement
            if (measurement == null){
                d.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
            }
            //dismiss dialog on submit button only if inputs are valid and inputs and updated/added
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateNew = dateData.getText().toString();
                    String timeNew = timeData.getText().toString();
                    String systolicStr = systolicData.getText().toString(); //for validity check
                    String diastolicStr = diastolicData.getText().toString(); //for validity check
                    String heartRateStr = heartRateData.getText().toString(); //for validity check
                    int systolicNew = Integer.parseInt(systolicStr);
                    int diastolicNew = Integer.parseInt(diastolicStr);
                    int heartRateNew = Integer.parseInt(heartRateStr);
                    String commentNew = commentData.getText().toString();

                    if (validity(dateNew, timeNew, systolicStr, diastolicStr, heartRateStr)==TRUE){
                        if (measurement!=null) {
                            //update measurement
                                updateEdit(dateNew, timeNew, systolicNew, diastolicNew,
                                        heartRateNew, commentNew);
                        } else {
                            //add new measurement
                            listener.onOkPressed(new Measurement(dateNew, timeNew, systolicNew,
                                    diastolicNew, heartRateNew, commentNew));
                        }
                        dismiss();
                    }
                }
            });
        }
    }


    //auto fill data with previous inputs
    private void autoFillData(){
        dateData.setText(measurement.getDate());
        timeData.setText(measurement.getTime());
        systolicData.setText(String.valueOf(measurement.getSystolic()));
        diastolicData.setText(String.valueOf(measurement.getDiastolic()));
        heartRateData.setText(String.valueOf(measurement.getHeartRate()));
        commentData.setText(measurement.getComment());
    }


    //update previous input with new changes
    private void updateEdit(String date, String time, int sys, int dias, int heart,String comment){
        measurement.setDate(date);
        measurement.setTime(time);
        measurement.setSystolic(sys);
        measurement.setDiastolic(dias);
        measurement.setHeartRate(heart);
        measurement.setComment(comment);
        ((MainActivity)getActivity()).onEditPressed();
    }


    // date picker dialog for user selection
    //Stackoverflow post by Luc: https://stackoverflow.com/users/231957/luc
    //Answer by Jon: https://stackoverflow.com/questions/2348657/android-use-a-datepicker-and-timepicker-from-within-a-dialog
    private void dateOnClick(View v){
        dateData = (Button) v;
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int year = c.get(Calendar.YEAR);

        //the datepickerdialog which allows the user to select a date
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
            //set formatted date on the date selection button
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String formattedDate = formatDate(year, month, day);
                dateData.setText(formattedDate);
            }
            //convert selected date into yyyy-mm-dd format
            private String formatDate(int y, int m, int d){
                String strYear = Integer.toString(y);
                String strMonth = Integer.toString(m+1);
                String strDay = Integer.toString(d);
                return strYear + "-" + strMonth + "-" + strDay;
            }
        }, year, month, day);
        datePickerDialog.show();
    }



    // time picker dialog for user selection
    // source as above
    private void timeOnClick(View v){
        timeData = (Button) v;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            // set formatted time on the time selection button
            @Override
            public void onTimeSet(TimePicker timePicker, int h, int m) {
                String formattedTime = formatTime(h, m);
                timeData.setText(formattedTime);
            }
            // converts selected time into hh:mm format
            private String formatTime(int hour, int minute){
                String strHour = Integer.valueOf(hour).toString();
                String strMinute = Integer.valueOf(minute).toString();
                return (strHour + ":" + strMinute);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }


    // check validity of user inputs, pop up toast message if inputs are empty or not valid
    private Boolean validity(String date, String time, String sys, String dias, String heart){
        Boolean filled = TRUE;
        Boolean positive = TRUE;
        String message = new String();

        //check empty input field
        if (date.equals("")|| time.equals("")|| sys.equals("") || dias.equals("")|| heart.equals("") ){
            filled = FALSE;
            message = "One or more fields need to be filled!";
         }

        //check negative input
        if((Integer.parseInt(sys) < 0 || Integer.parseInt(dias) < 0 || Integer.parseInt(heart) < 0) && filled == TRUE){
            message = "Inputs cannot be negative!";
            positive = FALSE;
        }

        //pop up toast message
        if ((filled && positive) == FALSE){
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

        return (filled && positive);
    }



}

