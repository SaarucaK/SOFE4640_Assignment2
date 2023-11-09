package com.example.mobileassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "assignment2.db";
    private static final String TABLE_NAME = "location";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_ADDRESS = "address";


    //database setup
    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }
    //create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_LATITUDE + " DOUBLE," + COLUMN_LONGITUDE + " DOUBLE," + COLUMN_ADDRESS + " TEXT" +")" ;
        db.execSQL(CREATE_NOTES_TABLE);
    }
    //upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //check if table empty
    public boolean isEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_NAME; //count rows in table
        Cursor cursor = db.rawQuery(count, null); //get cursor for query
        cursor.moveToFirst();
        int counter = cursor.getInt(0); //get values of row counter
        db.close();
        //return true if empty
        if(counter<=0)
            return true;
        else
            return false;
    }

    //get id given lat and lon input
    public int getID(double latitude, double longitude){
        SQLiteDatabase db = this.getWritableDatabase();
        //query search lat and lon in table, find col id
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_LATITUDE + " = '" + Double.toString(latitude) + "AND" + COLUMN_LONGITUDE + " = '" + Double.toString(longitude) + "'";
        Cursor cursor = db.rawQuery(query, null); //retrieve cursor for query
        int id = -1;
        //get id column value for cursor row
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            id = cursor.getInt(0); //id column value
        }
        else{
            id = -1;
        }
        return id;
    }

    //use lat, lon and address to find id
    public int getIdByAll(double latitude, double longitude, String address){
        SQLiteDatabase db = this.getWritableDatabase();
        //query search lat, lon and address in table, find col id
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_LATITUDE + " = '" + Double.toString(latitude) + "AND" + COLUMN_LONGITUDE + " = '" + Double.toString(longitude) + "AND" + COLUMN_ADDRESS + " = '" + address + "'";
        Cursor cursor = db.rawQuery(query, null); //retrieve cursor for query
        //get id column value for cursor row
        int id = -1; //set negative default ie entry not found
        //if entry found retrive id
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            id = cursor.getInt(0); //id column value
        }
        //entry not found
        else{
            id = -1;
        }
        return id;
    }

    //add entry to location table
    public void addLocation(String address, double latitude, double longitude){
        //get values for address, lat and long
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        //insert to database
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    //update existing record
    public void updateLocation(int id, String address, double latitude, double longitude){
        //get address, lat and long
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        //complete database operation, update at the column id
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME,values,COLUMN_ID+" =?",new String []{String.valueOf(id)});
        db.close();
    }

    //delete existing record
    public boolean deleteLocation(int id){
        //complete database operation, delete at column id
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String []{String.valueOf(id)});
        // Return true if at least one row was deleted
        return rowsDeleted > 0;
    }

    //get latitude
    public String getLatitude(String address){
        SQLiteDatabase db = this.getReadableDatabase();
        //query get latitude where address found
        String query = "SELECT " + COLUMN_LATITUDE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ADDRESS + " = '" + address + "'";
        Cursor cursor = db.rawQuery(query, null); //get entry where query success
        String lat = null;
        //retrieve latitude value in row
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            lat = cursor.getString(2); //column index 2 is latitude
        }
        //if address not found in table, return message
        else{
            lat = "Not Found";
        }
        db.close();
        return lat;
    }

    //get longitude
    public String getLongitude(String address){
        SQLiteDatabase db = this.getReadableDatabase();
        //query get latitude where address found
        String query = "SELECT " + COLUMN_LONGITUDE + " FROM " + TABLE_NAME +
                " WHERE " + COLUMN_ADDRESS + " = '" + address + "'";
        Cursor cursor = db.rawQuery(query, null);
        String lon = null;
        //retrieve longitude value in row
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            lon = cursor.getString(3); //column index 3 is longitude
        }
        //if address not found in table, return message
        else{
            lon = "Not Found";
        }
        db.close();
        return lon;
    }

}