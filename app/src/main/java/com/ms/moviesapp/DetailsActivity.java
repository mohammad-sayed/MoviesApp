package com.ms.moviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.fragments.MovieDetailsFragment;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mActionToolbar;
    private ImageView mIvMovieCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mActionToolbar = (Toolbar) mCollapsingToolbarLayout.findViewById(R.id.toolbar);
        setSupportActionBar(mActionToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.home_up);
        actionBar.setHomeButtonEnabled(true);
        mIvMovieCover = (ImageView) mCollapsingToolbarLayout.findViewById(R.id.iv_cover);
        if (getIntent() != null && getIntent().hasExtra(Constants.MOVIE_TAG)) {
            Movie movie = (Movie) getIntent().getSerializableExtra(Constants.MOVIE_TAG);

            String coverPath = getString(R.string.movie_api_base_url)
                    .concat(getString(R.string.poster_size_w780))
                    .concat(movie.getThumbnail());

            mCollapsingToolbarLayout.setTitle(movie.getTitle());
            //mTvMovieTitle.setText(movie.getTitle());
            Picasso.with(this).load(coverPath).placeholder(R.drawable.placeholder_blur).error(R.drawable.placeholder_blur).into(mIvMovieCover);

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
