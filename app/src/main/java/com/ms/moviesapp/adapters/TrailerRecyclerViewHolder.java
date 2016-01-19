package com.ms.moviesapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ms.moviesapp.R;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class TrailerRecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView tvTrailerName;

    public TrailerRecyclerViewHolder(View itemView) {
        super(itemView);
        tvTrailerName = (TextView) itemView.findViewById(R.id.tv_trailer_name);
    }

    public TextView getTvTrailerName() {
        return tvTrailerName;
    }

    public void setTvTrailerName(TextView tvTrailerName) {
        this.tvTrailerName = tvTrailerName;
    }
}
