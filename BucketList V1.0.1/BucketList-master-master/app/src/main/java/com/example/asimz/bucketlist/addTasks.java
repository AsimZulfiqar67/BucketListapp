package com.example.asimz.bucketlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.asimz.bucketlist.db.TaskContract;
import com.example.asimz.bucketlist.db.TaskDbHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class addTasks extends AppCompatActivity {

    public EditText titles,desc,points;
    String txt_title,txt_desc,txt_points;
    TaskDbHelper mHelper;
    Button addTask;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D2A6244586EA1860A8E41EB2798C2CC5").build());

        titles =(EditText) findViewById(R.id.titleEditText);
        desc =(EditText) findViewById(R.id.descEditText);
        points =(EditText) findViewById(R.id.points);
        mHelper = new TaskDbHelper(this);
        addTask =(Button) findViewById(R.id.addTask);



        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_title = titles.getText().toString();
                txt_desc = desc.getText().toString();
                txt_points = points.getText().toString();
                addTask();
            }
        });
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D2A6244586EA1860A8E41EB2798C2CC5").build());
            }

        });




    }

    public void addTask(){
        if (mInterstitialAd.isLoaded()) {


            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, txt_title);
            values.put(TaskContract.TaskEntry.COL_TASK_DESC, txt_desc);
            values.put(TaskContract.TaskEntry.COL_TASK_POINT, txt_points);
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            db.close();
            Intent i = new Intent(addTasks.this, MainActivity.class);
            mInterstitialAd.show();
            startActivity(i);
        }
        else{
            SQLiteDatabase db = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.TaskEntry.COL_TASK_TITLE, txt_title);
            values.put(TaskContract.TaskEntry.COL_TASK_DESC, txt_desc);
            values.put(TaskContract.TaskEntry.COL_TASK_POINT, txt_points);
            db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);

            db.close();
            Intent i = new Intent(addTasks.this, MainActivity.class);

            startActivity(i);

        }
    }
    public void difficultyDialog(View view){
        AlertDialog.Builder yourDialog = new AlertDialog.Builder(this);
        View layout =  getLayoutInflater().inflate(R.layout.difficulty, null);
        yourDialog.setView(layout);
        yourDialog.show();
    }
}
