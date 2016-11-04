package com.ms.moviesapp.controllerlayer;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.servicelayer.database.MovieContract;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */
public class GetReviewsListFromDatabaseAsyncTask extends AsyncTask<Long, Void, ArrayList<Review>> {
    private final String LOG_TAG = GetReviewsListFromDatabaseAsyncTask.class.getSimpleName();
    private Context mContext;
    private OnGettingReviewsFromDatabaseListener mOnGettingReviewsFromDatabaseListener;

    public GetReviewsListFromDatabaseAsyncTask(Context context, @NonNull OnGettingReviewsFromDatabaseListener onGettingReviewsFromDatabaseListener) {
        this.mContext = context;
        this.mOnGettingReviewsFromDatabaseListener = onGettingReviewsFromDatabaseListener;
    }

    @Override
    protected ArrayList<Review> doInBackground(Long... params) {
        long movieId = params[0];
        return MovieContract.getReviewsOfMovie(mContext, movieId);
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        Log.d(LOG_TAG, "Data parsed");
        mOnGettingReviewsFromDatabaseListener.onReviewsRetrieved(reviews);
    }

    public interface OnGettingReviewsFromDatabaseListener {
        void onReviewsRetrieved(ArrayList<Review> reviews);
    }
}
