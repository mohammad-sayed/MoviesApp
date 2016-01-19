package com.ms.moviesapp.servicelayer.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 1/8/2016.
 */
public class MovieContract {

    /**
     * To query a content provider, you specify the query string in the form of a URI which has following format:
     * <prefix>://<authority>/<data_type>/<id>
     */

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.ms.moviesapp";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIE = "movies";
    public static final String PATH_TRAILER = "trailers";
    public static final String PATH_REVIEW = "reviews";

    //public static final String CONTENT_AUTHORITY = "com.ms.moviesapp";
    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        //public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_USER_RATING = "user_rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static String getSelectionStringViaMovieId() {
            return MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry._ID + " = ? ";
        }

        public static String[] getSelectionArgsViaMovieId(Uri uri) {
            return new String[]{getMovieIdFromUri(uri)};
        }

    }

    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailers";
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static Uri buildTrailersOfMovieUri(long movieId) {
            return CONTENT_URI.buildUpon().appendQueryParameter(TrailerEntry.COLUMN_MOVIE_ID, "" + movieId).build();
        }

        public static Uri buildTrailer(long movieId, String trailerId) {
            return CONTENT_URI.buildUpon().appendPath("" + movieId).appendPath(trailerId).build();
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(TrailerEntry.COLUMN_MOVIE_ID);
        }

        public static String getTrailerIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }

        public static String getSelectionStringViaTrailerId() {
            return TrailerEntry.TABLE_NAME +
                    "." + TrailerEntry.COLUMN_TRAILER_ID + " = ?";
        }

        public static String getSelectionStringViaMovieId() {
            return TrailerEntry.TABLE_NAME +
                    "." + TrailerEntry.COLUMN_MOVIE_ID + " = ? ";
        }

        public static String[] getSelectionArgsViaTrailer(Uri uri) {
            return new String[]{getTrailerIdFromUri(uri)};
        }

        public static String[] getSelectionArgsViaMovieId(Uri uri) {
            return new String[]{getMovieIdFromUri(uri)};
        }

    }

    public static final class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_AUTHOR = "poster_path";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static Uri buildReviewsOfMovieUri(long movieId) {
            return CONTENT_URI.buildUpon().appendQueryParameter(ReviewEntry.COLUMN_MOVIE_ID, "" + movieId).build();
        }

        public static Uri buildReview(long movieId, String reviewId) {
            return CONTENT_URI.buildUpon().appendPath("" + movieId).appendPath(reviewId).build();
        }

        public static String getReviewIdFromUri(Uri uri) {
            return uri.getPathSegments().get(2);
        }


        public static String getMovieIdFromUri(Uri uri) {
            return uri.getQueryParameter(ReviewEntry.COLUMN_MOVIE_ID);
        }

        public static String getSelectionStringViaReviewId() {
            return ReviewEntry.TABLE_NAME +
                    "." + ReviewEntry.COLUMN_REVIEW_ID + " = ?";
        }

        public static String getSelectionStringViaMovieId() {
            return ReviewEntry.TABLE_NAME +
                    "." + ReviewEntry.COLUMN_MOVIE_ID + " = ?";
        }

        public static String[] getSelectionArgsViaReviewId(Uri uri) {
            return new String[]{getReviewIdFromUri(uri)};
        }

        public static String[] getSelectionArgsViaMovieId(Uri uri) {
            return new String[]{getMovieIdFromUri(uri)};
        }

    }


    public static ArrayList<Movie> getAllMovies(Context context) {

        ArrayList<Movie> movies = new ArrayList<>();

        Cursor cursor = context.getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
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
            cursor.close();
        }
        return movies;
    }

    public static ArrayList<Trailer> getAllTrailers(Context context) {

        Cursor cursor = context.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI, null,
                null, null, null);

        return getTrailersFromCursor(cursor);
    }

    public static ArrayList<Trailer> getTrailersByMovieId(Context context, long movieId) {
        Cursor cursor = context.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieId), null,
                null, null, null);
        return getTrailersFromCursor(cursor);
    }

    private static ArrayList<Trailer> getTrailersFromCursor(Cursor cursor) {
        ArrayList<Trailer> trailers = new ArrayList<>();
        if (cursor != null) {
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
            cursor.close();
        }
        return trailers;
    }


    private static boolean insertTrailer(Context context, long movieId, Trailer trailer) {
        Uri returnedUri = context.getContentResolver().insert(TrailerEntry.buildTrailersOfMovieUri(movieId),
                createTrailerContentValue(movieId, trailer));
        if (returnedUri != null)
            return true;
        return false;
    }

    private static boolean insertTrailers(Context context, long movieId, ArrayList<Trailer> trailers) {
        ContentValues[] contentValuesArray = new ContentValues[trailers.size()];
        for (int i = 0; i < contentValuesArray.length; i++) {
            contentValuesArray[i] = createTrailerContentValue(movieId, trailers.get(i));
        }
        int insertedRows = context.getContentResolver().bulkInsert(
                TrailerEntry.CONTENT_URI, contentValuesArray);
        if (insertedRows > 0)
            return true;
        return false;
    }


    public static ArrayList<Review> getReviewsOfMovie(Context context, long movieId) {
        ArrayList<Review> reviews = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieId), null,
                null, null, null);
        if (cursor != null) {
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
            cursor.close();
        }
        return reviews;
    }

    private static boolean insertReview(Context context, long movieId, Review review) {
        Uri returnedUri = context.getContentResolver().insert(ReviewEntry.buildReviewsOfMovieUri(movieId),
                createReviewContentValue(movieId, review));
        if (returnedUri != null)
            return true;
        return false;
    }

    private static boolean insertReviews(Context context, long movieId, ArrayList<Review> reviews) {
        ContentValues[] contentValuesArray = new ContentValues[reviews.size()];
        for (int i = 0; i < contentValuesArray.length; i++) {
            contentValuesArray[i] = createReviewContentValue(movieId, reviews.get(i));
        }
        int insertedRows = context.getContentResolver().bulkInsert(
                ReviewEntry.CONTENT_URI, contentValuesArray);
        if (insertedRows > 0)
            return true;
        return false;
    }


    public static boolean checkIfMovieExist(Context context, long movieId) {
        boolean exist = false;
        Cursor cursor = context.getContentResolver()
                .query(MovieEntry.buildMovieUri(movieId), null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst())
                exist = true;
            cursor.close();
        }
        return exist;
    }

    public static boolean insertMovie(Context context, Movie movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        ContentResolver contentResolver = context.getContentResolver();
        Uri movieUri = contentResolver.insert(MovieEntry.buildMovieUri(movie.getId()), createMovieContentValue(movie));

        if (movieUri != null
                && insertTrailers(context, movie.getId(), trailers)
                && insertReviews(context, movie.getId(), reviews)) {
            return true;
        }
        return false;
    }

    public static boolean deleteMovie(Context context, long movieId) {
        int numberOfDeletedRows = context.getContentResolver().delete(MovieEntry.buildMovieUri(movieId), null, null);
        if (numberOfDeletedRows > 0)
            return true;
        return false;
    }


    public static ContentValues createMovieContentValue(Movie movie) {
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

    public static ContentValues createTrailerContentValue(long movieId, Trailer trailer) {
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

    public static ContentValues createReviewContentValue(long movieId, Review review) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ReviewEntry.COLUMN_REVIEW_ID, review.getId());
        contentValues.put(ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
        contentValues.put(ReviewEntry.COLUMN_CONTENT, review.getContent());
        contentValues.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        return contentValues;
    }

}
