package com.example.asimz.bucketlist.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.asimz.db";
    public static final int DB_VERSION = 30;

    public class PointsEntry implements  BaseColumns
        {
            public static final String TABLE1 = "points";
            public static final String COL_POINTS_TP = "totalPoints";
        }

    public class TaskEntry implements BaseColumns
        {
            public static final String TABLE = "tasks";
            public static final String COL_TASK_TITLE = "title";
        }
}
