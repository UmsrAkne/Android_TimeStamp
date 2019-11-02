package com.example.timestamper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeStampSQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TestDB.db";

    public static final String TABLE_NAME = "testdb";
    public static final String _ID = "_id";

    public static final String COLUMN_NAME_SERIAL_NUMBER = "serialNumber";
    public static final String COLUMN_NAME_CREATION_DATE = "creationDate";
    public static final String COLUMN_NAME_SETTING_DATE = "settingDate";
    public static final String COLUMN_NAME_COMMENT = "comment";
    public static final String COLUMN_NAME_IS_COUNTDOWN = "isCountDown";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SERIAL_NUMBER + " INTEGER," +
                    COLUMN_NAME_CREATION_DATE + " INTEGER," +
                    COLUMN_NAME_SETTING_DATE + " INTEGER," +
                    COLUMN_NAME_COMMENT + " STRING,"  +
                    COLUMN_NAME_IS_COUNTDOWN + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    TimeStampSQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * データベースが作成されたタイミングで呼び出されます
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.trace("databse creation--------------------------------------------------");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    /**
     * データベースをバージョンアップしたタイミングで呼び出されます。
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

    public List<TimeStamp> getData(){
        SQLiteDatabase readableDatabase = this.getReadableDatabase();

        String order_by = COLUMN_NAME_CREATION_DATE + " DESC";
        Cursor cursor = readableDatabase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                order_by
        );

        int creationDateIndex = cursor.getColumnIndex(COLUMN_NAME_CREATION_DATE);
        int settingDateIndex = cursor.getColumnIndex(COLUMN_NAME_SETTING_DATE);
        int isCountDownIndex = cursor.getColumnIndex(COLUMN_NAME_IS_COUNTDOWN);
        int serialNumber = cursor.getColumnIndex(COLUMN_NAME_SERIAL_NUMBER);

        List<TimeStamp> timeStampList = new ArrayList<TimeStamp>();

        for(int i = 0; i < 5; i++){
            if(!cursor.moveToNext()) break;

            if(cursor.getInt(isCountDownIndex) == 0){
                Date creationDate = new Date(cursor.getLong(creationDateIndex));
                TimeStamp timeStamp = new TimeStamp(creationDate);
                timeStampList.add(0,timeStamp);
            }else{
                Date creationDate = new Date(cursor.getLong(creationDateIndex));
                Date limitTimeDate = new Date(cursor.getLong(settingDateIndex));
                TimeStamp timeStamp = new TimeStamp(creationDate,limitTimeDate);
                timeStampList.add(0,timeStamp);
            }
        }

        return timeStampList;
    }

    public void insertData(SQLiteDatabase db, TimeStamp ts){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CREATION_DATE, ts.getCreationDate().getTime());
        contentValues.put(COLUMN_NAME_SETTING_DATE, ts.getTimeLimitDate().getTime());
        contentValues.put(COLUMN_NAME_COMMENT, ts.getComment());
        int b = 0;
        if(ts.IsCountDownTimer()) b = 1;

        contentValues.put(COLUMN_NAME_IS_COUNTDOWN, b);

        long databaseRecordCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        contentValues.put(COLUMN_NAME_SERIAL_NUMBER,databaseRecordCount);

        db.insert(TABLE_NAME,null,contentValues);
    }

    public void delete(int index){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_NAME_SERIAL_NUMBER + " = " + String.valueOf(index) , null);
    }
}
