package com.ms.moviesapp.controllerlayer;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ms.moviesapp.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 11/3/2016.
 */
public class ParseMoviesStringAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = ParseMoviesStringAsyncTask.class.getSimpleName();

    private OnMovieParserListener mOnMovieParserListener;

    public ParseMoviesStringAsyncTask(@NonNull OnMovieParserListener onMovieParserListener) {
        this.mOnMovieParserListener = onMovieParserListener;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        String moviesJsonString = params[0];
        ArrayList<Movie> moviesList = new ArrayList<>();

        final String MOVIE_ID = "id";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String VOTE_AVERAGE = "vote_average";
        final String RELEASE_DATE = "release_date";

        try {
            JSONObject moviesObject = new JSONObject(moviesJsonString);
            JSONArray moviesListArray = moviesObject.getJSONArray("results");
            for (int i = 0; i < moviesListArray.length(); i++) {
                JSONObject movieObject = moviesListArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setId(movieObject.getLong(MOVIE_ID));
                movie.setPosterPath(movieObject.getString(POSTER_PATH));
                movie.setSynopsis(movieObject.getString(OVERVIEW));
                movie.setTitle(movieObject.getString(TITLE));
                movie.setThumbnail(movieObject.getString(BACKDROP_PATH));
                movie.setUsersRating((float) movieObject.getDouble(VOTE_AVERAGE));
                movie.setReleaseDate(movieObject.getString(RELEASE_DATE));
                moviesList.add(movie);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return moviesList;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        Log.d(LOG_TAG, "Data parsed");
        mOnMovieParserListener.onMoviesParsed(movies);

    }

    public interface OnMovieParserListener {
        void onMoviesParsed(ArrayList<Movie> movies);
    }
}
