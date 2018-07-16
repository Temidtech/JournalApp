package com.swiftsynq.journalapp.data

import android.database.Cursor

import com.swiftsynq.journalapp.data.DatabaseContract.getColumnInt
import com.swiftsynq.journalapp.data.DatabaseContract.getColumnLong
import com.swiftsynq.journalapp.data.DatabaseContract.getColumnString

/**
 * Helpful data model for holding attributes related to a diary.
 */
class DiaryEntry {

    //Unique identifier in database
    var id: Long = 0
    //Diary description
    val description: String
    //Title
    val title: String
    //Marked if entry is favourite
    val isFavourite: Boolean
    //Optional diary date for the entry
    val diaryDateMillis: Long
    /**
     * Create a new Diary entry from discrete items
     */
    @JvmOverloads constructor(description: String, isFavourite: Boolean, diaryDateMillis: Long = NO_DATE,title:String) {
        this.id = NO_ID //Not set
        this.description = description
        this.title=title
        this.isFavourite = isFavourite
        this.diaryDateMillis = diaryDateMillis
    }
    /**
     * Create a new diary entry from a database Cursor
     */
    constructor(cursor: Cursor) {
        this.id = getColumnLong(cursor, DatabaseContract.DiaryColumns._ID)
        this.description = getColumnString(cursor, DatabaseContract.DiaryColumns.DESCRIPTION)
        this.title = getColumnString(cursor, DatabaseContract.DiaryColumns.TITLE)
        this.isFavourite = getColumnInt(cursor, DatabaseContract.DiaryColumns.IS_FAVOURITE) == 1
        this.diaryDateMillis = getColumnLong(cursor, DatabaseContract.DiaryColumns.DIARY_DATE)
    }
    /**
     * Return true if a diary date has been set on this entry.
     */
    fun hasDueDate(): Boolean {
        return this.diaryDateMillis != java.lang.Long.MAX_VALUE
    }

    companion object {
        /* Constants representing missing data */
        val NO_DATE = java.lang.Long.MAX_VALUE
        val NO_ID: Long = -1
    }

}
/**
 * Create a new diary with no diary date
 */
