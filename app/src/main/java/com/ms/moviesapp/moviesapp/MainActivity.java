package com.ms.moviesapp.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ms.moviesapp.moviesapp.entities.Movie;
import com.ms.moviesapp.moviesapp.fragments.MovieDetailsFragment;
import com.ms.moviesapp.moviesapp.fragments.MoviesListFragment;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.OnMovieSelectedListener {

    private MovieDetailsFragment mMovieDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        MoviesListFragment moviesListFragment = (MoviesListFragment) fragmentManager
                .findFragmentById(R.id.movies_list_fragment);
        moviesListFragment.setItemSelectedListener(this);

        mMovieDetailsFragment = (MovieDetailsFragment) fragmentManager
                .findFragmentById(R.id.movie_details_fragment);
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
    public void onMovieSelected(Movie movie) {
        if (mMovieDetailsFragment == null) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_TAG, movie);
            startActivity(intent);
        } else {
            mMovieDetailsFragment.updateViewsData(movie);
        }
    }
}
