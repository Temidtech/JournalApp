package com.swiftsynq.journalapp.data;

import android.database.Cursor;

import static com.swiftsynq.journalapp.data.DatabaseContract.getColumnInt;
import static com.swiftsynq.journalapp.data.DatabaseContract.getColumnLong;
import static com.swiftsynq.journalapp.data.DatabaseContract.getColumnString;

/**
 * Helpful data model for holding attributes related to a diary.
 */
public class DiaryEntry {

    /* Constants representing missing data */
    public static final long NO_DATE = Long.MAX_VALUE;
    public static final long NO_ID = -1;

    //Unique identifier in database
    public long id;
    //Diary description
    public final String description;
    //Marked if entry is favourite
    public final boolean isFavourite;
    //Optional diary date for the entry
    public final long diaryDateMillis;

    /**
     * Create a new Diary entry from discrete items
     */
    public DiaryEntry(String description, boolean isFavourite, long diaryDateMillis) {
        this.id = NO_ID; //Not set
        this.description = description;
        this.isFavourite = isFavourite;
        this.diaryDateMillis = diaryDateMillis;
    }

    /**
     * Create a new diary with no diary date
     */
    public DiaryEntry(String description, boolean isFavourite) {
        this(description, isFavourite, NO_DATE);
    }

    /**
     * Create a new diary entry from a database Cursor
     */
    public DiaryEntry(Cursor cursor) {
        this.id = getColumnLong(cursor, DatabaseContract.DiaryColumns._ID);
        this.description = getColumnString(cursor, DatabaseContract.DiaryColumns.DESCRIPTION);
        this.isFavourite = getColumnInt(cursor, DatabaseContract.DiaryColumns.IS_FAVOURITE) == 1;
        this.diaryDateMillis = getColumnLong(cursor, DatabaseContract.DiaryColumns.DIARY_DATE);
    }

    /**
     * Return true if a diary date has been set on this entry.
     */
    public boolean hasDueDate() {
        return this.diaryDateMillis != Long.MAX_VALUE;
    }

}
