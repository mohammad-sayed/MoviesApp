package com.ms.moviesapp.fragments;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.moviesapp.R;
import com.ms.moviesapp.adapters.MoviesItemsAdapter;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.servicelayer.FetchDataAsyncTask;
import com.ms.moviesapp.servicelayer.database.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesListFragment extends Fragment implements AdapterView.OnItemClickListener, FetchDataAsyncTask.DataFetchedListener {

    private final String LOG_TAG = MoviesListFragment.class.getSimpleName();
    private OnMovieListRetrieved mOnMovieListRetrieved;
    private OnMovieSelectedListener mOnMovieSelectedListener;
    private GridView mGVMovies;
    private ProgressBar mPbLoading;
    private MoviesItemsAdapter mMoviesItemsAdapter;
    private List<Movie> mMoviesList;
    //private RecyclerView mRVMovie;
    //private final int NUM_OF_COLUMNS_IN_GRID_VIEW = 2;
    //private MoviesRecyclerViewAdapter mMoviesRecyclerViewAdapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnMovieListRetrieved = castInterface(OnMovieListRetrieved.class, context);
        mOnMovieSelectedListener = castInterface(OnMovieSelectedListener.class, context);
    }

    private <T> T castInterface(Class<T> interfaceClass, Object object) {
        T interfaceObject = null;
        try {
            interfaceObject = (T) object;
        } catch (ClassCastException ex) {
            throw new ClassCastException(object.getClass().getSimpleName() + " must implement " + interfaceClass.getClass().getSimpleName());
        }
        return interfaceObject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mGVMovies = (GridView) view.findViewById(R.id.gv_movies);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        mMoviesList = new ArrayList<>();
        //int imageWidth = view.getLayoutParams().width / getResources().getInteger(R.integer.number_of_columns);
        mMoviesItemsAdapter = new MoviesItemsAdapter(getContext(), mMoviesList);
        mGVMovies.setAdapter(mMoviesItemsAdapter);
        mGVMovies.setOnItemClickListener(this);
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

    public interface OnMovieSelectedListener {
        void onMovieSelected(Movie movie, int position);
    }

    public interface OnMovieListRetrieved {
        void onMoviesRetrieved();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = (Movie) mGVMovies.getItemAtPosition(position);
        if (mOnMovieSelectedListener != null)
            mOnMovieSelectedListener.onMovieSelected(movie, position);
    }

    private void getMoviesList() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortBy = sharedPreferences.getString(getString(R.string.movies_sorted_by_key), getString(R.string.popularity_desc));
        if (sortBy.equals(getString(R.string.favorites_value))) {
            new GetMoviesListFromDatabase().execute();
        } else {
            getData(sortBy);
        }
    }

    private void getData(String sortBy) {
        String tmdbApiUrl = getString(R.string.tmdb_api_url);
        String discoverMoviesUri = getString(R.string.discover_movies_uri);
        String sortByParameter = getString(R.string.parameter_sort_by);
        String apiKeyParameter = getString(R.string.parameter_api_key);
        String apiKey = getString(R.string.tmdb_api_key);
        Uri uri = Uri.parse(tmdbApiUrl.concat(discoverMoviesUri)).buildUpon().appendQueryParameter(sortByParameter, sortBy)
                .appendQueryParameter(apiKeyParameter, apiKey).build();
        mPbLoading.setVisibility(View.VISIBLE);
        new FetchDataAsyncTask(this).execute(uri.toString());
    }

    @Override
    public void onDataFetched(String data) {
        Log.d(LOG_TAG, "Data retrieved");
        if (data != null)
            new ParseMoviesStringAsyncTask().execute(data);
        else {
            mPbLoading.setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataFetchError(String error) {
        mPbLoading.setVisibility(View.INVISIBLE);
    }


    private class ParseMoviesStringAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = ParseMoviesStringAsyncTask.class.getSimpleName();

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
            Log.d(LOG_TAG, "Movies Count: " + movies.size());
            mPbLoading.setVisibility(View.INVISIBLE);
            mMoviesItemsAdapter.setItems(movies);
            notifyMoviesRetrieved();
        }
    }

    private class GetMoviesListFromDatabase extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final String LOG_TAG = GetMoviesListFromDatabase.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            mPbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... params) {
            return MovieContract.getAllMovies(getContext());
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            Log.d(LOG_TAG, "Data Retrieved from DB");
            Log.d(LOG_TAG, "Movies Count: " + movies.size());
            mPbLoading.setVisibility(View.INVISIBLE);
            mMoviesItemsAdapter.setItems(movies);
            notifyMoviesRetrieved();
        }
    }

    private void notifyMoviesRetrieved() {
        if (mOnMovieListRetrieved != null)
            mOnMovieListRetrieved.onMoviesRetrieved();
    }

    public void selectMovie(int position) {
        mGVMovies.setSelection(position);
        if (position <= mGVMovies.getCount() - 1)
            mGVMovies.performItemClick(mGVMovies.getChildAt(position), position, mMoviesItemsAdapter.getItemId(position));
    }
}
