package com.swiftsynq.journalapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.swiftsynq.journalapp.R;
import com.swiftsynq.journalapp.data.DatabaseContract.DiaryColumns;
public class JournalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "diaries.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_TASKS = String.format("CREATE TABLE %s"
            +" (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s INTEGER)",
            DatabaseContract.TABLE_DIARIES,
            DiaryColumns._ID,
            DiaryColumns.DESCRIPTION,
            DiaryColumns.IS_FAVOURITE,
            DiaryColumns.DIARY_DATE
    );

    private final Context mContext;

    public JournalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_TASKS);
        loadDemoTask(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_DIARIES);
        onCreate(db);
    }

    private void loadDemoTask(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(DiaryColumns.DESCRIPTION, mContext.getResources().getString(R.string.demo_diary));
        values.put(DiaryColumns.IS_FAVOURITE, 0);
        values.put(DiaryColumns.DIARY_DATE, Long.MAX_VALUE);

        db.insertOrThrow(DatabaseContract.TABLE_DIARIES, null, values);
    }
}
