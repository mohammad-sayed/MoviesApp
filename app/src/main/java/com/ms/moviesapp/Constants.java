package com.ms.moviesapp;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public interface Constants {

    String MOVIE_TAG = "movie";
    String SELECTED_MOVIE_POSITION_TAG = "selected_movie_position";

    short LIST_TYPE_HEADER = 1;
    short LIST_TYPE_TRAILER = 2;
    short LIST_TYPE_REVIEW = 3;

    interface APIOperations {
        String GET_MOVIES_LIST = "get_movies_list";
        String GET_TRAILERS = "get_trailers";
        String GET_REVIEWS = "get_reviews";
    }

    interface DataOperations {
        String DATA_RETRIEVED = "data_retrieved";
        String DATA_PERSISTED = "data_persisted";
    }

    interface Errors {
        String ERROR_NO_DATA_RETRIEVED = "error_no_data_retrieved";
        String ERROR_CONNECTION = "error_connection";
        String ERROR_CLOSING_CONNECTION = "error_closing_connection";
        String ERROR_FETCH_DATA = "error_fetching_data";
        String ERROR_URL = "error_url";
    }

    interface ErrorCodes {
        int ERROR_NO_DATA_RETRIEVED = 1;
        int ERROR_CONNECTION = 2;
        int ERROR_CLOSING_CONNECTION = 3;
        int ERROR_FETCH_DATA = 4;
        int ERROR_URL = 5;
    }

    interface Extras {
        String OPERATION_TAG = "operation";
        String URI = "uri";
        String SYNC_RESULT = "sync_result";
    }

    interface BroadcastReceiversActions {
        String SYNC_RESULT_ACTION = "com.ms.moviesapp.android.SYNC_RESULT";
    }
}
