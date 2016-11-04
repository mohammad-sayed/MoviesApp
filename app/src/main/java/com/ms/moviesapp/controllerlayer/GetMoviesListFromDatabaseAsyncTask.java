package com.ms.moviesapp.controllerlayer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.servicelayer.database.MovieContract;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */
public class GetMoviesListFromDatabaseAsyncTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private final String LOG_TAG = GetMoviesListFromDatabaseAsyncTask.class.getSimpleName();
    private Context mContext;
    private OnGettingMoviesFromDatabaseListener mOnGettingMoviesFromDatabaseListener;

    public GetMoviesListFromDatabaseAsyncTask(Context context, @NonNull OnGettingMoviesFromDatabaseListener onGettingMoviesFromDatabaseListener) {
        this.mContext = context;
        this.mOnGettingMoviesFromDatabaseListener = onGettingMoviesFromDatabaseListener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        return MovieContract.getAllMovies(mContext);
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        Log.d(LOG_TAG, "Data Retrieved from DB");
        mOnGettingMoviesFromDatabaseListener.onMoviesRetrieved(movies);
    }

    public interface OnGettingMoviesFromDatabaseListener {
        void onMoviesRetrieved(ArrayList<Movie> movies);
    }
}
