package com.jblearning.helloandroid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper
{
  public static final String DATABASE_NAME="Weight.db";
  private static final int DATABASE_VERSION=1;
  public static final String TABLE_WEIGHT_ENTRIES="Weight_entries";

  //Columns
  public static final String COLUMN_ID="_id";
  public static final String COLUMN_WEIGHT="CUSTWEIGHT";
  public static final String COLUMN_DATE="WEIGHTDATE";
  public static final String COLUMN_IMAGEPATH = "ImagePath";

  private String TABLE_ACCOUNT_INFO = "ACCOUNT_INFO";
  //Create table SQL statement
  private static final String SQL_CREATE_ENTRIES="CREATE TABLE " +
          TABLE_WEIGHT_ENTRIES + "("+ COLUMN_ID +
          " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_WEIGHT +
          " REAL," + COLUMN_DATE + " TEXT, " +
          COLUMN_IMAGEPATH + " TEXT)";
  public DatabaseHelper(Context context)
  {
    super(context,DATABASE_NAME, null,DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db)
  {
    db.execSQL("CREATE TABLE " + TABLE_ACCOUNT_INFO + " (ACCOUNT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "START_WEIGHT REAL, GENDER TEXT, HEIGHT REAL, GOAL_DATE TEXT, GOAL_WEIGHT REAL)");
    db.execSQL(SQL_CREATE_ENTRIES);
  }
  @Override
  public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion)
  {
    db.execSQL("DROP TABLE IF EXISTS ACCOUNT_INFO");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT_ENTRIES);
    onCreate(db);
  }
  public boolean addWeightEntry(double weight, String date, String imagePath)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues contentValues = new ContentValues();
    contentValues.put(COLUMN_WEIGHT, weight);
    contentValues.put(COLUMN_DATE, date);
    contentValues.put(COLUMN_IMAGEPATH, imagePath);

    long result = db.insert(TABLE_WEIGHT_ENTRIES, null, contentValues);

    return result != -1;//Return true if insertion was successful
  }

  public List<WeightEntry> getAllWeights()
  {
    List<WeightEntry> weightList = new ArrayList<>();
    String selectQuery = "SELECT * FROM " + TABLE_WEIGHT_ENTRIES+ " ORDER BY " + COLUMN_DATE + " DESC";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
      do {
        WeightEntry weightEntry = new WeightEntry();
        weightEntry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        weightEntry.setWeight(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
        weightEntry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
        weightEntry.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEPATH)));
        weightList.add(weightEntry);
      } while (cursor.moveToNext());
    }
    cursor.close();
    db.close();
    return weightList;
  }

  public WeightEntry getLatestWeights()
  {
    WeightEntry weightEntry = new WeightEntry();
    String selectQuery = "SELECT * FROM " + TABLE_WEIGHT_ENTRIES+ " ORDER BY " + COLUMN_DATE + " DESC LIMIT 1";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst()) {
        weightEntry.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        weightEntry.setWeight(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)));
        weightEntry.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
        weightEntry.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGEPATH)));

    }
    cursor.close();
    db.close();

    return weightEntry;
  }

  public void deleteWeightEntry(long id) {
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_WEIGHT_ENTRIES, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    //return deletedRows > 0;
  }

  public boolean AddUpdateAccountInfo(String StartWeight, String Gender, double Height, String Goal_Date, double Goal_Weight)
  {
    SQLiteDatabase db = this.getWritableDatabase();

    ContentValues contentValues = new ContentValues();
    contentValues.put("START_WEIGHT", StartWeight);
    contentValues.put("GENDER", Gender);
    contentValues.put("HEIGHT", Height);
    contentValues.put("GOAL_DATE", Goal_Date);
    contentValues.put("GOAL_WEIGHT", Goal_Weight);

    long result = db.insert(TABLE_ACCOUNT_INFO, null, contentValues);

    return result != -1;//Return true if insertion was successful
  }

  public HashMap<String, String> GetAccountInfo()
  {
    HashMap<String, String> accountInfo = new HashMap<String, String>();
    String selectQuery = "SELECT START_WEIGHT, GENDER, HEIGHT, GOAL_DATE, GOAL_WEIGHT FROM " +
              " ACCOUNT_INFO LIMIT 1";
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);
    if (cursor.moveToFirst())
    {
      accountInfo.put("StartWeight",cursor.getString(cursor.getColumnIndexOrThrow("START_WEIGHT")));
      accountInfo.put("Gender",cursor.getString(cursor.getColumnIndexOrThrow("GENDER")));
      accountInfo.put("Height",cursor.getString(cursor.getColumnIndexOrThrow("HEIGHT")));
      accountInfo.put("GoalDate",cursor.getString(cursor.getColumnIndexOrThrow("GOAL_DATE")));
      accountInfo.put("GoalWeight",cursor.getString(cursor.getColumnIndexOrThrow("GOAL_WEIGHT")));
    }
    cursor.close();
    db.close();

    return accountInfo;
  }
}



