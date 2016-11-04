package com.ms.moviesapp.controllerlayer;

import android.content.Context;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.callbacks.OnErrorListener;
import com.ms.moviesapp.callbacks.OnSuccessListener;
import com.ms.moviesapp.entities.Movie;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 3/15/2016.
 */
public class GetMoviesListApiModel extends BaseApiModel implements ParseMoviesStringAsyncTask.OnMovieParserListener {

    public GetMoviesListApiModel(Context context, OnSuccessListener onSuccessListener, OnErrorListener onErrorListener) {
        super(context, Constants.APIOperations.GET_MOVIES_LIST, onSuccessListener, onErrorListener);
        //GetNewsListApiService getNewsListApiService = new GetNewsListApiService(context, this);
        //getNewsListApiService.connect(null);
    }

    @Override
    public void onServiceSuccess(String operationTag, String jsonString) {
        switch (operationTag) {
            case Constants.DataOperations.DATA_RETRIEVED:
                parseMoviesJsonString(jsonString);
                break;
        }
    }

    private void parseMoviesJsonString(String moviesJsonString) {
        new ParseMoviesStringAsyncTask(this).execute(moviesJsonString);
    }

    @Override
    public void onMoviesParsed(ArrayList<Movie> movies) {
        mOnSuccessListener.onSuccess(mOperationTag, (ArrayList) movies);
    }
}
