package com.ms.moviesapp.moviesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ms.moviesapp.moviesapp.entities.Movie;
import com.ms.moviesapp.moviesapp.fragments.MovieDetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_TAG)) {
            Movie movie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_TAG);
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.MOVIE_TAG, movie);
            movieDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_container, movieDetailsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
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
}
