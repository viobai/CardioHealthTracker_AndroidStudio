package com.example.tbai2_cardiobook;

public class Measurement {
    private String date;
    private String time;
    private int systolic;
    private int diastolic;
    private int heartRate;
    private String comment;

    //constructor
    public Measurement (String date, String time, int systolic, int diastolic, int heartRate, String comment){
        this.date = date;
        this.time = time;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.heartRate = heartRate;
        this.comment = comment;
    }

    //get measurement's date
    public String getDate(){ return date; }

    //get measurement's time
    public String getTime(){ return time; }

    //get measurement's systolic pressure
    public int getSystolic(){
        return systolic;
    }

    //get measurement's diastolic pressure
    public int getDiastolic(){
        return diastolic;
    }

    //get measurement's heart rate
    public int getHeartRate(){
        return heartRate;
    }

    //get measurement's comment
    public String getComment(){
        return comment;
    }

    //set measurement's date
    public void setDate(String date){ this.date = date; }

    //set measurement's time
    public void setTime(String time){
        this.time = time;
    }

    //set measurement's systolic pressure
    public void setSystolic(int systolic){
        this.systolic = systolic;
    }

    //set measurement's diastolic pressure
    public void setDiastolic(int diastolic){
        this.diastolic = diastolic;
    }

    //set measurement's heart rate
    public void setHeartRate(int heartRate){
        this.heartRate = heartRate;
    }

    //set measurement's comment
    public void setComment(String comment){
        this.comment = comment;
    }


}