package com.example.asimz.bucketlist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.asimz.bucketlist.MainActivity;

public class TaskDbHelper extends SQLiteOpenHelper {
    private MainActivity mainActivity;

    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TaskContract.TaskEntry.TABLE + " ( " +
                TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TaskContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL , "+
                TaskContract.TaskEntry.COL_TASK_DESC +" TEXT ," +
                TaskContract.TaskEntry.COL_TASK_POINT +" INTEGER );";

        String createTable1 = "CREATE TABLE " + TaskContract.PointsEntry.TABLE1 + " ( " +
                TaskContract.PointsEntry._ID + " INTEGER , " +
                TaskContract.PointsEntry.COL_POINTS_TP + " TEXT NOT NULL );";
        try {
            db.execSQL(createTable);
            Log.d("databasebanayannahi?","done1111");
        }
        catch(Exception e){
            Log.d("databasebanayannahi?",e.toString());
        }
        try {
            db.execSQL(createTable1);
            StartValue(db);

            Log.d("databasebanayannahi?","done");
        }
        catch(Exception e){
            Log.d("databasebanayannahi?",e.toString());
        }}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TABLE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.PointsEntry.TABLE1);
        onCreate(db);
    }
    public void StartValue(SQLiteDatabase db1){

        ContentValues values = new ContentValues();
        values.put(TaskContract.PointsEntry._ID,1);
        values.put(TaskContract.PointsEntry.COL_POINTS_TP, "0");

        db1.insert(TaskContract.PointsEntry.TABLE1,
                null,
                values);


    }

}
