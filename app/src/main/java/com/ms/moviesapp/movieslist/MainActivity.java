package com.ms.moviesapp.movieslist;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.moviedetails.DetailsActivity;
import com.ms.moviesapp.R;
import com.ms.moviesapp.settings.SettingsActivity;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.moviedetails.MovieDetailsFragment;
import com.ms.moviesapp.utils.ImageUtility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements MoviesListFragment.OnMovieSelectedListener,
        MoviesListFragment.OnMoviesListRetrieved {

    private ImageView mIvMovieBlurBackground;
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
        mIvMovieBlurBackground = (ImageView) findViewById(R.id.iv_blur_background);
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
            updateBackgroundImage(movie);
            mMovieDetailsFragment.updateViewsData(movie);
        }
    }

    private void updateBackgroundImage(Movie movie) {
        String posterPath = getString(R.string.movie_api_base_url)
                .concat(getString(R.string.poster_size_w342))
                .concat(movie.getPosterPath());
        Picasso.with(this).load(posterPath).placeholder(R.drawable.background).error(R.drawable.background).into(mIvMovieBlurBackground, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) mIvMovieBlurBackground.getDrawable()).getBitmap();
                Bitmap blurredBitmap = ImageUtility.blur(MainActivity.this, bitmap);
                mIvMovieBlurBackground.setImageBitmap(blurredBitmap);
            }

            @Override
            public void onError() {
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.SELECTED_MOVIE_POSITION_TAG, mSelectedItemPosition);
        super.onSaveInstanceState(outState);
    }
}
