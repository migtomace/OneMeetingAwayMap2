package com.example.lmnop.onemeetingawaymap1.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lmnop.onemeetingawaymap1.model.DataItemMeetings;

import java.util.ArrayList;

public class MeetingsTable {

    public static final String DB_FILE_NAME = "oneMeetingAway";
    public static final int DB_VERSION = 1;
    public static final String TABLE_ITEMS = "meetings";

    public static final String COLUMN_ID = "idNumber";
    public static final int COLUMN_ID_NUM= 0;

    public static final String COLUMN_DAY = "day";
    public static final int COLUMN_DAY_NUM=1;

    public static final String COLUMN_STARTTIME = "startTime";
    public static final int COLUMN_STARTTIME_NUM = 2;

    public static final String COLUMN_ENDTIME = "endTime";
    public static final int COLUMN_ENDTIME_NUM = 3;


    public static final String COLUMN_OC = "oc";
    public static final int COLUMN_OC_NUM = 4;


    public static final String COLUMN_MEETINGNAME = "meetingName";
    public static final int COLUMN_MEETINGNAME_NUM=5;


    public static final String COLUMN_LOCATION = "location";
    public static final int COLUMN_LOCATION_NUM=6;


    public static final String COLUMN_ADDRESS = "address";
    public static final int COLUMN_ADDRESS_NUM=7;


    public static final String COLUMN_CODES = "codes";
    public static final int COLUMN_CODES_NUM=8;

    public static final String COLUMN_LAT = "lat";
    public static final int COLUMN_LAT_NUM = 9;

    public static final String COLUMN_LNG = "lng";
    public static final int COLUMN_LNG_NUM = 10;


    public static final String[] ALL_COLUMNS =
            {
                    COLUMN_ID,
                    COLUMN_DAY,
                    COLUMN_STARTTIME,
                    COLUMN_ENDTIME,
                    COLUMN_OC,
                    COLUMN_MEETINGNAME,
                    COLUMN_LOCATION,
                    COLUMN_ADDRESS,
                    COLUMN_CODES,
                    COLUMN_LAT,
                    COLUMN_LNG
            };


    public static final String DATA_CREATE =

            "CREATE TABLE " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " TEXT PRIMARY KEY," +
                    COLUMN_DAY + " TEXT," +
                    COLUMN_STARTTIME + " TEXT," +
                    COLUMN_ENDTIME + " TEXT," +
                    COLUMN_OC + " TEXT," +
                    COLUMN_MEETINGNAME + " TEXT," +
                    COLUMN_LOCATION + " TEXT," +
                    COLUMN_ADDRESS + " TEXT," +
                    COLUMN_CODES + " TEXT," +
                    COLUMN_LAT + " TEXT," +
                    COLUMN_LNG + " TEXT" + ");";


    public static final String DATA_DELETE =
            "DROP TABLE " + TABLE_ITEMS;


    private static class DBHelper extends SQLiteOpenHelper {

        public static final String DB_FILE_NAME = "oneMeetingAway.db";
        public static final int DB_VERSION = 1;

        public DBHelper(Context context, String dbFileName, Object o, int dbVersion) {
            super(context, DB_FILE_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATA_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Data List", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(MeetingsTable.DATA_DELETE);

            onCreate(db);
        }

    }

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    public MeetingsTable(Context context){
        dbHelper = new DBHelper(context, DB_FILE_NAME, null, DB_VERSION);
    }

    private void openReadableDB(){
        db = dbHelper.getReadableDatabase();

    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();

    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    private void closeCursor(Cursor cursor){
        if(cursor!=null){
            cursor.close();
            closeCursor(cursor);
        }

    }

    //Gets all the meetings
    public ArrayList<DataItemMeetings> getMeetings(){
        ArrayList<DataItemMeetings> meetings = new ArrayList<DataItemMeetings>();
        openReadableDB();
        Cursor cursor = db.query(TABLE_ITEMS, null, null,
                null, null, null, null);

        while(cursor.moveToNext()){
            DataItemMeetings meeting = new DataItemMeetings();
            meeting.setIdNumber(cursor.getString(COLUMN_ID_NUM));
            meeting.setDay(cursor.getString(COLUMN_DAY_NUM));
            meeting.setStartTime(cursor.getString(COLUMN_STARTTIME_NUM));
            meeting.setEndTime(cursor.getString(COLUMN_ENDTIME_NUM));
            meeting.setOc(cursor.getString(COLUMN_OC_NUM));
            meeting.setMeetingName(cursor.getString(COLUMN_MEETINGNAME_NUM));
            meeting.setLocation(cursor.getString(COLUMN_LOCATION_NUM));
            meeting.setAddress(cursor.getString(COLUMN_ADDRESS_NUM));
            meeting.setCodes(cursor.getString(COLUMN_CODES_NUM));
            meeting.setLat(cursor.getString(COLUMN_LAT_NUM));
            meeting.setLng(cursor.getString(COLUMN_LNG_NUM));

            meetings.add(meeting);

        }
        closeCursor(cursor);
        closeDB();


        return meetings;
    }



    //Gets everything as long as the cursor can keep moving
    public static DataItemMeetings getMeetingsFromCursor(Cursor cursor){
        if(cursor == null||cursor.getCount()==0){
            return null;
        }else{
            try{
                DataItemMeetings meeting = new DataItemMeetings(
                        cursor.getString(COLUMN_ID_NUM),
                        cursor.getString(COLUMN_DAY_NUM),
                        cursor.getString(COLUMN_STARTTIME_NUM),
                        cursor.getString(COLUMN_ENDTIME_NUM),
                        cursor.getString(COLUMN_OC_NUM),
                        cursor.getString(COLUMN_MEETINGNAME_NUM),
                        cursor.getString(COLUMN_LOCATION_NUM),
                        cursor.getString(COLUMN_ADDRESS_NUM),
                        cursor.getString(COLUMN_CODES_NUM),
                        cursor.getString(COLUMN_LAT_NUM),
                        cursor.getString(COLUMN_LNG_NUM));
                return meeting;
            }catch(Exception e){
                return null;
            }
        }
    }


    //Gets the meetings from a specified day. This will be user input.
    public ArrayList<DataItemMeetings> getDay(String day){
        day="friday";
        String where = COLUMN_DAY + "= '" + day + "';";
        String[] whereArgs = {day};

        openReadableDB();
        Cursor cursor = db.query(TABLE_ITEMS, null, where, whereArgs,
                null, null, null);
        ArrayList<DataItemMeetings> meetings = new ArrayList<DataItemMeetings>();
        while (cursor.moveToNext()){
            meetings.add(getMeetingsFromCursor(cursor));

        }
        closeCursor(cursor);
        this.closeDB();
        return meetings;

    }




}
