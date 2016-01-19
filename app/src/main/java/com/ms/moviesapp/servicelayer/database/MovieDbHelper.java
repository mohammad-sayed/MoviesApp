package com.ms.moviesapp.servicelayer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ms.moviesapp.servicelayer.database.MovieContract.MovieEntry;
import com.ms.moviesapp.servicelayer.database.MovieContract.TrailerEntry;
import com.ms.moviesapp.servicelayer.database.MovieContract.ReviewEntry;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 1/8/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_SYNOPSIS + " TEXT, " +
                MovieEntry.COLUMN_THUMBNAIL + " TEXT, " +
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_USER_RATING + " REAL);";

        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TrailerEntry.COLUMN_TRAILER_ID + " TEXT UNIQUE, " +
                TrailerEntry.COLUMN_KEY + " TEXT, " +
                TrailerEntry.COLUMN_NAME + " TEXT, " +
                TrailerEntry.COLUMN_SITE + " TEXT, " +
                TrailerEntry.COLUMN_SIZE + " INTEGER, " +
                TrailerEntry.COLUMN_TYPE + " TEXT, " +
                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") ON DELETE CASCADE );";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ReviewEntry.COLUMN_REVIEW_ID + " TEXT UNIQUE, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT, " +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER, " +
                " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry._ID + ") ON DELETE CASCADE);";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }


    /*public void insert(Movie movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        insertMovie(movie);
        long movieId = movie.getId();
        insertTrailers(movieId, trailers, true);
        insertReviews(movieId, reviews, true);
    }*/


    /*private void insertMovie(Movie movie) {
        boolean movieExist = checkIfMovieExist(movie.getId());
        if (movieExist) {
            updateMovie(movie);
        } else {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValue = createMovieContentValue(movie);
            db.insert(MovieEntry.TABLE_NAME, null, contentValue);
            db.close();
        }
    }*/

    /*public ArrayList<Movie> getAllMovies() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Movie> movies = new ArrayList<>();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(MovieEntry._ID);
            int posterPathIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
            int releaseDateIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            int synopsisIndex = cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS);
            int thumbnailIndex = cursor.getColumnIndex(MovieEntry.COLUMN_THUMBNAIL);
            int titleIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
            int userRatingIndex = cursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING);

            do {
                Movie movie = new Movie();
                movie.setId(cursor.getLong(idIndex));
                movie.setPosterPath(cursor.getString(posterPathIndex));
                movie.setReleaseDate(cursor.getString(releaseDateIndex));
                movie.setSynopsis(cursor.getString(synopsisIndex));
                movie.setThumbnail(cursor.getString(thumbnailIndex));
                movie.setTitle(cursor.getString(titleIndex));
                movie.setUsersRating(cursor.getFloat(userRatingIndex));
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        db.close();
        return movies;
    }*/

    /*public Movie getMovie(long movieId) {
        Movie movie = null;
        SQLiteDatabase db = getReadableDatabase();

        String whereStatement = MovieEntry._ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(MovieEntry._ID);
            int posterPathIndex = cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH);
            int releaseDateIndex = cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE);
            int synopsisIndex = cursor.getColumnIndex(MovieEntry.COLUMN_SYNOPSIS);
            int thumbnailIndex = cursor.getColumnIndex(MovieEntry.COLUMN_THUMBNAIL);
            int titleIndex = cursor.getColumnIndex(MovieEntry.COLUMN_TITLE);
            int userRatingIndex = cursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING);
            movie = new Movie();
            movie.setId(cursor.getLong(idIndex));
            movie.setPosterPath(cursor.getString(posterPathIndex));
            movie.setReleaseDate(cursor.getString(releaseDateIndex));
            movie.setSynopsis(cursor.getString(synopsisIndex));
            movie.setThumbnail(cursor.getString(thumbnailIndex));
            movie.setTitle(cursor.getString(titleIndex));
            movie.setUsersRating(cursor.getFloat(userRatingIndex));
        }
        db.close();
        return movie;
    }*/

    /*public boolean checkIfMovieExist(long movieId) {
        SQLiteDatabase db = getReadableDatabase();
        String whereStatement = MovieEntry._ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        boolean exist = cursor.moveToFirst();
        db.close();
        return exist;
    }*/


    /*public void updateMovie(Movie movie) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createMovieContentValue(movie);
        String whereStatement = MovieEntry._ID + " = ?";
        String[] selectionArguments = new String[]{"" + movie.getId()};
        db.update(MovieEntry.TABLE_NAME, contentValues, whereStatement, selectionArguments);
        db.close();
    }

    public void deleteMovie(long movieId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereStatement = MovieEntry._ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        db.delete(MovieEntry.TABLE_NAME, whereStatement, selectionArguments);
    }*/

    /*private void insertTrailer(long movieId, Trailer trailer) {
        Trailer existingTrailer = getTrailer(trailer.getId());
        if (existingTrailer == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValue = createTrailerContentValue(movieId, trailer);
            db.insert(TrailerEntry.TABLE_NAME, null, contentValue);
            db.close();
        } else {
            updateTrailer(movieId, trailer);
        }
    }*/

    /*private void insertTrailers(long movieId, ArrayList<Trailer> trailers, boolean removeBeforeInserting) {
        if (removeBeforeInserting) {
            deleteTrailersOfMovie(movieId);
            SQLiteDatabase db = getWritableDatabase();
            for (Trailer trailer : trailers) {
                ContentValues contentValue = createTrailerContentValue(movieId, trailer);
                db.insert(TrailerEntry.TABLE_NAME, null, contentValue);
            }
            db.close();
        } else {
            for (Trailer trailer : trailers) {
                insertTrailer(movieId, trailer);
            }
        }
    }*/

    /*public ArrayList<Trailer> getTrailers(long movieId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Trailer> trailers = new ArrayList<>();
        String whereStatement = TrailerEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        Cursor cursor = db.query(TrailerEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        if (cursor.moveToFirst()) {
            int trailerIdIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_TRAILER_ID);
            int keyIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_KEY);
            int nameIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_NAME);
            int siteIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_SITE);
            int sizeIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_SIZE);
            int typeIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_TYPE);

            do {
                Trailer trailer = new Trailer();
                trailer.setId(cursor.getString(trailerIdIndex));
                trailer.setKey(cursor.getString(keyIndex));
                trailer.setName(cursor.getString(nameIndex));
                trailer.setSite(cursor.getString(siteIndex));
                trailer.setSize(cursor.getInt(sizeIndex));
                trailer.setType(cursor.getString(typeIndex));
                trailers.add(trailer);
            } while (cursor.moveToNext());
        }
        db.close();
        return trailers;
    }*/

    /*public Trailer getTrailer(String trailerId) {
        Trailer trailer = null;
        SQLiteDatabase db = getReadableDatabase();
        String whereStatement = TrailerEntry.COLUMN_TRAILER_ID + " = ?";
        String[] selectionArguments = new String[]{"" + trailerId};
        Cursor cursor = db.query(TrailerEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        if (cursor.moveToFirst()) {
            int trailerIdIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_TRAILER_ID);
            int keyIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_KEY);
            int nameIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_NAME);
            int siteIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_SITE);
            int sizeIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_SIZE);
            int typeIndex = cursor.getColumnIndex(TrailerEntry.COLUMN_TYPE);
            trailer = new Trailer();
            trailer.setId(cursor.getString(trailerIdIndex));
            trailer.setKey(cursor.getString(keyIndex));
            trailer.setName(cursor.getString(nameIndex));
            trailer.setSite(cursor.getString(siteIndex));
            trailer.setSize(cursor.getInt(sizeIndex));
            trailer.setType(cursor.getString(typeIndex));
        }
        db.close();
        return trailer;
    }*/

    /*private void updateTrailer(long movieId, Trailer trailer) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createTrailerContentValue(movieId, trailer);
        String whereStatement = TrailerEntry.COLUMN_TRAILER_ID + " = ?";
        String[] selectionArguments = new String[]{"" + trailer.getId()};
        db.update(TrailerEntry.TABLE_NAME, contentValues, whereStatement, selectionArguments);
        db.close();
    }*/

    /*private void deleteAllTrailers() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TrailerEntry.TABLE_NAME, null, null);
    }*/

    /*private void deleteTrailersOfMovie(long movieId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereStatement = TrailerEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        db.delete(TrailerEntry.TABLE_NAME, whereStatement, selectionArguments);
    }*/

    /*private void deleteTrailer(String trailerId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereStatement = TrailerEntry.COLUMN_TRAILER_ID + " = ?";
        String[] selectionArguments = new String[]{trailerId};
        db.delete(TrailerEntry.TABLE_NAME, whereStatement, selectionArguments);
    }*/

    /*private void insertReview(long movieId, Review review) {
        Trailer existingTrailer = getTrailer(review.getId());
        if (existingTrailer == null) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues contentValue = createReviewContentValue(movieId, review);
            db.insert(ReviewEntry.TABLE_NAME, null, contentValue);
            db.close();
        } else {
            updateReview(movieId, review);
        }
    }*/

    /*private void insertReviews(long movieId, ArrayList<Review> reviews, boolean removeBeforeInserting) {
        if (removeBeforeInserting) {
            deleteReviewsOfMovie(movieId);
            SQLiteDatabase db = getWritableDatabase();
            for (Review review : reviews) {
                ContentValues contentValue = createReviewContentValue(movieId, review);
                db.insert(ReviewEntry.TABLE_NAME, null, contentValue);
            }
            db.close();
        } else {
            for (Review review : reviews) {
                insertReview(movieId, review);
            }
        }
    }*/


    public ArrayList<Review> getReviews(long movieId) {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Review> reviews = new ArrayList<>();
        String whereStatement = ReviewEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        Cursor cursor = db.query(ReviewEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        if (cursor.moveToFirst()) {
            int reviewIdIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_REVIEW_ID);
            int authorIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR);
            int contentIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT);

            do {
                Review review = new Review();
                review.setId(cursor.getString(reviewIdIndex));
                review.setAuthor(cursor.getString(authorIndex));
                review.setContent(cursor.getString(contentIndex));
                reviews.add(review);
            } while (cursor.moveToNext());
        }
        db.close();
        return reviews;
    }

    public Review getReview(String reviewId) {
        Review review = null;
        SQLiteDatabase db = getReadableDatabase();
        String whereStatement = ReviewEntry.COLUMN_REVIEW_ID + " = ?";
        String[] selectionArguments = new String[]{"" + reviewId};
        Cursor cursor = db.query(ReviewEntry.TABLE_NAME, null, whereStatement, selectionArguments, null, null, null);
        if (cursor.moveToFirst()) {
            int reviewIdIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_REVIEW_ID);
            int authorIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR);
            int contentIndex = cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT);
            review = new Review();
            review.setId(cursor.getString(reviewIdIndex));
            review.setAuthor(cursor.getString(authorIndex));
            review.setContent(cursor.getString(contentIndex));
        }
        db.close();
        return review;
    }

    private void updateReview(long movieId, Review review) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = createReviewContentValue(movieId, review);
        String whereStatement = ReviewEntry.COLUMN_REVIEW_ID + " = ?";
        String[] selectionArguments = new String[]{"" + review.getId()};
        db.update(ReviewEntry.TABLE_NAME, contentValues, whereStatement, selectionArguments);
        db.close();
    }

    public void deleteAllReview() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ReviewEntry.TABLE_NAME, null, null);
    }

    public void deleteReviewsOfMovie(long movieId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereStatement = ReviewEntry.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArguments = new String[]{"" + movieId};
        db.delete(ReviewEntry.TABLE_NAME, whereStatement, selectionArguments);
    }

    public void deleteReview(String reviewId) {
        SQLiteDatabase db = getWritableDatabase();
        String whereStatement = ReviewEntry.COLUMN_REVIEW_ID + " = ?";
        String[] selectionArguments = new String[]{reviewId};
        db.delete(ReviewEntry.TABLE_NAME, whereStatement, selectionArguments);
    }

    private ContentValues createMovieContentValue(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry._ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(MovieEntry.COLUMN_THUMBNAIL, movie.getThumbnail());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_USER_RATING, movie.getUsersRating());
        return contentValues;
    }

    private ContentValues createTrailerContentValue(long movieId, Trailer trailer) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrailerEntry.COLUMN_TRAILER_ID, trailer.getId());
        contentValues.put(TrailerEntry.COLUMN_TYPE, trailer.getType());
        contentValues.put(TrailerEntry.COLUMN_KEY, trailer.getKey());
        contentValues.put(TrailerEntry.COLUMN_NAME, trailer.getName());
        contentValues.put(TrailerEntry.COLUMN_SITE, trailer.getSite());
        contentValues.put(TrailerEntry.COLUMN_SIZE, trailer.getSize());
        contentValues.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

    private ContentValues createReviewContentValue(long movieId, Review review) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReviewEntry.COLUMN_REVIEW_ID, review.getId());
        contentValues.put(ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        contentValues.put(ReviewEntry.COLUMN_CONTENT, review.getContent());
        contentValues.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
}
