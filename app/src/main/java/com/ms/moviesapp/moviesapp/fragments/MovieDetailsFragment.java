package com.ms.moviesapp.moviesapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.moviesapp.moviesapp.Constants;
import com.ms.moviesapp.moviesapp.R;
import com.ms.moviesapp.moviesapp.entities.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieDetailsFragment extends Fragment {

    private Movie mMovie;
    private TextView mTvMovieTitle;
    private ImageView mIvMoviePoster;
    private TextView mTvReleaseDate;
    private TextView mTvMovieAvgRate;
    private TextView mTvMovieSynopsis;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews();
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(Constants.MOVIE_TAG);
            updateViewsData(mMovie);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovie != null)
            updateViewsData(mMovie);
    }

    private void initializeViews() {
        View view = getView();
        mTvMovieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
        mIvMoviePoster = (ImageView) view.findViewById(R.id.iv_movie_poster);
        mTvReleaseDate = (TextView) view.findViewById(R.id.tv_release_date);
        mTvMovieAvgRate = (TextView) view.findViewById(R.id.tv_voting_average);
        mTvMovieSynopsis = (TextView) view.findViewById(R.id.tv_movie_synopsis);
    }

    public void updateViewsData(Movie movie) {
        Context context = getContext();
        mTvMovieTitle.setText(movie.getTitle());
        String posterPath = context.getString(R.string.movie_api_base_url)
                .concat(context.getString(R.string.poster_size_w780))
                .concat(movie.getPosterPath());
        Picasso.with(context).load(posterPath).error(R.drawable.no_poster).into(mIvMoviePoster);
        mTvReleaseDate.setText(movie.getReleaseDate());
        mTvMovieAvgRate.setText(String.format(getString(R.string.out_of_ten), movie.getUsersRating()));
        mTvMovieSynopsis.setText(movie.getSynopsis());
    }
}
