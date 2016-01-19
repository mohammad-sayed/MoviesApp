package com.ms.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.fragments.MovieDetailsFragment;
import com.ms.moviesapp.fragments.MoviesListFragment;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.OnMovieSelectedListener,
        MoviesListFragment.OnMovieListRetrieved {

    private MovieDetailsFragment mMovieDetailsFragment;
    private MoviesListFragment mMoviesListFragment;
    private int mSelectedItemPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            mSelectedItemPosition = savedInstanceState.getInt(Constants.SELECTED_MOVIE_POSITION_TAG);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        mMoviesListFragment = (MoviesListFragment) fragmentManager
                .findFragmentById(R.id.movies_list_fragment);
        mMovieDetailsFragment = (MovieDetailsFragment) fragmentManager
                .findFragmentById(R.id.movie_details_fragment);
        if (mMovieDetailsFragment != null && mMovieDetailsFragment.getView() != null)
            mMovieDetailsFragment.getView().setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMoviesRetrieved() {
        if (mMovieDetailsFragment != null && mMovieDetailsFragment.getView() != null) {
            mMovieDetailsFragment.getView().setVisibility(View.VISIBLE);
            mMoviesListFragment.selectMovie(mSelectedItemPosition);
        }
    }

    @Override
    public void onMovieSelected(Movie movie, int position) {
        if (mMovieDetailsFragment == null) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_TAG, movie);
            startActivity(intent);
        } else {
            mSelectedItemPosition = position;
            mMovieDetailsFragment.updateViewsData(movie);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.SELECTED_MOVIE_POSITION_TAG, mSelectedItemPosition);
        super.onSaveInstanceState(outState);
    }
}
