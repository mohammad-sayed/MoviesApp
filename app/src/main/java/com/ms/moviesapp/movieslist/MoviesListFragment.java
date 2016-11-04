package com.ms.moviesapp.movieslist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.bases.DataFragment;
import com.ms.moviesapp.controllerlayer.GetMoviesListFromDatabaseAsyncTask;
import com.ms.moviesapp.controllerlayer.ParseMoviesStringAsyncTask;
import com.ms.moviesapp.entities.ErrorException;
import com.ms.moviesapp.entities.Movie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MoviesListFragment extends DataFragment implements AdapterView.OnItemClickListener,
        ParseMoviesStringAsyncTask.OnMovieParserListener,
        GetMoviesListFromDatabaseAsyncTask.OnGettingMoviesFromDatabaseListener {

    private final String LOG_TAG = MoviesListFragment.class.getSimpleName();
    private OnMoviesListRetrieved mOnMoviesListRetrieved;
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
        mOnMoviesListRetrieved = castInterface(OnMoviesListRetrieved.class, context);
        mOnMovieSelectedListener = castInterface(OnMovieSelectedListener.class, context);
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

    public interface OnMoviesListRetrieved {
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
            mPbLoading.setVisibility(View.VISIBLE);
            new GetMoviesListFromDatabaseAsyncTask(getContext(), this).execute();
        } else {
            getData(sortBy);
        }
    }

    private void getData(String sortBy) {
        mPbLoading.setVisibility(View.VISIBLE);
        fetch(Constants.APIOperations.GET_MOVIES_LIST, getUrl(), getParameters(sortBy));
    }

    @Override
    protected String getUrl(Object... objects) {
        return getString(R.string.tmdb_api_url).concat(getString(R.string.discover_movies_uri));
    }

    @Override
    protected Map<String, String> getParameters(String... strings) {
        Map<String, String> params = new HashMap<>();
        String sortByParameter = getString(R.string.parameter_sort_by);
        String sortBy = strings[0];
        String apiKeyParameter = getString(R.string.parameter_api_key);
        String apiKey = getString(R.string.tmdb_api_key);
        params.put(sortByParameter, sortBy);
        params.put(apiKeyParameter, apiKey);
        return params;
    }

    @Override
    public void onSuccess(String operationTag, Object... objects) {
        switch (operationTag) {
            case Constants.APIOperations.GET_MOVIES_LIST: {
                String data = (String) objects[0];
                if (data != null)
                    new ParseMoviesStringAsyncTask(this).execute(data);
                else {
                    mPbLoading.setVisibility(View.GONE);
                    toastForLong(R.string.no_available_data);
                }
                break;
                /*ArrayList<Movie> movies = (ArrayList<Movie>) objects[0];
                if (movies.isEmpty()) {
                    Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(LOG_TAG, "Movies Count: " + movies.size());
                    mMoviesItemsAdapter.setItems(movies);
                    notifyMoviesRetrieved();
                }
                mPbLoading.setVisibility(View.GONE);
                break;*/
            }
        }
    }

    @Override
    public void onError(String operationTag, ErrorException error) {
        switch (operationTag) {
            case Constants.APIOperations.GET_MOVIES_LIST:
                mPbLoading.setVisibility(View.INVISIBLE);
                break;
        }
        toastForLong(error.getMessage());
    }

    @Override
    public void onMoviesParsed(ArrayList<Movie> movies) {
        updateMovies(movies);
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onMoviesRetrieved(ArrayList<Movie> movies) {
        updateMovies(movies);
        mPbLoading.setVisibility(View.GONE);
    }

    private void updateMovies(ArrayList<Movie> movies) {
        if (movies.isEmpty()) {
            Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
        } else {
            Log.d(LOG_TAG, "Movies Count: " + movies.size());
            mMoviesItemsAdapter.setItems(movies);
            notifyMoviesRetrieved();
        }
    }

    private void notifyMoviesRetrieved() {
        if (mOnMoviesListRetrieved != null)
            mOnMoviesListRetrieved.onMoviesRetrieved();
    }

    public void selectMovie(int position) {
        mGVMovies.setSelection(position);
        if (position <= mGVMovies.getCount() - 1)
            mGVMovies.performItemClick(mGVMovies.getChildAt(position), position, mMoviesItemsAdapter.getItemId(position));
    }
}
