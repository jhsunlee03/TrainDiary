package com.example.traintwo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TrainManager";
    private static final String TABLE_JOURNALS = "journals";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGEURI = "imageuri";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "date";

    public DataBaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOURNAL_TABLE = "CREATE TABLE " + TABLE_JOURNALS + "(" + KEY_ID + "INTEGER PRIMARY KEY,"
        + KEY_IMAGEURI + "TEXT," + KEY_TEXT + "TEXT," + KEY_DATE + "TEXT" + ")";
        db.execSQL(CREATE_JOURNAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNALS);
        //create table again
        onCreate(db);
    }

    //add new journal
    void addJournal(JournalEntry journal){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IMAGEURI, journal.getImagePath());
        values.put(KEY_TEXT, journal.getText());
        values.put(KEY_DATE, journal.getDate());
        //inserting row
        db.insert(TABLE_JOURNALS, null, values);
        db.close(); // close database connection
    }

    void deleteAll(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        //delete rows
        sqLiteDatabase.delete(TABLE_JOURNALS, null, null);
        sqLiteDatabase.close();
    }

    public int updateJournal(JournalEntry journal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGEURI, journal.getImagePath());
        values.put(KEY_TEXT, journal.getText());
        values.put(KEY_DATE, journal.getDate());
        //updating row
        return db.update(TABLE_JOURNALS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(journal.getId())});
    }

    public int updateJournalId(JournalEntry journal){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, journal.getId());
        values.put(KEY_IMAGEURI, journal.getImagePath());
        values.put(KEY_TEXT, journal.getText());
        values.put(KEY_DATE, journal.getDate());
        //updating row
        return db.update(TABLE_JOURNALS, values, KEY_ID + " = ?",
                new String[] {String.valueOf(journal.getId())});
    }

    public void deleteJournal(JournalEntry journal){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNALS, KEY_ID + " = ?", new String[] {String.valueOf(journal.getId())} );
        db.close();
    }

    //get journal count
    public int getJournalsCount(){
        String countQuery = "SELECT * FROM " + TABLE_JOURNALS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        //return count
        return count;
    }
    //code to get single journal
    JournalEntry getJournal(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOURNALS, new String[]{KEY_ID, KEY_IMAGEURI, KEY_TEXT}, KEY_ID + " =?", new String[] {String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        JournalEntry journal = new JournalEntry(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        //return journal
        return journal;
        }

    //code to get all journals for showing in a list view
    public ArrayList<JournalEntry> getAllJournals(){
        ArrayList<JournalEntry> journalList = new ArrayList<JournalEntry>();
        //select all query *
        String selectQuery = "SELECT * FROM " + TABLE_JOURNALS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and adding to list
        if(cursor.moveToFirst()) {
            do {
                JournalEntry journal = new JournalEntry();
                journal.setId(Integer.parseInt(cursor.getString(0)));
                journal.setImagePath(cursor.getString(1));
                journal.setText(cursor.getString(2));
                journal.setDate(cursor.getString(3));
                //add journal to list
                journalList.add(journal);
            }
            while (cursor.moveToNext());
        }
        return journalList;
    }

    public ArrayList<JournalEntry> getAllJournals(String date){
        ArrayList<JournalEntry> journalList = new ArrayList<JournalEntry>();
        //select all query *
        String selectQuery = "SELECT * FROM " + TABLE_JOURNALS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_JOURNALS, new String[]{
                KEY_ID, KEY_IMAGEURI, KEY_TEXT, KEY_DATE}, KEY_DATE + " LIKE ?",
                new String[] { "%" + date.substring(0,11) + "%"}, null, null, null, null);

        //loop through all rows and adding to list
        if(cursor.moveToFirst()) {
            do {
                JournalEntry journal = new JournalEntry();
                journal.setId(Integer.parseInt(cursor.getString(0)));
                journal.setImagePath(cursor.getString(1));
                journal.setText(cursor.getString(2));
                journal.setDate(cursor.getString(3));
                //add journal to list
                journalList.add(journal);
            }
            while (cursor.moveToNext());
        }
        return journalList;
    }




    //날짜만 가져올 수 있는 method
    public ArrayList<CalendarDay> getAllJournalDates(){
        ArrayList<CalendarDay> journalDates = new ArrayList<CalendarDay>();
        //일단 다 데이터 가져와서
        String selectQuery = "SELECT " +KEY_DATE + "FROM"+ TABLE_JOURNALS;

        SQLiteDatabase db = this.getWritableDatabase();
        //cursor에 담기
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loop through all rows and add to list
        if(cursor.moveToFirst()){
            do{
                //add dates to lsit
                String str= cursor.getString(0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                Date date = new Date();
                try{
                    date = sdf.parse(str);
                    journalDates.add(CalendarDay.from(date));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());//마지막꺼까
        }
        return journalDates;


    }







}
