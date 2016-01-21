package com.ms.moviesapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.adapters.MovieDetailsRecyclerViewAdapter;
import com.ms.moviesapp.entities.ListType;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;
import com.ms.moviesapp.servicelayer.FetchDataAsyncTask;
import com.ms.moviesapp.servicelayer.database.MovieContract;
import com.ms.moviesapp.utils.ImageUtility;
import com.ms.moviesapp.widgets.CustomLinearLayoutManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieDetailsFragment extends Fragment implements
        MovieDetailsRecyclerViewAdapter.OnDetailsActionListener {

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
            new GetTrailersListFromDatabaseAsyncTask().execute(mMovie.getId());
        } else {
            //True for Trailer
            getTrailersOrReviews(mMovie.getId(), getString(R.string.trailers_uri), mTrailersDataFetchedListener);
        }
    }

    FetchDataAsyncTask.DataFetchedListener mTrailersDataFetchedListener = new FetchDataAsyncTask.DataFetchedListener() {
        @Override
        public void onDataFetched(String data) {
            Log.d(LOG_TAG, "Data retrieved");
            if (data != null)
                new ParseMovieTrailerStringAsyncTask().execute(data);
            else {
                mPbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDataFetchError(String error) {
            mPbLoading.setVisibility(View.INVISIBLE);
        }
    };

    FetchDataAsyncTask.DataFetchedListener mReviewsDataFetchedListener = new FetchDataAsyncTask.DataFetchedListener() {
        @Override
        public void onDataFetched(String data) {
            Log.d(LOG_TAG, "Data retrieved");
            if (data != null) {
                new ParseMovieReviewsStringAsyncTask().execute(data);
            } else {
                mPbLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), R.string.no_available_data, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onDataFetchError(String error) {
            mPbLoading.setVisibility(View.INVISIBLE);
        }
    };

    private void getTrailersOrReviews(long movieId, String requestAttribute, FetchDataAsyncTask.DataFetchedListener listener) {
        String tmdbApiUrl = getString(R.string.tmdb_api_url);
        String requestUrl = String.format(tmdbApiUrl.concat(requestAttribute), movieId);
        String apiKeyParameter = getString(R.string.parameter_api_key);
        String apiKey = getString(R.string.tmdb_api_key);
        Uri uri = Uri.parse(requestUrl).buildUpon().appendQueryParameter(apiKeyParameter, apiKey).build();
        new FetchDataAsyncTask(listener).execute(uri.toString());
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

    private class ParseMovieTrailerStringAsyncTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        private final String LOG_TAG = ParseMovieTrailerStringAsyncTask.class.getSimpleName();

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            String moviesJsonString = params[0];
            ArrayList<Trailer> trailers = new ArrayList<>();

            final String TRAILER_ID = "id";
            final String TRAILER_KEY = "key";
            final String TRAILER_NAME = "name";
            final String TRAILER_SITE = "site";
            final String TRAILER_TYPE = "type";

            try {
                JSONObject moviesObject = new JSONObject(moviesJsonString);
                JSONArray moviesListArray = moviesObject.getJSONArray("results");
                for (int i = 0; i < moviesListArray.length(); i++) {
                    JSONObject trailerObject = moviesListArray.getJSONObject(i);
                    Trailer trailer = new Trailer();
                    trailer.setId(trailerObject.getString(TRAILER_ID));
                    trailer.setKey(trailerObject.getString(TRAILER_KEY));
                    trailer.setName(trailerObject.getString(TRAILER_NAME));
                    trailer.setSite(trailerObject.getString(TRAILER_SITE));
                    trailer.setType(trailerObject.getString(TRAILER_TYPE));
                    trailers.add(trailer);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return trailers;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            Log.d(LOG_TAG, "Data parsed");
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
            getTrailersOrReviews(mMovie.getId(), getString(R.string.reviews_uri), mReviewsDataFetchedListener);
        }
    }

    private class ParseMovieReviewsStringAsyncTask extends AsyncTask<String, Void, ArrayList<Review>> {

        private final String LOG_TAG = ParseMovieTrailerStringAsyncTask.class.getSimpleName();

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            String moviesJsonString = params[0];
            ArrayList<Review> reviews = new ArrayList<>();

            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";

            try {
                JSONObject moviesObject = new JSONObject(moviesJsonString);
                JSONArray moviesListArray = moviesObject.getJSONArray("results");
                for (int i = 0; i < moviesListArray.length(); i++) {
                    JSONObject trailerObject = moviesListArray.getJSONObject(i);
                    Review review = new Review();
                    review.setId(trailerObject.getString(REVIEW_ID));
                    review.setAuthor(trailerObject.getString(REVIEW_AUTHOR));
                    review.setContent(trailerObject.getString(REVIEW_CONTENT));
                    reviews.add(review);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return reviews;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> trailers) {
            Log.d(LOG_TAG, "Data parsed");
            Log.d(LOG_TAG, "Reviews Count: " + trailers.size());
            ArrayList<ListType> listTypes = new ArrayList<>();
            for (Review review : trailers) {
                ListType listType = new ListType();
                listType.setType(Constants.LIST_TYPE_REVIEW);
                listType.setObject(review);
                listTypes.add(listType);
            }
            mAdapter.addItems(listTypes);
            mPbLoading.setVisibility(View.INVISIBLE);

            //mPbLoading.setVisibility(View.INVISIBLE);
            //mMoviesItemsAdapter.setItems(movies);
        }
    }

    private class GetTrailersListFromDatabaseAsyncTask extends AsyncTask<Long, Void, ArrayList<Trailer>> {
        private final String LOG_TAG = GetTrailersListFromDatabaseAsyncTask.class.getSimpleName();

        @Override
        protected ArrayList<Trailer> doInBackground(Long... params) {
            long movieId = params[0];
            return MovieContract.getTrailersByMovieId(getContext(), movieId);
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            Log.d(LOG_TAG, "Data parsed");
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
            new GetReviewsListFromDatabaseAsyncTask().execute(mMovie.getId());
            //mPbLoading.setVisibility(View.INVISIBLE);
            //mMoviesItemsAdapter.setItems(movies);
        }
    }

    private class GetReviewsListFromDatabaseAsyncTask extends AsyncTask<Long, Void, ArrayList<Review>> {
        private final String LOG_TAG = GetReviewsListFromDatabaseAsyncTask.class.getSimpleName();

        @Override
        protected ArrayList<Review> doInBackground(Long... params) {
            long movieId = params[0];
            return MovieContract.getReviewsOfMovie(getContext(), movieId);
        }

        @Override
        protected void onPostExecute(ArrayList<Review> trailers) {
            Log.d(LOG_TAG, "Data parsed");
            Log.d(LOG_TAG, "Reviews Count: " + trailers.size());
            ArrayList<ListType> listTypes = new ArrayList<>();
            for (Review review : trailers) {
                ListType listType = new ListType();
                listType.setType(Constants.LIST_TYPE_REVIEW);
                listType.setObject(review);
                listTypes.add(listType);
            }
            mAdapter.addItems(listTypes);
            mPbLoading.setVisibility(View.INVISIBLE);
            //mMoviesItemsAdapter.setItems(movies);
        }
    }

}
