package com.ms.moviesapp.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ms.moviesapp.R;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class ReviewRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView tvAuthor;
    private TextView tvContent;

    public ReviewRecyclerViewHolder(View itemView) {
        super(itemView);
        tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public TextView getTvAuthor() {
        return tvAuthor;
    }

    public void setTvAuthor(TextView tvAuthor) {
        this.tvAuthor = tvAuthor;
    }

    public TextView getTvContent() {
        return tvContent;
    }

    public void setTvContent(TextView tvContent) {
        this.tvContent = tvContent;
    }
}
