package com.ms.moviesapp.moviesapp.fragments;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.moviesapp.moviesapp.R;
import com.ms.moviesapp.moviesapp.adapters.MoviesItemsAdapter;
import com.ms.moviesapp.moviesapp.entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesListFragment extends Fragment implements MoviesItemsAdapter.OnMovieSelectedListener {

    private OnMovieSelectedListener mOnMovieSelectedListener;
    private GridView mGVMovies;
    private ProgressBar mPbLoading;
    private MoviesItemsAdapter mMoviesItemsAdapter;
    private List<Movie> mMoviesList;
    //private RecyclerView mRVMovie;
    //private final int NUM_OF_COLUMNS_IN_GRID_VIEW = 2;
    //private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mGVMovies = (GridView) view.findViewById(R.id.gv_movies);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        mMoviesList = new ArrayList<>();
        mMoviesItemsAdapter = new MoviesItemsAdapter(getContext(), mMoviesList, this);
        mGVMovies.setAdapter
                (mMoviesItemsAdapter);
        /*mRVMovie = (RecyclerView) view.findViewById(R.id.movies_recycler_view);
        mRVMovie.setHasFixedSize(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), NUM_OF_COLUMNS_IN_GRID_VIEW);
        mRVMovie.setLayoutManager(gridLayoutManager);
        mRVMovie.addItemDecoration(new DividerItemDecoration);
        mRVMovie.setAdapter(mMoviesRecyclerViewAdapter);*/
        //mMoviesRecyclerViewAdapter = new MoviesRecyclerViewAdapter(getActivity(), mMoviesList, MoviesListFragment.this);

    }

    @Override
    public void onStart() {
        super.onStart();
        getMoviesList();
    }

    public static interface OnMovieSelectedListener {
        public void onMovieSelected(Movie movie);
    }

    @Override
    public void onMovieSelected(Movie movie) {
        if (mOnMovieSelectedListener != null)
            mOnMovieSelectedListener.onMovieSelected(movie);
    }

    public void setItemSelectedListener(OnMovieSelectedListener onMovieSelectedListener) {
        this.mOnMovieSelectedListener = onMovieSelectedListener;
    }

    private void getMoviesList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort_by = sharedPreferences.getString(getString(R.string.movies_sorted_by_key), getString(R.string.popularity_desc));
        new GetMoviesListAsyncTask().execute(sort_by);
    }

    private class GetMoviesListAsyncTask extends AsyncTask<String, Void, String> {

        private final String LOG_TAG = GetMoviesListAsyncTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mPbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            String tmdbApiUrl = getString(R.string.tmdb_api_url);
            String sortByParameter = getString(R.string.parameter_sort_by);
            String sortBy = params[0];
            String apiKeyParameter = getString(R.string.parameter_api_key);
            String apiKey = getString(R.string.tmdb_api_key);

            String moviesJsonString = null;
            HttpURLConnection httpURLConnection = null;
            BufferedReader bufferedInputStream = null;

            try {
                Uri uri = Uri.parse(tmdbApiUrl).buildUpon().appendQueryParameter(sortByParameter, sortBy)
                        .appendQueryParameter(apiKeyParameter, apiKey).build();
                Log.d(LOG_TAG, uri.toString());
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();

                StringBuilder stringBuilder = new StringBuilder();
                if (inputStream == null)
                    return null;
                bufferedInputStream = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedInputStream.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                if (stringBuilder.length() == 0)
                    return null;

                moviesJsonString = stringBuilder.toString();


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();

                if (bufferedInputStream != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return moviesJsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(LOG_TAG, "Data retrieved");
            if (s != null)
                new ParseMoviesStringAsyncTask().execute(s);
            else {
                mPbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class ParseMoviesStringAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = ParseMoviesStringAsyncTask.class.getSimpleName();

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            String moviesJsonString = params[0];
            ArrayList<Movie> moviesList = new ArrayList<>();

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
            Log.d(LOG_TAG, "Movies Count: " + movies.size());
            mPbLoading.setVisibility(View.INVISIBLE);
            mMoviesItemsAdapter.setItems(movies);
        }
    }

}
