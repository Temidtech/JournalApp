package com.swiftsynq.journalapp.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    //Database schema information
    public static final String TABLE_DIARIES = "dairies";

    public static final class DiaryColumns implements BaseColumns {
        //Task description
        public static final String DESCRIPTION = "description";
        //Favourite
        public static final String IS_FAVOURITE = "is_favourite";
        //Completion date (can be null)
        public static final String DIARY_DATE = "diary_date";

    }

    //Unique authority string for the content provider
    public static final String CONTENT_AUTHORITY = "com.swiftsynq.journalapp";

    /* Sort order constants */
    //Priority first, Completed last, the rest by date
    public static final String DEFAULT_SORT = String.format("%s ASC, %s DESC",
            DiaryColumns.DIARY_DATE, DiaryColumns.IS_FAVOURITE);

    //FAVOURITE FIRST
    public static final String FAVUORITE_SORT = String.format("%s ASC, %s ASC",
            DiaryColumns.IS_FAVOURITE, DiaryColumns.DIARY_DATE);

    //Base content Uri for accessing the provider
    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(CONTENT_AUTHORITY)
            .appendPath(TABLE_DIARIES)
            .build();

    /* Helpers to retrieve column values */
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt( cursor.getColumnIndex(columnName) );
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong( cursor.getColumnIndex(columnName) );
    }
}
