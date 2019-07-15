package com.example.asimz.bucketlist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

public class PointsDbHelper extends SQLiteOpenHelper {
    public PointsDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("databasebanayannahi?","start");

        String createTable = "CREATE TABLE " + TaskContract.PointsEntry.TABLE1 + " ( " +
                TaskContract.PointsEntry.COL_POINTS_TP + " INTEGER DEFAULT 0)";
        try {
            db.execSQL(createTable);
            Log.d("databasebanayannahi?","done1111");
        }
        catch(Exception e){
            Log.d("databasebanayannahi?",e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.PointsEntry.TABLE1);
        onCreate(db);
    }
}
