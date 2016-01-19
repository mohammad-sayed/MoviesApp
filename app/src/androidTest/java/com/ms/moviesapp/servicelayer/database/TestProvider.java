package com.ms.moviesapp.servicelayer.database;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by Mohammad-Sayed-PC on 1/12/2016.
 */
public class TestProvider extends AndroidTestCase {
    /*
        This test checks to make sure that the content provider is registered correctly.
     */

    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // MoviesProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MoviesProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: WeatherProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MoviesProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /**
     * This test doesn't touch the database.  It verifies that the ContentProvider returns
     * the correct type for each type of URI that it can handle.
     */
    public void testGetType() {
        // content://com.ms.moviesapp/movies/
        String type = mContext.getContentResolver().getType(MovieContract.MovieEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.ms.moviesapp/movies
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieContract.MovieEntry.CONTENT_TYPE, type);

        // content://com.ms.moviesapp/trailers/[movie_id]
        type = mContext.getContentResolver().getType(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID));
        // vnd.android.cursor.dir/com.ms.moviesapp/trailers
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_TYPE",
                MovieContract.TrailerEntry.CONTENT_TYPE, type);

        // content://com.ms.moviesapp/reviews/[movie_id]
        type = mContext.getContentResolver().getType(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID));
        // vnd.android.cursor.dir/com.ms.moviesapp/reviews
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry .CONTENT_TYPE",
                MovieContract.ReviewEntry.CONTENT_TYPE, type);

        long movieId = 123456L;

        // content://com.ms.moviesapp/movies/123456
        type = mContext.getContentResolver().getType(
                MovieContract.MovieEntry.buildMovieUri(movieId));
        // vnd.android.cursor.item/com.ms.moviesapp/trailers/123456
        assertEquals("Error: the MovieEntry CONTENT_URI with location should return MovieEntry.CONTENT_ITEM_TYPE",
                MovieContract.MovieEntry.CONTENT_ITEM_TYPE, type);


        // content://com.ms.moviesapp/trailers/123456
        type = mContext.getContentResolver().getType(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieId));
        // vnd.android.cursor.dir/com.ms.moviesapp/trailers/123456
        assertEquals("Error: the TrailerEntry CONTENT_URI with location should return TrailerEntry.CONTENT_TYPE",
                MovieContract.TrailerEntry.CONTENT_TYPE, type);

        // content://com.ms.moviesapp/review/123456
        type = mContext.getContentResolver().getType(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieId));
        // vnd.android.cursor.dir/com.ms.moviesapp/reviews/123456
        assertEquals("Error: the ReviewEntry CONTENT_URI with location should return ReviewEntry.CONTENT_TYPE",
                MovieContract.ReviewEntry.CONTENT_TYPE, type);

    }


    /**
     * This test uses the database directly to insert and then uses the ContentProvider to
     * read out the data.
     */
    public void testBasicMovieQuery() {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long movieRowId = TestUtilities.insertMovieValues(mContext);
        assertTrue("Unable to Insert TrailerEntry into the Database", movieRowId != -1);

        long trailerRowId = TestUtilities.insertTrailerValues(mContext);
        assertTrue("Unable to Insert TrailerEntry into the Database", trailerRowId != -1);

        long reviewRowId = TestUtilities.insertReviewValues(mContext);
        assertTrue("Unable to Insert ReviewEntry into the Database", reviewRowId != -1);

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );


        // Fantastic.  Now that we have a location, add some weather!
        ContentValues movieContentValues = TestUtilities.createMovieContentValues(TestUtilities.TEST_MOVIE_ID);

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicMovieQuery", movieCursor, movieContentValues);

        // Test the basic content provider query
        Cursor trailerCursor = mContext.getContentResolver().query(MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieRowId),
                null,
                null,
                null,
                null
        );

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicTrailerQuery", trailerCursor, trailerContentValues);

        // Test the basic content provider query
        Cursor reviewCursor = mContext.getContentResolver().query(MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieRowId),
                null,
                null,
                null,
                null
        );

        // Fantastic.  Now that we have a location, add some weather!
        ContentValues reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicReviewQuery", reviewCursor, reviewContentValues);
        db.close();
    }

    //insert and query functionality must also be complete before this test can be used.
    public void testInsertReadProvider() {

        //*************** Test Movie Insertion *****************************************************
        ContentValues testValues = TestUtilities.createMovieContentValues(TestUtilities.TEST_MOVIE_ID);

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, tco);
        Uri movieUri = mContext.getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long movieId = ContentUris.parseId(movieUri);

        // Verify we got a row back.
        assertTrue(movieId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry.",
                cursor, testValues);

        //******************************************************************************************

        //*************** Test Trailer Insertion *****************************************************

        ContentValues trailerValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);

        // Register a content observer for our insert.  This time, directly with the content resolver
        tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.TrailerEntry.CONTENT_URI, true, tco);
        Uri trailerUri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailer(movieId, TestUtilities.TEST_TRAILER_ID), trailerValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        String trailerId = MovieContract.TrailerEntry.getTrailerIdFromUri(trailerUri);

        // Verify we got a row back.
        assertTrue(trailerId != null);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailer(movieId, trailerId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating TrailerEntry Item.",
                trailerCursor, trailerValues);
        //******************************************************************************************

        //*************** Test Trailers Of Movie Insertion *****************************************************


        // Fantastic.  Now that we have a location, add some weather!
        trailerValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieContract.TrailerEntry.CONTENT_URI, true, tco);

        Uri trailerInsertUri = mContext.getContentResolver()
                .insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieId), trailerValues);
        assertTrue(trailerInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieId),  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating TrailerEntry for Movie insert.",
                trailerCursor, trailerValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        //trailerValues.putAll(trailerValues);

        // Get the joined Trailer and Movie data
        trailerCursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(movieId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Trailer and Movie Data.",
                trailerCursor, trailerValues);

        //******************************************************************************************

        //*************** Test Review Insertion *****************************************************

        ContentValues reviewValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);

        // Register a content observer for our insert.  This time, directly with the content resolver
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieContract.ReviewEntry.CONTENT_URI, true, tco);
        Uri reviewUri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReview(movieId, TestUtilities.TEST_REVIEW_ID), reviewValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        String reviewId = MovieContract.ReviewEntry.getReviewIdFromUri(reviewUri);

        // Verify we got a row back.
        assertTrue(reviewId != null);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor reviewCursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.buildReview(movieId, reviewId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ReviewEntry Item.",
                reviewCursor, reviewValues);
        //******************************************************************************************

        //*************** Test Review Of Movie Insertion *****************************************************
        // Fantastic.  Now that we have a location, add some weather!
        //ContentValues reviewValues = TestUtilities.createReviewContentValues();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(MovieContract.ReviewEntry.CONTENT_URI, true, tco);

        Uri reviewInsertUri = mContext.getContentResolver()
                .insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieId), reviewValues);
        assertTrue(reviewInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        reviewCursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieId),  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ReviewEntry for Movie insert.",
                reviewCursor, reviewValues);

        // Add the location values in with the weather data so that we can make
        // sure that the join worked and we actually get all the values back
        //reviewValues.putAll(testValues);

        // Get the joined Review and Movie data
        reviewCursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(movieId),
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );
        TestUtilities.validateCursor("testInsertReadProvider.  Error validating joined Review and Movie Data.",
                reviewCursor, reviewValues);

    }


    //TEST_DELETE

    // Make sure we can still delete after adding/updating stuff
    //
    // Student: Uncomment this test after you have completed writing the delete functionality
    // in your provider.  It relies on insertions with testInsertReadProvider, so insert and
    // query functionality must also be complete before this test can be used.
    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver movieObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.MovieEntry.CONTENT_URI, true, movieObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver trailerObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(MovieContract.TrailerEntry.CONTENT_URI, true, trailerObserver);

        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        movieObserver.waitForNotificationOrFail();
        trailerObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(movieObserver);
        mContext.getContentResolver().unregisterContentObserver(trailerObserver);
    }

    /*
   This helper function deletes all records from both database tables using the ContentProvider.
   It also queries the ContentProvider to make sure that the database has been successfully
   deleted, so it cannot be used until the Query and Delete functions have been written
   in the ContentProvider.
     */
    public void deleteAllRecordsFromProvider() {

        //*************************** Test Movie Deletion ******************************************

        //Delete All Movies
        mContext.getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null
        );
        //Get AllMovies
        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Movies Must Equal Zero
        assertEquals("Error: All Records not deleted from Movie table during delete", 0, cursor.getCount());
        //Insert two Movies
        ContentValues movieContentValues = TestUtilities.createMovieContentValues(TestUtilities.TEST_MOVIE_ID);
        Uri movie1Uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.buildMovieUri(TestUtilities.TEST_MOVIE_ID), movieContentValues);
        final long TEST_MOVIE_ID2 = 147258L;
        movieContentValues = TestUtilities.createMovieContentValues(TEST_MOVIE_ID2);
        Uri movie2Uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.buildMovieUri(TEST_MOVIE_ID2), movieContentValues);

        //Get All Movies
        cursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Movies must equal 2
        assertEquals("Error: A Record not inserted to Movie table", 2, cursor.getCount());

        //Delete a specific Movie for ID: TEST_MOVIE_ID, Use Selection and SelectionArgs
        int deletedRows = mContext.getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(TestUtilities.TEST_MOVIE_ID), null, null);

        assertEquals("Error:A requested Records not deleted from Movie table", 1, deletedRows);

        //Get AllMovies
        cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Movies Must Equal 1
        assertEquals("Error: The Record not deleted from Movie table", 1, cursor.getCount());


        //**************************** Test Trailer ************************************************

        movieContentValues = TestUtilities.createMovieContentValues(TestUtilities.TEST_MOVIE_ID);
        movie1Uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.buildMovieUri(TestUtilities.TEST_MOVIE_ID), movieContentValues);
        movieContentValues = TestUtilities.createMovieContentValues(TEST_MOVIE_ID2);
        movie2Uri = mContext.getContentResolver().insert(MovieContract.MovieEntry.buildMovieUri(TEST_MOVIE_ID2), movieContentValues);


        //Delete All Trailers
        mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null
        );

        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Trailers must equal zero
        assertEquals("Error: All Records not deleted from Trailer table", 0, cursor.getCount());

        //Insert Four Trailers Two for each movieId
        ContentValues trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
        Uri trailer1Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        Uri trailer2Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TEST_MOVIE_ID2, "159326");
        Uri trailer3Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TEST_MOVIE_ID2, "147369");
        Uri trailer4Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2), trailerContentValues);

        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 4 trailers
        assertEquals("Error: Records not inserted to Trailer table", 4, cursor.getCount());

        //Get Trailers Of TEST_MOVIE_ID
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID),
                null,
                null,
                null,
                null
        );
        //Must Return 2 trailers
        assertEquals("Error: Records not inserted to Trailer table", 2, cursor.getCount());

        //Get Trailers Of TEST_MOVIE_ID2
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2),
                null,
                null,
                null,
                null
        );
        //Must Return 2 trailers
        assertEquals("Error: Records not inserted to Trailer table", 2, cursor.getCount());

        //Test Delete Specific Trailer Record by Id
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.buildTrailer(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID),
                null,
                null
        );

        assertEquals("Error:A requested Records not deleted from Trailer table", 1, deletedRows);


        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 3 trailers
        assertEquals("Error: Records not deleted from Trailer table", 3, cursor.getCount());


        //Test Delete Reviews of Specific MovieId: TEST_MOVIE_ID2
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2),
                null, null);

        assertEquals("Error:A requested Records not deleted from Trailer table", 2, deletedRows);

        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 1 trailer
        assertEquals("Error: Records not inserted to Trailer table", 1, cursor.getCount());

        //Test Delete Reviews of Specific MovieId: TEST_MOVIE_ID2
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.TrailerEntry.CONTENT_URI,
                null, null);

        //Must Return 1
        assertEquals("Error:A requested Records not deleted from Trailer table", 1, deletedRows);

        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 0 trailer
        assertEquals("Error: Records not inserted to Trailer table", 0, cursor.getCount());

        //************************************ Review Test *****************************************


        //Delete All Trailers
        mContext.getContentResolver().delete(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null
        );

        //Get All Trailers
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        //Reviews must equal zero
        assertEquals("Error: All Records not deleted from Review table", 0, cursor.getCount());

        //Insert Four Reviews Two for each movieId
        ContentValues reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
        Uri review1Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        Uri review2Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TEST_MOVIE_ID2, "5489415");
        Uri review3Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TEST_MOVIE_ID2, "147369");
        Uri review4Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);

        //Get All Reviews
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 4 reviews
        assertEquals("Error: Records not inserted to Review table", 4, cursor.getCount());

        //Get Reviews Of TEST_MOVIE_ID
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID),
                null,
                null,
                null,
                null
        );
        //Must Return 2 Reviews
        assertEquals("Error: Records not inserted to Review table", 2, cursor.getCount());

        //Get Trailers Of TEST_MOVIE_ID2
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(TEST_MOVIE_ID2),
                null,
                null,
                null,
                null
        );
        //Must Return 2 reviews
        assertEquals("Error: Records not inserted to Review table", 2, cursor.getCount());

        //Test Delete Specific Review Record by Id
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.ReviewEntry.buildReview(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID),
                null,
                null
        );

        assertEquals("Error:A requested Records not deleted from Review table", 1, deletedRows);


        //Get All Review
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 3 Reviews
        assertEquals("Error: Records not deleted from Review table", 3, cursor.getCount());


        //Test Delete Reviews of Specific MovieId: TEST_MOVIE_ID2
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.ReviewEntry.buildReviewsOfMovieUri(TEST_MOVIE_ID2),
                null, null);

        assertEquals("Error:A requested Records not deleted from Review table", 2, deletedRows);

        //Get All Reviews
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 1 Review
        assertEquals("Error: Records not inserted to Review table", 1, cursor.getCount());

        //Test Delete Reviews of Specific MovieId: TEST_MOVIE_ID2
        deletedRows = mContext.getContentResolver().delete(
                MovieContract.ReviewEntry.CONTENT_URI,
                null, null);

        //Must Return 1
        assertEquals("Error:A requested Records not deleted from Review table", 1, deletedRows);

        //Get All Reviews
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        //Must Return 0 Review
        assertEquals("Error: Records not inserted to Review table", 0, cursor.getCount());


        //Test Movie Delete Cascading:

        //Insert Four Trailers Two for each movieId
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
        trailer1Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        trailer2Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TestUtilities.TEST_MOVIE_ID), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TEST_MOVIE_ID2, "15648448");
        trailer3Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2), trailerContentValues);
        trailerContentValues = TestUtilities.createTrailerContentValues(TEST_MOVIE_ID2, "147369");
        trailer4Uri = mContext.getContentResolver().insert(MovieContract.TrailerEntry.buildTrailersOfMovieUri(TEST_MOVIE_ID2), trailerContentValues);

        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
        review1Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        review2Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TestUtilities.TEST_MOVIE_ID), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TEST_MOVIE_ID2, "123789");
        review3Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TEST_MOVIE_ID2), reviewContentValues);
        reviewContentValues = TestUtilities.createReviewContentValues(TEST_MOVIE_ID2, "147369");
        review4Uri = mContext.getContentResolver().insert(MovieContract.ReviewEntry.buildReviewsOfMovieUri(TEST_MOVIE_ID2), reviewContentValues);

        //Delete a specific Movie for ID: TEST_MOVIE_ID, Use Selection and SelectionArgs
        deletedRows = mContext.getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(TestUtilities.TEST_MOVIE_ID), null, null);

        assertEquals("Error:A requested Records not deleted from Movie table", 1, deletedRows);


        //Get All Reviews
        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        assertEquals("Error: Records not inserted to Review table", 2, cursor.getCount());

        cursor.close();
    }

    public void testBulkInsert() {

        mContext.getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(MovieContract.TrailerEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI, null, null);

        Cursor cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 0);

        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 0);

        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 0);


        final long TEST_MOVIE_ID2 = 147258L;

        ContentValues[] movieContentValuesArray = new ContentValues[2];
        ContentValues movieContentValues = TestUtilities.createMovieContentValues(TestUtilities.TEST_MOVIE_ID);
        movieContentValuesArray[0] = movieContentValues;
        movieContentValues = TestUtilities.createMovieContentValues(TEST_MOVIE_ID2);
        movieContentValuesArray[1] = movieContentValues;

        mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, movieContentValuesArray);

        cursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 2);

        //Insert Four Trailers
        ContentValues[] trailerContentValuesArray = new ContentValues[4];
        ContentValues trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_TRAILER_ID);
        trailerContentValuesArray[0] = trailerContentValues;
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        trailerContentValuesArray[1] = trailerContentValues;
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, "15648448");
        trailerContentValuesArray[2] = trailerContentValues;
        trailerContentValues = TestUtilities.createTrailerContentValues(TestUtilities.TEST_MOVIE_ID, "147369");
        trailerContentValuesArray[3] = trailerContentValues;

        mContext.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, trailerContentValuesArray);


        cursor = mContext.getContentResolver().query(
                MovieContract.TrailerEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 4);


        ContentValues[] reviewContentValuesArray = new ContentValues[4];
        ContentValues reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, TestUtilities.TEST_REVIEW_ID);
        reviewContentValuesArray[0] = reviewContentValues;
        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, "258369");
        reviewContentValuesArray[1] = reviewContentValues;
        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, "123789");
        reviewContentValuesArray[2] = reviewContentValues;
        reviewContentValues = TestUtilities.createReviewContentValues(TestUtilities.TEST_MOVIE_ID, "147369");
        reviewContentValuesArray[3] = reviewContentValues;


        mContext.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, reviewContentValuesArray);

        cursor = mContext.getContentResolver().query(
                MovieContract.ReviewEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        // we should have as many records in the database as we've inserted
        assertEquals(cursor.getCount(), 4);

        cursor.close();
    }
}
