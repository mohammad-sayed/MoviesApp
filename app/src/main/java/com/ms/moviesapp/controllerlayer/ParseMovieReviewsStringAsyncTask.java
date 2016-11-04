package com.ms.moviesapp.controllerlayer;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */
public class ParseMovieReviewsStringAsyncTask extends AsyncTask<String, Void, ArrayList<Review>> {

    private final String LOG_TAG = ParseMovieTrailerStringAsyncTask.class.getSimpleName();

    private OnReviewParserListener mOnReviewParserListener;

    public ParseMovieReviewsStringAsyncTask(@NonNull OnReviewParserListener onReviewParserListener) {
        this.mOnReviewParserListener = onReviewParserListener;
    }

    @Override
    protected ArrayList<Review> doInBackground(String... params) {
        String moviesJsonString = params[0];
        ArrayList<Review> reviews = new ArrayList<>();

        final String REVIEW_ID = "id";
        final String REVIEW_AUTHOR = "author";
        final String REVIEW_CONTENT = "content";

        try {
            JSONObject moviesObject = new JSONObject(moviesJsonString);
            JSONArray moviesListArray = moviesObject.getJSONArray("results");
            for (int i = 0; i < moviesListArray.length(); i++) {
                JSONObject trailerObject = moviesListArray.getJSONObject(i);
                Review review = new Review();
                review.setId(trailerObject.getString(REVIEW_ID));
                review.setAuthor(trailerObject.getString(REVIEW_AUTHOR));
                review.setContent(trailerObject.getString(REVIEW_CONTENT));
                reviews.add(review);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return reviews;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> reviews) {
        Log.d(LOG_TAG, "Data parsed");
        mOnReviewParserListener.onReviewsParsed(reviews);
    }

    public interface OnReviewParserListener {
        void onReviewsParsed(ArrayList<Review> reviews);
    }

}
