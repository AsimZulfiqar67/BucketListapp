package com.example.asimz.bucketlist.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.asimz.db";
    public static final int DB_VERSION = 01;

    public class PointsEntry implements  BaseColumns
        {
            public static final String TABLE1 = "points";
            public static final String COL_FIRST_NAME = "firstname";
            public static final String COL_LAST_NAME = "lastname";
            public static final String COL_POINTS_TP = "totalPoints";

        }

    public class TaskEntry implements BaseColumns
        {
            public static final String TABLE = "tasks";
            public static final String COL_TASK_TITLE = "title";
            public static final String COL_TASK_DESC = "description";
            public static final String COL_TASK_POINT = "point";
        }
    public class PointsDetails implements BaseColumns
    {
        public static final String tbName = "PointsDetail";
        public static final String COL_POINTS= "pointsCounts";
        public static final String COL_DATE = "date";
    }
}
