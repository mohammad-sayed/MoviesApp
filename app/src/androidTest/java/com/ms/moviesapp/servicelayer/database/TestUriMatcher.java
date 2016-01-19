package com.ms.moviesapp.servicelayer.database;

import android.content.UriMatcher;
import android.net.Uri;

import junit.framework.TestCase;

/**
 * Created by Mohammad-Sayed-PC on 1/12/2016.
 */
public class TestUriMatcher extends TestCase {

    //private static final long TEST_DATE = 1419033600L;  // December 20th, 2014
    //private static final long TEST_LOCATION_ID = 10L;

    // content://com.example.android.sunshine.app/weather"
    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_TRAILERS_DIR = MovieContract.TrailerEntry.buildTrailer(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
    private static final Uri TEST_REVIEWS_DIR = MovieContract.ReviewEntry.buildReview(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
    private static final Uri TEST_TRAILERS_OF_MOVIE_DIR = MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID);
    private static final Uri TEST_REVIEWS_OF_MOVIE_DIR = MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID);

    /*
        Students: This function tests that your UriMatcher returns the correct integer value
        for each of the Uri types that our ContentProvider can handle.  Uncomment this when you are
        ready to test your UriMatcher.
     */
    public void testUriMatcher() {
        UriMatcher testMatcher = MoviesProvider.buildUriMatcher();

        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_MOVIE_DIR), MoviesProvider.MOVIES);
        assertEquals("Error: The TRAILER URI was matched incorrectly.",
                testMatcher.match(TEST_TRAILERS_DIR), MoviesProvider.TRAILER_ITEM);
        assertEquals("Error: The REVIEW WITH MOVIE URI was matched incorrectly.",
                testMatcher.match(TEST_REVIEWS_DIR), MoviesProvider.REVIEW_ITEM);
        assertEquals("Error: The TRAILER WITH MOVIE ID was matched incorrectly.",
                testMatcher.match(TEST_TRAILERS_OF_MOVIE_DIR), MoviesProvider.MOVIE_TRAILERS);
        assertEquals("Error: The TRAILER WITH MOVIE ID was matched incorrectly.",
                testMatcher.match(TEST_REVIEWS_OF_MOVIE_DIR), MoviesProvider.MOVIE_REVIEWS);
    }
}
