package evia.huji.ac.il.ex4;

import android.provider.BaseColumns;

public class TableData {


    public TableData() {}

    static abstract class TableInfo implements BaseColumns {

        static final String TASK_NAME = "taskName";
        static final String TASK_ID = "taskID";
        static final String DATABASE_NAME = "tasksDatabase";
        static final String TABLE_NAME = "tasksTable";
    }
}
