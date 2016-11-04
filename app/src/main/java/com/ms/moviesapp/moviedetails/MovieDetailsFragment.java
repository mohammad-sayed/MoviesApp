package com.ms.moviesapp.moviedetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.bases.DataFragment;
import com.ms.moviesapp.controllerlayer.GetReviewsListFromDatabaseAsyncTask;
import com.ms.moviesapp.controllerlayer.GetTrailersListFromDatabaseAsyncTask;
import com.ms.moviesapp.controllerlayer.ParseMovieReviewsStringAsyncTask;
import com.ms.moviesapp.controllerlayer.ParseMovieTrailerStringAsyncTask;
import com.ms.moviesapp.entities.ErrorException;
import com.ms.moviesapp.entities.ListType;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;
import com.ms.moviesapp.servicelayer.database.MovieContract;
import com.ms.moviesapp.utils.ImageUtility;
import com.ms.moviesapp.widgets.CustomLinearLayoutManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieDetailsFragment extends DataFragment implements
        MovieDetailsRecyclerViewAdapter.OnDetailsActionListener,
        ParseMovieTrailerStringAsyncTask.OnTrailerParserListener,
        ParseMovieReviewsStringAsyncTask.OnReviewParserListener,
        GetTrailersListFromDatabaseAsyncTask.OnGettingTrailerFromDatabaseListener,
        GetReviewsListFromDatabaseAsyncTask.OnGettingReviewsFromDatabaseListener {

    private static final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();
    private Movie mMovie;
    private TextView mTvMovieTitle;
    private ImageView mIvMovieCover;
    private ImageView mIvMovieBlurBackground;
    private ProgressBar mPbLoading;

    private RecyclerView mRvMovieDetails;
    private MovieDetailsRecyclerViewAdapter mAdapter;


    //private ListView mLvDetails;
    //private ListTypesItemsAdapter mAdapter;
    //private TextView mTvMovieTitle;
    private Uri mYoutubeUri;
    private final String PARAMETER_VIDEO = "v";
    private Menu mMenu;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        if (getArguments() != null) {
            mMovie = (Movie) getArguments().getSerializable(Constants.MOVIE_TAG);
            if (mMovie != null)
                updateViewsData(mMovie);
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.menu_details_fragment, menu);
        // Retrieve the share menu item
        MenuItem menuItem = mMenu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareTrailerIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private void updateSharingOption(boolean hasOptionMenu) {
        setHasOptionsMenu(hasOptionMenu);
    }

    private Intent createShareTrailerIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        Trailer trailer = mAdapter.getTrailers().get(0);
        Uri trailerUri = mYoutubeUri.buildUpon().appendQueryParameter(PARAMETER_VIDEO, trailer.getKey()).build();
        shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUri.toString());
        return shareIntent;
    }


    private void initializeViews(View view) {
        mTvMovieTitle = (TextView) view.findViewById(R.id.tv_movie_title);
        if (mTvMovieTitle != null) {
            mIvMovieCover = (ImageView) view.findViewById(R.id.iv_cover);
        }
        mIvMovieBlurBackground = (ImageView) view.findViewById(R.id.iv_blur_background);
        mPbLoading = (ProgressBar) view.findViewById(R.id.pb_loading);
        mRvMovieDetails = (RecyclerView) view.findViewById(R.id.rv_movie_details);
        LinearLayoutManager linearLayoutManager = new CustomLinearLayoutManager(getActivity());
        mRvMovieDetails.setLayoutManager(linearLayoutManager);
        mRvMovieDetails.setNestedScrollingEnabled(false);
        mRvMovieDetails.setHasFixedSize(false);
        String youtubeUrl = getString(R.string.youtube_url);
        mYoutubeUri = Uri.parse(youtubeUrl);
    }

    public void updateViewsData(Movie movie) {
        mPbLoading.setVisibility(View.VISIBLE);
        mMovie = movie;
        Context context = getContext();
        String coverPath = context.getString(R.string.movie_api_base_url)
                .concat(context.getString(R.string.poster_size_w342))
                .concat(movie.getThumbnail());

        if (mTvMovieTitle != null) {
            mTvMovieTitle.setText(movie.getTitle());
            Picasso.with(context).load(coverPath).placeholder(R.drawable.placeholder_blur).error(R.drawable.placeholder_blur).into(mIvMovieCover);
        }

        if (mIvMovieBlurBackground != null) {
            String posterPath = context.getString(R.string.movie_api_base_url)
                    .concat(context.getString(R.string.poster_size_w342))
                    .concat(movie.getPosterPath());
            Picasso.with(context).load(posterPath).placeholder(R.drawable.background).error(R.drawable.background).into(mIvMovieBlurBackground, new Callback() {
                @Override
                public void onSuccess() {
                    if (getContext() == null)
                        return;
                    Bitmap bitmap = ((BitmapDrawable) mIvMovieBlurBackground.getDrawable()).getBitmap();
                    Bitmap blurredBitmap = ImageUtility.blur(getContext(), bitmap);
                    mIvMovieBlurBackground.setImageBitmap(blurredBitmap);
                }

                @Override
                public void onError() {
                }
            });
        }

        if (mAdapter != null) {
            mAdapter.clearItems();
            mAdapter = null;
        }
        mAdapter = new MovieDetailsRecyclerViewAdapter(getContext(), mMovie, new ArrayList<ListType>(), this);
        mRvMovieDetails.setAdapter(mAdapter);
        ListType listType = new ListType();
        listType.setType(Constants.LIST_TYPE_HEADER);
        listType.setObject(mMovie);
        mAdapter.addItem(listType);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sort_by = sharedPreferences.getString(getString(R.string.movies_sorted_by_key), getString(R.string.popularity_desc));
        if (sort_by.equals(getString(R.string.favorites_value))) {
            new GetTrailersListFromDatabaseAsyncTask(getContext(), this).execute(mMovie.getId());
        } else {
            //True for Trailer
            getTrailers(mMovie.getId());
        }
    }

    private void getTrailers(long movieId) {
        String url = getUrl(getString(R.string.trailers_uri), movieId);
        Map<String, String> params = getParameters();
        fetch(Constants.APIOperations.GET_TRAILERS, url, params);
    }

    private void getReviews(long movieId) {
        String url = getUrl(getString(R.string.reviews_uri), movieId);
        fetch(Constants.APIOperations.GET_REVIEWS, url, getParameters());
    }

    private void updateTrailers(ArrayList<Trailer> trailers) {
        Log.d(LOG_TAG, "Trialers Count: " + trailers.size());
        ArrayList<ListType> listTypes = new ArrayList<>();
        for (Trailer trailer : trailers) {
            ListType listType = new ListType();
            listType.setType(Constants.LIST_TYPE_TRAILER);
            listType.setObject(trailer);
            listTypes.add(listType);
        }
        if (!listTypes.isEmpty())
            mAdapter.addItems(listTypes);
        if (trailers.size() > 0)
            updateSharingOption(true);
        else
            updateSharingOption(false);
    }

    private void updateReviews(ArrayList<Review> reviews) {
        Log.d(LOG_TAG, "Reviews Count: " + reviews.size());
        ArrayList<ListType> listTypes = new ArrayList<>();
        for (Review review : reviews) {
            ListType listType = new ListType();
            listType.setType(Constants.LIST_TYPE_REVIEW);
            listType.setObject(review);
            listTypes.add(listType);
        }
        mAdapter.addItems(listTypes);
    }

    @Override
    protected String getUrl(Object... objects) {
        String requestAttribute = (String) objects[0];
        long movieId = (long) objects[1];
        String url = String.format(getString(R.string.tmdb_api_url).concat(requestAttribute), movieId);
        return url;
    }

    @Override
    protected Map<String, String> getParameters(String... strings) {
        Map<String, String> params = new HashMap<>();
        String apiKeyParameter = getString(R.string.parameter_api_key);
        String apiKey = getString(R.string.tmdb_api_key);
        params.put(apiKeyParameter, apiKey);
        return params;
    }

    @Override
    public void onSuccess(String operationTag, Object... objects) {
        switch (operationTag) {
            case Constants.APIOperations.GET_TRAILERS: {
                String data = (String) objects[0];
                if (data != null)
                    new ParseMovieTrailerStringAsyncTask(this).execute(data);
                else {
                    mPbLoading.setVisibility(View.GONE);
                    toastForLong(R.string.no_available_data);
                }
                break;
            }
            case Constants.APIOperations.GET_REVIEWS: {
                String data = (String) objects[0];
                if (data != null) {
                    new ParseMovieReviewsStringAsyncTask(this).execute(data);
                } else {
                    mPbLoading.setVisibility(View.GONE);
                    toastForLong(R.string.no_available_data);
                }
                break;
            }
        }
    }

    @Override
    public void onError(String operationTag, ErrorException error) {
        switch (operationTag) {
            case Constants.APIOperations.GET_TRAILERS:
            case Constants.APIOperations.GET_REVIEWS:
                mPbLoading.setVisibility(View.GONE);
                break;
        }
        toastForLong(error.getMessage());
    }

    @Override
    public void onTrailersParsed(ArrayList<Trailer> trailers) {
        updateTrailers(trailers);
        getReviews(mMovie.getId());
    }

    @Override
    public void onReviewsParsed(ArrayList<Review> reviews) {
        updateReviews(reviews);
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onTrailersRetrieved(ArrayList<Trailer> trailers) {
        updateTrailers(trailers);
        new GetReviewsListFromDatabaseAsyncTask(getContext(), this).execute(mMovie.getId());
    }

    @Override
    public void onReviewsRetrieved(ArrayList<Review> reviews) {
        updateReviews(reviews);
        mPbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onFavoritePressed(boolean favorite) {
        Context context = getContext();
        //if exist remove from favorite
        if (favorite) {
            MovieContract.deleteMovie(context, mMovie.getId());
        }
        //else add to favorites
        else {
            MovieContract.insertMovie(context, mMovie, mAdapter.getTrailers(), mAdapter.getReviews());
        }
    }

    @Override
    public void onTrailerPressed(Trailer trailer) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(mYoutubeUri.buildUpon().appendQueryParameter(PARAMETER_VIDEO, trailer.getKey()).build());
        startActivity(intent);
    }

}
