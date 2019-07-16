package com.example.asimz.bucketlist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.asimz.bucketlist.db.TaskContract;
import com.example.asimz.bucketlist.db.TaskDbHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static String CurrentDate = null ;
    private TaskDbHelper mHelper;

    private ListView mTaskListView;
    private TextView pointsDisplay;
    private ArrayAdapter<String> mAdapter;
    private InterstitialAd mInterstitialAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D2A6244586EA1860A8E41EB2798C2CC5").build());



        pointsDisplay = (TextView) findViewById(R.id.points);
        mTaskListView = (ListView) findViewById(R.id.list_todo);
        mHelper = new TaskDbHelper(this);
        //read name and last name
        SQLiteDatabase db1 = mHelper.getReadableDatabase();
        Cursor cursor1 = db1.query(TaskContract.PointsEntry.TABLE1,
                new String[] {TaskContract.PointsEntry.COL_FIRST_NAME},
                TaskContract.PointsEntry._ID+"= ?",
                new String[] {"1"},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToFirst();
        int nameindex = cursor1.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_FIRST_NAME);

        Log.d(TAG, "Points: " + cursor1.getString(nameindex));



        if(cursor1.getString(nameindex).equals("NAME")){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = this.getLayoutInflater();
            final View view = inflater.inflate(R.layout.namedialog, null, false);
            builder.setCancelable(false);
             final EditText fname = (EditText)view.findViewById(R.id.Fname);
            final EditText lname = (EditText)view.findViewById(R.id.Lname);


            builder.setView(inflater.inflate(R.layout.namedialog, null));

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
                            if(isEmpty(fname)){
                                Toast.makeText(MainActivity.this,"Please enter your name",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                dialog.dismiss();
                            }

                            break;


                    }
                }
            };

            builder.setPositiveButton("Save", dialogClickListener);




           final AlertDialog d= builder.create();

            d.setCanceledOnTouchOutside(false);
            d.show();
        }




        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while(cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            Log.d(TAG, "Task: " + cursor.getString(idx));
        }
        cursor.close();
        db.close();
        updateUI();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("D2A6244586EA1860A8E41EB2798C2CC5").build());
            }

        });
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent i =new Intent(MainActivity.this,addTasks.class);
                startActivity(i);
