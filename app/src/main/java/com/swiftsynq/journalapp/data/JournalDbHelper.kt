package com.swiftsynq.journalapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.swiftsynq.journalapp.R
import com.swiftsynq.journalapp.data.DatabaseContract.DiaryColumns

class JournalDbHelper(private val mContext: Context) : SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_TASKS)
        loadDemoTask(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_DIARIES)
        onCreate(db)
    }

    private fun loadDemoTask(db: SQLiteDatabase) {
        val values = ContentValues()
        values.put(DiaryColumns.DESCRIPTION, mContext.resources.getString(R.string.demo_diary))
        values.put(DiaryColumns.TITLE, mContext.resources.getString(R.string.app_name))
        values.put(DiaryColumns.IS_FAVOURITE, 0)
        values.put(DiaryColumns.DIARY_DATE, java.lang.Long.MAX_VALUE)
        db.insertOrThrow(DatabaseContract.TABLE_DIARIES, null, values)
    }

    companion object {

        private val DATABASE_NAME = "diaries.db"
        private val DATABASE_VERSION = 1

        private val SQL_CREATE_TABLE_TASKS = String.format("CREATE TABLE %s" + " (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s INTEGER, %s INTEGER)",
                DatabaseContract.TABLE_DIARIES,
                DiaryColumns._ID,
                DiaryColumns.DESCRIPTION,
                DiaryColumns.TITLE,
                DiaryColumns.IS_FAVOURITE,
                DiaryColumns.DIARY_DATE
        )
    }
}
