package com.example.asimz.bucketlist;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class taskdata extends AppCompatActivity {
    public TextView ti,de,po;
    String txt_title,txt_desc,txt_points,taskName;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = new MainActivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskdata);
        ti =(TextView) findViewById(R.id.Tasktitl);
        de =(TextView) findViewById(R.id.Taskdes);
        po =(TextView) findViewById(R.id.Taskpnt);
        Intent intent = getIntent();
        txt_title = intent.getStringExtra("title");
        txt_desc=intent.getStringExtra("desc");
        txt_points=intent.getStringExtra("points");
        taskName = intent.getStringExtra("task");
        ti.setText(txt_title);
        de.setText(txt_desc);
        po.setText(txt_points);
    }
    public void donetask2(View view){

        Intent intent = new Intent();
        intent.putExtra("method",taskName);
        setResult(Activity.RESULT_OK,intent);
        finish();



    }
}
