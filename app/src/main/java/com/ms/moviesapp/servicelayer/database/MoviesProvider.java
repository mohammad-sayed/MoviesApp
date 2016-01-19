package com.ms.moviesapp.servicelayer.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Mohammad-Sayed-PC on 1/9/2016.
 */
public class MoviesProvider extends ContentProvider {

    private MovieDbHelper mMovieDbHelper;
    private static final UriMatcher mUriMatcher = buildUriMatcher();

    static final int MOVIES = 100;
    static final int MOVIE_ITEM = 101;
    static final int TRAILER_ITEM = 200;
    static final int MOVIE_TRAILERS = 201;
    static final int REVIEW_ITEM = 300;
    static final int MOVIE_REVIEWS = 301;

    private static final SQLiteQueryBuilder mMovieByIdQueryBuilder;
    private static final SQLiteQueryBuilder mTrailerQueryBuilder;
    private static final SQLiteQueryBuilder mReviewQueryBuilder;


    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // 2) Use the addURI function to match each of the types.  Use the constants from
        // WeatherContract to help define the types to the UriMatcher.
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_MOVIE, MOVIES);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_MOVIE.concat("/#"), MOVIE_ITEM);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_TRAILER.concat("/#/*"), TRAILER_ITEM);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_TRAILER, MOVIE_TRAILERS);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_REVIEW.concat("/#/*"), REVIEW_ITEM);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY,
                MovieContract.PATH_REVIEW, MOVIE_REVIEWS);
        // 3) Return the new matcher!
        return uriMatcher;
    }

    static {
        mMovieByIdQueryBuilder = new SQLiteQueryBuilder();
        mTrailerQueryBuilder = new SQLiteQueryBuilder();
        //mTrailersByMovieQueryBuilder = new SQLiteQueryBuilder();
        mReviewQueryBuilder = new SQLiteQueryBuilder();
        //mReviewsByMovieQueryBuilder = new SQLiteQueryBuilder();

        mMovieByIdQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);

        mTrailerQueryBuilder.setTables(MovieContract.TrailerEntry.TABLE_NAME);

        mReviewQueryBuilder.setTables(MovieContract.ReviewEntry.TABLE_NAME);

    }


    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {

        String selection = MovieContract.MovieEntry.getSelectionStringViaMovieId();
        String[] selectionArgs = MovieContract.MovieEntry.getSelectionArgsViaMovieId(uri);

        return mMovieByIdQueryBuilder.query(mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getTrailersById(Uri uri, String[] projection, String sortOrder) {
        String selection = MovieContract.TrailerEntry.getSelectionStringViaTrailerId();
        String[] selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaTrailer(uri);

        return mTrailerQueryBuilder.query(mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getTrailersByMovie(Uri uri, String[] projection, String sortOrder) {
        String movieId = MovieContract.TrailerEntry.getMovieIdFromUri(uri);
        String selection = null;
        String[] selectionArgs = null;
        if (movieId != null) {
            selection = MovieContract.TrailerEntry.getSelectionStringViaMovieId();
            selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaMovieId(uri);
        }
        return mTrailerQueryBuilder.query(mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getReviewById(Uri uri, String[] projection, String sortOrder) {
        String selection = MovieContract.ReviewEntry.getSelectionStringViaReviewId();
        String[] selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaReviewId(uri);
        return mReviewQueryBuilder.query(mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }


    private Cursor getReviewsByMovie(Uri uri, String[] projection, String sortOrder) {
        String movieId = MovieContract.TrailerEntry.getMovieIdFromUri(uri);
        String selection = null;
        String[] selectionArgs = null;
        if (movieId != null) {
            selection = MovieContract.ReviewEntry.getSelectionStringViaMovieId();
            selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaMovieId(uri);
        }
        return mReviewQueryBuilder.query(mMovieDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private int updateMovieById(SQLiteDatabase db, Uri uri, ContentValues values) {
        String selection = MovieContract.MovieEntry.getSelectionStringViaMovieId();
        String[] selectionArgs = MovieContract.MovieEntry.getSelectionArgsViaMovieId(uri);
        return db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateTrailerById(SQLiteDatabase db, Uri uri, ContentValues values) {
        String selection = MovieContract.TrailerEntry.getSelectionStringViaTrailerId();
        String[] selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaTrailer(uri);
        return db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateTrailerByMovieId(SQLiteDatabase db, Uri uri, ContentValues values) {
        String selection = MovieContract.TrailerEntry.getSelectionStringViaMovieId();
        String[] selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaMovieId(uri);
        return db.update(MovieContract.TrailerEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateReviewById(SQLiteDatabase db, Uri uri, ContentValues values) {
        String selection = MovieContract.ReviewEntry.getSelectionStringViaReviewId();
        String[] selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaReviewId(uri);
        return db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    private int updateReviewByMovieId(SQLiteDatabase db, Uri uri, ContentValues values) {
        String selection = MovieContract.ReviewEntry.getSelectionStringViaMovieId();
        String[] selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaMovieId(uri);
        return db.update(MovieContract.ReviewEntry.TABLE_NAME, values, selection, selectionArgs);
    }


    private int deleteMovieById(SQLiteDatabase db, Uri uri) {
        String selection = MovieContract.MovieEntry.getSelectionStringViaMovieId();
        String[] selectionArgs = MovieContract.MovieEntry.getSelectionArgsViaMovieId(uri);
        return db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
    }

    private int deleteTrailerById(SQLiteDatabase db, Uri uri) {
        String selection = MovieContract.TrailerEntry.getSelectionStringViaTrailerId();
        String[] selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaTrailer(uri);
        return db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
    }

    private int deleteTrailerByMovieId(SQLiteDatabase db, Uri uri) {
        String movieId = MovieContract.TrailerEntry.getMovieIdFromUri(uri);
        String selection = null;
        String[] selectionArgs = null;
        if (movieId != null) {
            selection = MovieContract.TrailerEntry.getSelectionStringViaMovieId();
            selectionArgs = MovieContract.TrailerEntry.getSelectionArgsViaMovieId(uri);
        }
        if (selection == null)
            selection = "1";
        return db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
    }

    private int deleteReviewById(SQLiteDatabase db, Uri uri) {
        String selection = MovieContract.ReviewEntry.getSelectionStringViaReviewId();
        String[] selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaReviewId(uri);
        return db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
    }

    private int deleteReviewByMovieId(SQLiteDatabase db, Uri uri) {
        String movieId = MovieContract.ReviewEntry.getMovieIdFromUri(uri);
        String selection = null;
        String[] selectionArgs = null;
        if (movieId != null) {
            selection = MovieContract.ReviewEntry.getSelectionStringViaMovieId();
            selectionArgs = MovieContract.ReviewEntry.getSelectionArgsViaMovieId(uri);
        }
        if (selection == null)
            selection = "1";
        return db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
    }


    @Override
    public boolean onCreate() {
        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ITEM:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case TRAILER_ITEM:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            case MOVIE_TRAILERS:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW_ITEM:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case MOVIE_REVIEWS:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (mUriMatcher.match(uri)) {
            case MOVIES:
                cursor = mMovieDbHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MOVIE_ITEM:
                cursor = getMovieById(uri, projection, sortOrder);
                break;
            case TRAILER_ITEM:
                cursor = getTrailersById(uri, projection, sortOrder);
                break;
            case MOVIE_TRAILERS:
                cursor = getTrailersByMovie(uri, projection, sortOrder);
                break;
            case REVIEW_ITEM:
                cursor = getReviewById(uri, projection, sortOrder);
                break;
            case MOVIE_REVIEWS:
                cursor = getReviewsByMovie(uri, projection, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE_ITEM:
            case MOVIES: {
                returnUri = insertMovieItem(db, uri, values);
                break;
            }
            case TRAILER_ITEM:
            case MOVIE_TRAILERS: {
                returnUri = insertMovieTrailer(db, uri, values);
                break;
            }
            case REVIEW_ITEM:
            case MOVIE_REVIEWS: {
                returnUri = insertMovieReview(db, uri, values);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        // Student: Use the uriMatcher to match the WEATHER and LOCATION URI's we are going to
        // handle.  If it doesn't match these, throw an UnsupportedOperationException.
        final int match = mUriMatcher.match(uri);
        int effectedRows;
        if (selection == null)
            selection = "1";
        switch (match) {
            case MOVIE_ITEM:
                effectedRows = deleteMovieById(db, uri);
                break;
            case MOVIES:
                effectedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER_ITEM:
                effectedRows = deleteTrailerById(db, uri);
                break;
            case MOVIE_TRAILERS:
                effectedRows = deleteTrailerByMovieId(db, uri);
                break;
            case REVIEW_ITEM:
                effectedRows = deleteReviewById(db, uri);
                break;
            case MOVIE_REVIEWS:
                effectedRows = deleteReviewByMovieId(db, uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null && effectedRows > 0)
            getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return effectedRows;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();

        final int match = mUriMatcher.match(uri);
        int effectedRows;
        switch (match) {
            case MOVIE_ITEM:
                effectedRows = updateMovieById(db, uri, values);
                break;
            case MOVIES:
                effectedRows = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case TRAILER_ITEM:
                effectedRows = updateTrailerById(db, uri, values);
                break;
            case MOVIE_TRAILERS:
                effectedRows = updateTrailerByMovieId(db, uri, values);
                break;
            case REVIEW_ITEM:
                effectedRows = updateReviewById(db, uri, values);
                break;
            case MOVIE_REVIEWS:
                effectedRows = updateReviewByMovieId(db, uri, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (getContext() != null && effectedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        db.close();
        return effectedRows;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        int numberOfInsertedValues = 0;
        switch (match) {
            case MOVIE_ITEM:
            case MOVIES: {
                db.beginTransaction();
                try {
                    Uri returnedUri;
                    for (ContentValues contentValues : values) {
                        returnedUri = insertMovieItem(db, uri, contentValues);
                        if (returnedUri != null)
                            numberOfInsertedValues++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            case TRAILER_ITEM:
            case MOVIE_TRAILERS: {
                db.beginTransaction();
                try {
                    Uri returnedUri;
                    for (ContentValues contentValues : values) {
                        returnedUri = insertMovieTrailer(db, uri, contentValues);
                        if (returnedUri != null)
                            numberOfInsertedValues++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            case REVIEW_ITEM:
            case MOVIE_REVIEWS: {
                db.beginTransaction();
                try {
                    Uri returnedUri;
                    for (ContentValues contentValues : values) {
                        returnedUri = insertMovieReview(db, uri, contentValues);
                        if (returnedUri != null)
                            numberOfInsertedValues++;
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }
            default:
                return super.bulkInsert(uri, values);
        }
        if (getContext() != null)
            getContext().getContentResolver().notifyChange(uri, null);
        return numberOfInsertedValues;
    }

    private Uri insertMovieItem(SQLiteDatabase db, Uri uri, ContentValues contentValues) {
        Uri returnUri = null;
        long movieId = contentValues.getAsLong(MovieContract.MovieEntry._ID);
        Uri exactMovieUri = MovieContract.MovieEntry.buildMovieUri(movieId);
        Cursor cursor = query(exactMovieUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int effectedRows = update(exactMovieUri, contentValues, null, null);
                if (effectedRows > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(contentValues.getAsLong(MovieContract.MovieEntry._ID));
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            } else {
                //returned row is long movieId
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            cursor.close();
        }
        return returnUri;
    }

    private Uri insertMovieTrailer(SQLiteDatabase db, Uri uri, ContentValues contentValues) {
        Uri returnUri = null;
        long movieId = contentValues.getAsLong(MovieContract.TrailerEntry.COLUMN_MOVIE_ID);
        String trailerId = contentValues.getAsString(MovieContract.TrailerEntry.COLUMN_TRAILER_ID);
        Uri exactTrailerUri = MovieContract.TrailerEntry.buildTrailer(movieId, trailerId);
        Cursor cursor = query(exactTrailerUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int effectedRows = update(exactTrailerUri, contentValues, null, null);
                if (effectedRows > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailer(movieId, trailerId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            } else {
                //returned row is long movieId
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MovieContract.TrailerEntry.buildTrailer(movieId, trailerId);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            cursor.close();
        }
        return returnUri;
    }

    private Uri insertMovieReview(SQLiteDatabase db, Uri uri, ContentValues contentValues) {
        Uri returnUri = null;
        long movieId = contentValues.getAsLong(MovieContract.ReviewEntry.COLUMN_MOVIE_ID);
        String reviewId = contentValues.getAsString(MovieContract.ReviewEntry.COLUMN_REVIEW_ID);
        Uri exactReviewUri = MovieContract.ReviewEntry.buildReview(movieId, reviewId);
        Cursor cursor = query(exactReviewUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int effectedRows = update(exactReviewUri, contentValues, null, null);
                if (effectedRows > 0)
                    returnUri = MovieContract.ReviewEntry.buildReview(movieId, reviewId);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            } else {
                //returned row is long movieId
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = MovieContract.TrailerEntry.buildTrailer(movieId, reviewId);
                } else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            cursor.close();
        }

        return returnUri;
    }
}
