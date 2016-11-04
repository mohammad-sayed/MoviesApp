package com.ms.moviesapp.controllerlayer;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/4/2016.
 */
public class ParseMovieTrailerStringAsyncTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = ParseMovieTrailerStringAsyncTask.class.getSimpleName();
    private OnTrailerParserListener mOnTrailerParserListener;

    public ParseMovieTrailerStringAsyncTask(@NonNull OnTrailerParserListener onTrailerParserListener) {
        this.mOnTrailerParserListener = onTrailerParserListener;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... params) {
        String moviesJsonString = params[0];
        ArrayList<Trailer> trailers = new ArrayList<>();

        final String TRAILER_ID = "id";
        final String TRAILER_KEY = "key";
        final String TRAILER_NAME = "name";
        final String TRAILER_SITE = "site";
        final String TRAILER_TYPE = "type";

        try {
            JSONObject moviesObject = new JSONObject(moviesJsonString);
            JSONArray moviesListArray = moviesObject.getJSONArray("results");
            for (int i = 0; i < moviesListArray.length(); i++) {
                JSONObject trailerObject = moviesListArray.getJSONObject(i);
                Trailer trailer = new Trailer();
                trailer.setId(trailerObject.getString(TRAILER_ID));
                trailer.setKey(trailerObject.getString(TRAILER_KEY));
                trailer.setName(trailerObject.getString(TRAILER_NAME));
                trailer.setSite(trailerObject.getString(TRAILER_SITE));
                trailer.setType(trailerObject.getString(TRAILER_TYPE));
                trailers.add(trailer);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return trailers;
    }

    @Override
    protected void onPostExecute(ArrayList<Trailer> trailers) {
        Log.d(LOG_TAG, "Data parsed");
        mOnTrailerParserListener.onTrailersParsed(trailers);
    }

    public interface OnTrailerParserListener {
        void onTrailersParsed(ArrayList<Trailer> trailers);
    }
}
