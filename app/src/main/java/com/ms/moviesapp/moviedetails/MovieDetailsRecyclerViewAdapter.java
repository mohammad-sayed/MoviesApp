package com.ms.moviesapp.moviedetails;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.entities.ListType;
import com.ms.moviesapp.entities.Movie;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;
import com.ms.moviesapp.servicelayer.database.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class MovieDetailsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Movie mMovie;
    private List<ListType> mListTypes;
    private OnDetailsActionListener mOnDetailsActionListener;

    public MovieDetailsRecyclerViewAdapter(Context context, Movie movie, List<ListType> listTypes, OnDetailsActionListener onDetailsActionListener) {
        this.mContext = context;
        this.mMovie = movie;
        this.mListTypes = listTypes;
        this.mOnDetailsActionListener = onDetailsActionListener;
    }

    @Override
    public int getItemCount() {
        if (mListTypes == null)
            return 0;
        return mListTypes.size();
    }


    public void setItems(List<ListType> listTypes) {
        this.mListTypes = listTypes;
        notifyDataSetChanged();
        //notifyItemRangeChanged(0, mMoviesList.size());
    }

    public void addItem(ListType listType) {
        this.mListTypes.add(listType);
        notifyDataSetChanged();
        //notifyItemRangeChanged(0, mMoviesList.size());
    }

    public void addItems(List<ListType> listTypes) {
        this.mListTypes.addAll(listTypes);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        final ListType listType = mListTypes.get(position);
        return listType.getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case Constants.LIST_TYPE_HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_details_header, parent, false);
                DetailsHeaderRecyclerViewHolder headerRecyclerViewHolder = new DetailsHeaderRecyclerViewHolder(view);
                return headerRecyclerViewHolder;
            case Constants.LIST_TYPE_TRAILER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);
                TrailerRecyclerViewHolder trailerRecyclerViewHolder = new TrailerRecyclerViewHolder(view);
                return trailerRecyclerViewHolder;
            case Constants.LIST_TYPE_REVIEW:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
                ReviewRecyclerViewHolder reviewRecyclerViewHolder = new ReviewRecyclerViewHolder(view);
                return reviewRecyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ListType listType = mListTypes.get(position);

        switch (listType.getType()) {
            case Constants.LIST_TYPE_HEADER:
                final Movie movie = (Movie) listType.getObject();
                DetailsHeaderRecyclerViewHolder headerRecyclerViewHolder = (DetailsHeaderRecyclerViewHolder) holder;
                String posterPath = mContext.getString(R.string.movie_api_base_url)
                        .concat(mContext.getString(R.string.poster_size_w780))
                        .concat(movie.getPosterPath());
                Picasso.with(mContext).load(posterPath).error(R.drawable.no_poster)
                        .into(headerRecyclerViewHolder.getIvMoviePoster());
                headerRecyclerViewHolder.getTvReleaseDate().setText(movie.getReleaseDate());
                headerRecyclerViewHolder.getTvMovieAvgRate()
                        .setText("" + movie.getUsersRating());
                headerRecyclerViewHolder.getCpbAvgRate().setProgress(movie.getUsersRating() * 10f);
                headerRecyclerViewHolder.getTvMovieSynopsis().setText(movie.getSynopsis());
                final Button btnFavorite = headerRecyclerViewHolder.getBtnFavorite();
                if (MovieContract.checkIfMovieExist(mContext, movie.getId())) {
                    Drawable drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.favorite);
                    btnFavorite.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                }
                btnFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MovieContract.checkIfMovieExist(mContext, mMovie.getId())) {
                            Drawable drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.unfavorite);
                            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                            if (mOnDetailsActionListener != null)
                                mOnDetailsActionListener.onFavoritePressed(true);
                        }
                        //else add to favorites
                        else {
                            Drawable drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.favorite);
                            btnFavorite.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
                            if (mOnDetailsActionListener != null)
                                mOnDetailsActionListener.onFavoritePressed(false);
                        }

                    }
                });
                break;
            case Constants.LIST_TYPE_TRAILER:
                final Trailer trailer = (Trailer) listType.getObject();
                TrailerRecyclerViewHolder trailerRecyclerViewHolder = (TrailerRecyclerViewHolder) holder;
                trailerRecyclerViewHolder.getTvTrailerName().setText(trailer.getName());
                trailerRecyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDetailsActionListener != null)
                            mOnDetailsActionListener.onTrailerPressed(trailer);
                    }
                });

                break;
            case Constants.LIST_TYPE_REVIEW:
                final Review review = (Review) listType.getObject();
                ReviewRecyclerViewHolder reviewRecyclerViewHolder = (ReviewRecyclerViewHolder) holder;
                reviewRecyclerViewHolder.getTvAuthor().setText(review.getAuthor());
                reviewRecyclerViewHolder.getTvContent().setText(review.getContent());
                break;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public ArrayList<Trailer> getTrailers() {
        ArrayList<Trailer> trailers = new ArrayList<>();
        for (ListType listType : mListTypes) {
            if (listType.getType() == Constants.LIST_TYPE_TRAILER)
                trailers.add((Trailer) listType.getObject());
        }
        return trailers;
    }

    public ArrayList<Review> getReviews() {
        ArrayList<Review> reviews = new ArrayList<>();
        for (ListType listType : mListTypes) {
            if (listType.getType() == Constants.LIST_TYPE_REVIEW)
                reviews.add((Review) listType.getObject());
        }
        return reviews;
    }

    public void clearItems() {
        mListTypes.clear();
        notifyDataSetChanged();
    }

    public interface OnDetailsActionListener {
        void onFavoritePressed(boolean favorite);

        void onTrailerPressed(Trailer trailer);
    }

}
