package com.ms.moviesapp.controllerlayer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Trailer;
import com.ms.moviesapp.servicelayer.database.MovieContract;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */
public class GetTrailersListFromDatabaseAsyncTask extends AsyncTask<Long, Void, ArrayList<Trailer>> {
    private Context mContext;
    private final String LOG_TAG = GetTrailersListFromDatabaseAsyncTask.class.getSimpleName();
    private OnGettingTrailerFromDatabaseListener mOnGettingTrailerFromDatabaseListener;

    public GetTrailersListFromDatabaseAsyncTask(Context context, @NonNull OnGettingTrailerFromDatabaseListener onGettingTrailerFromDatabaseListener) {
        this.mContext = context;
        this.mOnGettingTrailerFromDatabaseListener = onGettingTrailerFromDatabaseListener;
    }


    @Override
    protected ArrayList<Trailer> doInBackground(Long... params) {
        long movieId = params[0];
        return MovieContract.getTrailersByMovieId(mContext, movieId);
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        Log.d(LOG_TAG, "Data parsed");
        mOnGettingTrailerFromDatabaseListener.onTrailersRetrieved(trailers);
        //mPbLoading.setVisibility(View.INVISIBLE);
        //mMoviesItemsAdapter.setItems(movies);
    }

    public interface OnGettingTrailerFromDatabaseListener {
        void onTrailersRetrieved(ArrayList<Trailer> trailers);
    }
}
