package com.example.timestamper;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<TimeStamp> timeStamps = new ArrayList<>();
    private MyAdapter myAdapter;

    private TimeStampSQLiteHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();
        //  レイアウトファイルに用意したリストビューに ArrayAdapter をセットする。
        final ListView tsList = findViewById(R.id.tsList);

        myAdapter = new MyAdapter(this);
        myAdapter.setTimeStampList(timeStamps);
        tsList.setAdapter(myAdapter);

        restoreTimeStampList();
        findViewById(R.id.timeStampButton).setOnClickListener(this);
        findViewById(R.id.timeStampButton_2).setOnClickListener(this);

        final Handler mHandler = new Handler();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        },0,1000);
    }

    @Override
    public void onClick(View v) {
        if(v != null){
            switch (v.getId()){
                case R.id.timeStampButton:
                    addTimeStamp(new TimeStamp(new Date()));
                    break;

                case R.id.timeStampButton_2:
                    NumPicDialogFragment dialog = new NumPicDialogFragment();
                    dialog.show(getSupportFragmentManager(),"sp");
                    break;
            }
        }
    }

    public void addTimeStamp(TimeStamp ts){
        helper.insertData(helper.getWritableDatabase(),ts);
        timeStamps.add(0,ts);
        myAdapter.notifyDataSetChanged();
    }

    private void createDatabase(){
        if(helper == null){
            helper = new TimeStampSQLiteHelper(getApplicationContext());
        }

        if(db == null){
            db = helper.getWritableDatabase();
        }
    }

    private void restoreTimeStampList(){
        List<TimeStamp> timeStampList = helper.getData();
        for(int i = timeStampList.size() -1; i >= 0; i--){
            timeStamps.add(timeStampList.get(i));
        }

        myAdapter.notifyDataSetChanged();
    }
}
