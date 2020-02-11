package com.example.tbai2_cardiobook;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AddEditMeasurement.OnFragmentInteractionListener {

    private ArrayList measurementDataList;
    private ListView measurementList;
    private ArrayAdapter<Measurement> measurementAdapter;
    private static final String cardio = "MeasurementRecord.sav";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveData();
        measurementList = findViewById(R.id.measurement_list);
        measurementAdapter = new CustomList(this,measurementDataList);
        measurementList.setAdapter(measurementAdapter);

        displayEmptyMessage();

        //Button for adding measurement, pops up alert dialog for new input
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddEditMeasurement().show(getSupportFragmentManager(), "ADD_MEASUREMENT");
            }
        });

        //OnClick method for ListView items, pop up alert dialog for edit action on previous input
        measurementList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long arg3) {
                Measurement measurement = (Measurement) adapter.getItemAtPosition(position);
                new AddEditMeasurement(measurement).show(getSupportFragmentManager(), "EDIT_MEASUREMENT");
            }
        });
    }

    //Retrieves data from the measurements ArrayList SharedPreferences
    //Stackoverflow post by Muhammad Maqsoodur Rehman https://stackoverflow.com/users/2355649/muhammad-maqsoodur-rehman Answer https://stackoverflow.com/questions/3624280/how-to-use-sharedpreferences-in-android-to-store-fetch-and-edit-values
    private void retrieveData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = prefs.getString(cardio, null);
        Type type = new TypeToken<ArrayList<Measurement>>() {}.getType();
        measurementDataList = gson.fromJson(json, type);
        if (measurementDataList == null){
            measurementDataList = new ArrayList<Measurement>();
        }
    }

    //Saves data from the measurements ArrayList SharedPreferences
    //source as above
    private void saveData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(measurementDataList);
        editor.putString(cardio, json);
        editor.apply();
    }

    // set empty message if measurement record is empty
    private void displayEmptyMessage(){
        TextView emptyMessage = findViewById(R.id.empty_message);
        if (measurementDataList.size() == 0 ) {
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            emptyMessage.setVisibility(View.INVISIBLE);
        }
    }

    //adds new input to data list, updates the adapter and saves the data
    //called in AddEditMeasurement
    @Override
    public void onOkPressed(Measurement newInput){
        measurementAdapter.add(newInput);
        displayEmptyMessage();
        saveData();
    }

    //removes measurement in data list, updates the adapter and saves the data
    //called in AddEditMeasurement
    public void onDeletePressed(Measurement measurement){
        measurementDataList.remove(measurement);
        measurementAdapter.notifyDataSetChanged();
        displayEmptyMessage();
        saveData();
    }

    //update measurement in data list, updates the adapter and saves the data
    //called in AddEditMeasurement
    public void onEditPressed() {
        measurementAdapter.notifyDataSetChanged();
        saveData();
    }

}