//                final EditText taskEditText = new EditText(this);
//                AlertDialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("Add a new task")
//                        .setMessage("What do you want to do next?")
//                        .setView(taskEditText)
//                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                String task = String.valueOf(taskEditText.getText());
//                                Log.d(TAG, "Task to add: " + task);
//                                SQLiteDatabase db = mHelper.getWritableDatabase();
//                                ContentValues values = new ContentValues();
//                                values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task);
//
//                                db.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
//                                        null,
//                                        values,
//                                        SQLiteDatabase.CONFLICT_REPLACE);
//
//                                db.close();
//
//
//                                updateUI();
//                                dialog.dismiss();
//                                }
//                        })
//                        .setNegativeButton("Cancel", null)
//                        .create();
//                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.TaskEntry.TABLE,
                new String[]{TaskContract.TaskEntry._ID, TaskContract.TaskEntry.COL_TASK_TITLE},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.item_todo,
                    R.id.task_title,
                    taskList);
            mTaskListView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();

        SQLiteDatabase db1 = mHelper.getReadableDatabase();
        Cursor cursor1 = db1.query(TaskContract.PointsEntry.TABLE1,
                new String[] {TaskContract.PointsEntry.COL_POINTS_TP},
                TaskContract.PointsEntry._ID+"= ?",
                new String[] {"1"},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToFirst();
        int idx = cursor1.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

        Log.d(TAG, "Points: " + cursor1.getString(idx));

        int Points = Integer.parseInt(cursor1.getString(idx));
        pointsDisplay.setText("Total Points :"+Integer.toString(Points));
        db1.close();
    }

    public void newFunc(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
        String task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db2 = mHelper.getReadableDatabase();
        Cursor cursor1 = db2.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry.COL_TASK_POINT,TaskContract.TaskEntry.COL_TASK_DESC,TaskContract.TaskEntry.COL_TASK_TITLE},
                TaskContract.TaskEntry.COL_TASK_TITLE+"= ?",
                new String[] {task},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToNext();
        int titl = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_TITLE);
        Log.d(TAG, "DATA: " + cursor1.getString(titl));
        int des = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_DESC);
        Log.d(TAG, "DATA: " + cursor1.getString(des));
        int pnts = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_POINT);
        Log.d(TAG, "DATA: " + cursor1.getString(pnts));
        Intent intent = new Intent(MainActivity.this, taskdata.class);
        intent.putExtra("title",  cursor1.getString(titl));
        intent.putExtra("desc",cursor1.getString(des));
        intent.putExtra("points",cursor1.getString(pnts));
        intent.putExtra("task",task);
        startActivityForResult(intent,1);

    }

    public void doneTask(View view) {
        if (mInterstitialAd.isLoaded()) {

            SQLiteDatabase db = mHelper.getReadableDatabase();
            Cursor cursor = db.query(TaskContract.PointsEntry.TABLE1,
                    new String[]{TaskContract.PointsEntry.COL_POINTS_TP},
                    TaskContract.PointsEntry._ID + "= ?",
                    new String[]{"1"},
                    null,
                    null,
                    null);

            if (cursor != null)
                cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

            Log.d(TAG, "Points: " + cursor.getString(idx));
            int Points = Integer.parseInt(cursor.getString(idx));

            db.close();


            View parent = (View) view.getParent();
            TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
            String task = String.valueOf(taskTextView.getText());
            SQLiteDatabase db2 = mHelper.getReadableDatabase();
            Cursor cursor1 = db2.query(TaskContract.TaskEntry.TABLE,
                    new String[]{TaskContract.TaskEntry.COL_TASK_POINT},
                    TaskContract.TaskEntry.COL_TASK_TITLE + "= ?",
                    new String[]{task},
                    null,
                    null,
                    null);

            if (cursor1 != null)
                cursor1.moveToNext();
            int idx1 = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_POINT);
            Log.d(TAG, "Points: " + cursor.getString(idx1));
            int totalPoints = Integer.parseInt(cursor1.getString(idx)) + Points;
            db2.close();


            SQLiteDatabase db1 = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.PointsEntry.COL_POINTS_TP, Integer.toString(totalPoints));
            db1.update(TaskContract.PointsEntry.TABLE1, values, TaskContract.PointsEntry._ID + "= ?", new String[]{"1"});
            db1.close();

            SQLiteDatabase db3 = mHelper.getWritableDatabase();
            db3.delete(TaskContract.TaskEntry.TABLE,
                    TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                    new String[]{task});
            db3.close();

            updateUI();
            mInterstitialAd.show();
        } else {

            SQLiteDatabase db = mHelper.getReadableDatabase();
            Cursor cursor = db.query(TaskContract.PointsEntry.TABLE1,
                    new String[]{TaskContract.PointsEntry.COL_POINTS_TP},
                    TaskContract.PointsEntry._ID + "= ?",
                    new String[]{"1"},
                    null,
                    null,
                    null);

            if (cursor != null)
                cursor.moveToFirst();
            int idx = cursor.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

            Log.d(TAG, "Points: " + cursor.getString(idx));
            int Points = Integer.parseInt(cursor.getString(idx));

            db.close();


            View parent = (View) view.getParent();
            TextView taskTextView = (TextView) parent.findViewById(R.id.task_title);
            String task = String.valueOf(taskTextView.getText());
            SQLiteDatabase db2 = mHelper.getReadableDatabase();
            Cursor cursor1 = db2.query(TaskContract.TaskEntry.TABLE,
                    new String[]{TaskContract.TaskEntry.COL_TASK_POINT},
                    TaskContract.TaskEntry.COL_TASK_TITLE + "= ?",
                    new String[]{task},
                    null,
                    null,
                    null);

            if (cursor1 != null)
                cursor1.moveToNext();
            int idx1 = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_POINT);
            Log.d(TAG, "Points: " + cursor.getString(idx1));
            int totalPoints = Integer.parseInt(cursor1.getString(idx)) + Points;
            db2.close();

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
            CurrentDate = mdformat.format(calendar.getTime());
            Log.d("Date : ",CurrentDate);

            SQLiteDatabase detailsDB = mHelper.getWritableDatabase();
            ContentValues pvalues = new ContentValues();
            pvalues.put(TaskContract.PointsDetails.COL_POINTS, Integer.toString(Points));
            pvalues.put(TaskContract.PointsDetails.COL_DATE, CurrentDate.toString());
            detailsDB.insertWithOnConflict(TaskContract.PointsDetails.tbName,
                    null,
                    pvalues,
                    SQLiteDatabase.CONFLICT_REPLACE);

            detailsDB.close();

            SQLiteDatabase db1 = mHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TaskContract.PointsEntry.COL_POINTS_TP, Integer.toString(totalPoints));
            db1.update(TaskContract.PointsEntry.TABLE1, values, TaskContract.PointsEntry._ID + "= ?", new String[]{"1"});
            db1.close();

            SQLiteDatabase db3 = mHelper.getWritableDatabase();
            db3.delete(TaskContract.TaskEntry.TABLE,
                    TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                    new String[]{task});
            db3.close();

            updateUI();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        Log.d("HERE: ",Integer.toString(requestCode));
        if (requestCode == 1) {
            // Make sure the request was successful
            Log.d("HERE: ",Integer.toString(resultCode));
            if (resultCode == RESULT_OK) {
                Log.d("HERE: ","HERE");
                String message=data.getStringExtra("method");
                deleteTask(message);
            }
        }
    }
    public void deleteTask(String task){
        Log.d("HERE: ","HERE");
        mHelper = new TaskDbHelper(this);
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskContract.PointsEntry.TABLE1,
                new String[] {TaskContract.PointsEntry.COL_POINTS_TP},
                TaskContract.PointsEntry._ID+"= ?",
                new String[] {"1"},
                null,
                null,
                null);

        if (cursor != null)
            cursor.moveToFirst();
        int idx = cursor.getColumnIndexOrThrow(TaskContract.PointsEntry.COL_POINTS_TP);

        Log.d(TAG, "Points: " + cursor.getString(idx));
        int Points = Integer.parseInt(cursor.getString(idx));

        db.close();





        SQLiteDatabase db2 = mHelper.getReadableDatabase();
        Cursor cursor1 = db2.query(TaskContract.TaskEntry.TABLE,
                new String[] {TaskContract.TaskEntry.COL_TASK_POINT},
                TaskContract.TaskEntry.COL_TASK_TITLE+"= ?",
                new String[] {task},
                null,
                null,
                null);

        if (cursor1 != null)
            cursor1.moveToNext();
        int idx1 = cursor1.getColumnIndexOrThrow(TaskContract.TaskEntry.COL_TASK_POINT);
        Log.d(TAG, "Points: " + cursor.getString(idx1));
        int totalPoints = Integer.parseInt(cursor1.getString(idx)) + Points;
        db2.close();

//cut this from onCreate and paste it here above query in done task
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd ");
        CurrentDate = mdformat.format(calendar.getTime());
        Log.d("Date : ",CurrentDate);

        SQLiteDatabase detailsDB = mHelper.getWritableDatabase();
        ContentValues pvalues = new ContentValues();
        pvalues.put(TaskContract.PointsDetails.COL_POINTS, Integer.toString(Points));
        pvalues.put(TaskContract.PointsDetails.COL_DATE, CurrentDate.toString());
        detailsDB.insertWithOnConflict(TaskContract.PointsDetails.tbName,
                null,
                pvalues,
                SQLiteDatabase.CONFLICT_REPLACE);

        detailsDB.close();

        SQLiteDatabase db1 = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.PointsEntry.COL_POINTS_TP, Integer.toString(totalPoints));
        db1.update(TaskContract.PointsEntry.TABLE1,values,TaskContract.PointsEntry._ID +  "= ?", new String[] {"1"});
        db1.close();

        SQLiteDatabase db3 = mHelper.getWritableDatabase();
        db3.delete(TaskContract.TaskEntry.TABLE,
                TaskContract.TaskEntry.COL_TASK_TITLE + " = ?",
                new String[]{task});
        db3.close();

        updateUI();



    }

}
