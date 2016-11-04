package com.ms.moviesapp.moviedetails;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ms.moviesapp.Constants;
import com.ms.moviesapp.R;
import com.ms.moviesapp.entities.ListType;
import com.ms.moviesapp.entities.Review;
import com.ms.moviesapp.entities.Trailer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class ListTypesItemsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ListType> mListTypes;
    private Uri mYoutubeUri;
    private final String PARAMETER_VIDEO = "v";

    public ListTypesItemsAdapter(Context context, List<ListType> moviesList) {
        this.mContext = context;
        this.mListTypes = moviesList;
        String youtubeUrl = context.getString(R.string.youtube_url);
        this.mYoutubeUri = Uri.parse(youtubeUrl);
    }

    @Override
    public int getCount() {
        if (mListTypes == null)
            return 0;
        return mListTypes.size();
    }

    @Override
    public ListType getItem(int position) {
        if (mListTypes == null)
            return null;
        return mListTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setItems(List<ListType> listTypes) {
        this.mListTypes = listTypes;
        notifyDataSetChanged();
    }

    public void addItems(List<ListType> listTypes) {
        this.mListTypes.addAll(listTypes);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ListType listType = mListTypes.get(position);

        switch (listType.getType()) {
            case Constants.LIST_TYPE_TRAILER:
                TrailerItemHolder trailerItemHolder;
                //if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_layout, parent, false);
                trailerItemHolder = new TrailerItemHolder();
                trailerItemHolder.setTvTrailerName((TextView) convertView.findViewById(R.id.tv_trailer_name));
                    /*convertView.setTag(trailerItemHolder);
                } else {
                    trailerItemHolder = (TrailerItemHolder) convertView.getTag();
                }*/
                final Trailer trailer = (Trailer) listType.getObject();
                trailerItemHolder.getTvTrailerName().setText(trailer.getName());
                /*convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(mYoutubeUri.buildUpon().appendQueryParameter(PARAMETER_VIDEO, trailer.getKey()).build());
                        mContext.startActivity(intent);
                    }
                });*/
                break;
            case Constants.LIST_TYPE_REVIEW:
                ReviewItemHolder reviewItemHolder;
                //if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
                reviewItemHolder = new ReviewItemHolder();
                reviewItemHolder.setTvAuthor((TextView) convertView.findViewById(R.id.tv_author));
                reviewItemHolder.setTvContent((TextView) convertView.findViewById(R.id.tv_content));
                /*    convertView.setTag(reviewItemHolder);
                } else {
                    reviewItemHolder = (ReviewItemHolder) convertView.getTag();
                }*/
                final Review review = (Review) listType.getObject();
                reviewItemHolder.getTvAuthor().setText(review.getAuthor());
                reviewItemHolder.getTvContent().setText(review.getContent());
                break;
        }
        return convertView;
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
}
