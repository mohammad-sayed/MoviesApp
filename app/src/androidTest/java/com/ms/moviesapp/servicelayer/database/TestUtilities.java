package com.ms.moviesapp.servicelayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.ms.moviesapp.utils.PollingCheck;

/**
 * Created by Mohammad-Sayed-PC on 1/12/2016.
 */
public class TestUtilities extends AndroidTestCase {

    static final long TEST_MOVIE_ID = 123456L;
    static final String TEST_POSTER_PATH = "poster_path";
    static final String TEST_RELEASE_DATE = "05.oct.2016";
    static final String TEST_SYNOPSIS = "synopsis";
    static final String TEST_THUMBNAIL = "thumbnail";
    static final String TEST_TITLE = "title";
    static final String TEST_USER_RATING = "10.0f";


    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        /*Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }*/
    }


    static ContentValues createMovieContentValues(long movieId) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry._ID, movieId);
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, TEST_POSTER_PATH);
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, TEST_RELEASE_DATE);
        testValues.put(MovieContract.MovieEntry.COLUMN_SYNOPSIS, TEST_SYNOPSIS);
        testValues.put(MovieContract.MovieEntry.COLUMN_THUMBNAIL, TEST_THUMBNAIL);
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, TEST_TITLE);
        testValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, TEST_USER_RATING);
        return testValues;
    }

    static long insertMovieValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieContentValues(TEST_MOVIE_ID);
        String where = MovieContract.MovieEntry._ID + " = ?";
        String[] selectionArgs = new String[]{testValues.getAsString(MovieContract.MovieEntry._ID)};
        Cursor cursor = db.query(MovieContract.MovieEntry.TABLE_NAME, null, where, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(MovieContract.MovieEntry.TABLE_NAME, where, selectionArgs);
        }
        long movieRowId;
        movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("ErrorException: Failure to insert Movie Values", movieRowId != -1);

        return movieRowId;
    }

    static final String TEST_TRAILER_ID = "123789";
    static final String TEST_KEY = "fxghdfx";
    static final String TEST_NAME = "trailer_name";
    static final String TEST_SITE = "youtube";
    static final String TEST_SIZE = "480";
    static final String TEST_TYPE = "type";

    static ContentValues createTrailerContentValues(long movieId, String trailerId) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, movieId);
        testValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, trailerId);
        testValues.put(MovieContract.TrailerEntry.COLUMN_KEY, TEST_KEY);
        testValues.put(MovieContract.TrailerEntry.COLUMN_NAME, TEST_NAME);
        testValues.put(MovieContract.TrailerEntry.COLUMN_SITE, TEST_SITE);
        testValues.put(MovieContract.TrailerEntry.COLUMN_SIZE, TEST_SIZE);
        testValues.put(MovieContract.TrailerEntry.COLUMN_TYPE, TEST_TYPE);
        return testValues;
    }

    static final String TEST_AUTHOR = "author";
    static final String TEST_CONTENT = "content";
    static final String TEST_REVIEW_ID = "456951";

    static ContentValues createReviewContentValues(long movieId, String reviewId) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, movieId);
        testValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, TEST_AUTHOR);
        testValues.put(MovieContract.ReviewEntry.COLUMN_CONTENT, TEST_CONTENT);
        testValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_ID, reviewId);
        return testValues;
    }

    static long insertTrailerValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createTrailerContentValues(TEST_MOVIE_ID, TEST_TRAILER_ID);
        String where = MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " = ?";
        String[] selectionArgs = new String[]{testValues.getAsString(MovieContract.TrailerEntry.COLUMN_TRAILER_ID)};
        Cursor cursor = db.query(MovieContract.TrailerEntry.TABLE_NAME, null, where, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(MovieContract.TrailerEntry.TABLE_NAME, where, selectionArgs);
        }
        long trailerRowId;
        trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("ErrorException: Failure to insert Trailer Values", trailerRowId != -1);

        return trailerRowId;
    }

    static long insertReviewValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
        String where = MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " = ?";
        String[] selectionArgs = new String[]{testValues.getAsString(MovieContract.ReviewEntry.COLUMN_REVIEW_ID)};
        Cursor cursor = db.query(MovieContract.ReviewEntry.TABLE_NAME, null, where, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            db.delete(MovieContract.ReviewEntry.TABLE_NAME, where, selectionArgs);
        }
        long reviewRowId;
        reviewRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues);
        // Verify we got a row back.
        assertTrue("ErrorException: Failure to insert Review Values", reviewRowId != -1);
        return reviewRowId;
    }

    /*
    Students: The functions we provide inside of TestProvider use this utility class to test
    the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
    CTS tests.

    Note that this only tests that the onChange function is called; it does not test that the
    correct Uri is returned.
 */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}
