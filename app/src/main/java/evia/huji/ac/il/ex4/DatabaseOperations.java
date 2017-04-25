package evia.huji.ac.il.ex4;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DatabaseOperations extends SQLiteOpenHelper {

    private static final int database_version = 1;

    DatabaseOperations(Context context) {
        super(context, TableData.TableInfo.DATABASE_NAME, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        String CREATE_QUERY = "CREATE TABLE tasksTable(taskName TEXT, taskID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL);";
        sdb.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    void putInformation(DatabaseOperations dop, String taskName){

        SQLiteDatabase SQ = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableData.TableInfo.TASK_NAME, taskName);

        SQ.insert(TableData.TableInfo.TABLE_NAME, null, cv);
    }

    Cursor getInformation(DatabaseOperations dop) {
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {TableData.TableInfo.TASK_NAME, TableData.TableInfo.TASK_ID};

        return SQ.query(TableData.TableInfo.TABLE_NAME, columns, null, null, null, null, null);
    }

    void deleteTask(DatabaseOperations dop, int taskID) {
        String selection = TableData.TableInfo.TASK_ID + " LIKE ? ";
        String args[] = {String.valueOf(taskID)};
        SQLiteDatabase SQ = dop.getWritableDatabase();
        SQ.delete(TableData.TableInfo.TABLE_NAME, selection, args);
    }
}