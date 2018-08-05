package com.bawaaba.rninja4.rookie.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

/* Created by rninja4 on 4/4/17. */


public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "android_api";
    private static final String TABLE_USER = "user";
//    private static final String TABLE_CREATE_PROFILE = "create_profile";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_VERIFY_CODE = "verify_code";
    private static final String KEY_PASSWORD = "password";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_UID + " TEXT,"
                + KEY_TOKEN +" TEXT," + KEY_VERIFY_CODE + " TEXT," + KEY_PASSWORD + " TEXT)" ;

        db.execSQL(CREATE_LOGIN_TABLE);

        Log.e(TAG, "Database tables created"+CREATE_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);

    }

    public boolean addUser(String name, String email, String uid, String token,String verify_code,String password ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_UID, uid); // Email
        values.put(KEY_TOKEN, token);// token
        values.put(KEY_VERIFY_CODE, verify_code);// token
        values.put(KEY_PASSWORD, password);// password

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.e(TAG, "New user inserted into sqlite: " + id);
        return true;
    }

//    public boolean create_profile(String fname, String lname, String email, String phone, String password, String dob, String gender) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_FIRSTNAME,fname);
//        values.put(KEY_LASTNAME, lname);
//        values.put(KEY_EMAIL, email); // Email
//        values.put(KEY_PHONE,phone);
//        values.put(KEY_PASSWORD, password);
//        values.put(KEY_DOB, dob);
//        values.put(KEY_GENDER, gender);
//
//        db.execSQL("DELETE FROM " + TABLE_CREATE_PROFILE);
//
//        String CREATE_PROFILE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CREATE_PROFILE +
//                "("+ KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT," + KEY_EMAIL +
//                " TEXT," + KEY_PHONE + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_DOB +
//                " TEXT," + KEY_GENDER + " TEXT )";
//         db.execSQL(CREATE_PROFILE_TABLE);
//
//        // Inserting Row
//        long id = db.insert(TABLE_CREATE_PROFILE, null, values);
//        db.close(); // Closing database connection
//
//        Log.e(TAG, "New user inserted into sqlite: " + id);
//        return true;
//    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("token", cursor.getString(4));
            user.put("verify_code", cursor.getString(5));
            user.put("password", cursor.getString(6));
        }
        cursor.close();
        db.close();

        Log.e(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


//    public boolean checkuser(String email) {
//
//        String selectQuery = "SELECT  * FROM " + TABLE_USER  + " WHERE email = " + "'" + email +"'";
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        // Move to first row
//        cursor.moveToFirst();
//        int cnt = cursor.getCount();
//        if (cnt == 1) {
//            return true;
//        }else{
//            return false;
//        }
//    }

    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.e(TAG, "Deleted all user info from sqlite");
    }
}