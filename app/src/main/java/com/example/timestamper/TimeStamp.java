package com.example.timestamper;

import android.database.DatabaseUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeStamp {

    //    constructor

    public  TimeStamp(Date currentDate){
        dateFormat = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
        creationDate = currentDate;
        creationDateString = dateFormat.format(creationDate);
        this.timeLimitDate = currentDate;
    }

    public TimeStamp(Date currentDate, Date timeLimitDate){
        this(currentDate);
        this.isCountDownTimer = true;
        this.timeLimitDate = timeLimitDate;
    }

    //  fields ****************************************

    private long id;    // ArrayAdapter内から参照される変数
    private SimpleDateFormat dateFormat;
    private String creationDateString;
    private Date timeLimitDate;
    private Boolean isCountDownTimer = false;
    private String comment = "";
    private Date creationDate;

    //  getter , setter ****************************************

    public Date getCreationDate(){ return (Date)this.creationDate.clone(); }
    public String getCreationDateString(){return this.creationDateString; }
    public Date getTimeLimitDate(){ return (Date)this.timeLimitDate.clone(); }
    public Boolean IsCountDownTimer(){ return isCountDownTimer; }
    public String getComment(){ return comment; }
    public long getId(){ return id; }

    public void setId(long id){ this.id = id; }
    public void setComment(String comment){ this.comment = comment; }

    // methods ****************************************

    public String getTime(){
        if(isCountDownTimer){
            long remainingTime = timeLimitDate.getTime() - new Date().getTime();
            if(remainingTime < 0) return "00 : 00 : 00";
            return convertMilliSecondsToTimeString(remainingTime);
        }

        Date currentDate = new Date();
        long elapsedTime = currentDate.getTime() - creationDate.getTime();
        return convertMilliSecondsToTimeString(elapsedTime);
    }

    private String convertMilliSecondsToTimeString(long milliSeconds){
        long sec = milliSeconds / 1000;
        int h = (int)Math.floor( sec/ 60 / 60);
        int m = (int)Math.floor((sec / 60) % 60);
        int s = (int)Math.floor( sec % 60);

        String t = "";
        if(h < 10) t += "0" + String.valueOf(h);
        else t += String.valueOf(h);

        t += " : ";

        if(m < 10) t += "0" + String.valueOf(m);
        else  t += String.valueOf(m);

        t += " : ";

        if(s < 10) t += "0" + String.valueOf(s);
        else t += String.valueOf(s);

        return t;
    }
}
