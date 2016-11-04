package com.ms.moviesapp.moviedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ms.moviesapp.R;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class DetailsHeaderRecyclerViewHolder extends RecyclerView.ViewHolder {

    private ImageView ivMoviePoster;
    private TextView tvReleaseDate;
    private CircularProgressBar cpbAvgRate;
    private TextView tvMovieAvgRate;
    private Button btnFavorite;
    private TextView tvMovieSynopsis;

    public DetailsHeaderRecyclerViewHolder(View itemView) {
        super(itemView);
        ivMoviePoster = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
        tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_release_date);
        cpbAvgRate = (CircularProgressBar) itemView.findViewById(R.id.cpb_voting_average);
        tvMovieAvgRate = (TextView) itemView.findViewById(R.id.tv_voting_average);
        btnFavorite = (Button) itemView.findViewById(R.id.btn_favorite);
        tvMovieSynopsis = (TextView) itemView.findViewById(R.id.tv_movie_synopsis);
    }

    public ImageView getIvMoviePoster() {
        return ivMoviePoster;
    }

    public void setIvMoviePoster(ImageView ivMoviePoster) {
        this.ivMoviePoster = ivMoviePoster;
    }

    public TextView getTvReleaseDate() {
        return tvReleaseDate;
    }

    public void setTvReleaseDate(TextView tvReleaseDate) {
        this.tvReleaseDate = tvReleaseDate;
    }

    public CircularProgressBar getCpbAvgRate() {
        return cpbAvgRate;
    }

    public void setCpbAvgRate(CircularProgressBar cpbAvgRate) {
        this.cpbAvgRate = cpbAvgRate;
    }

    public TextView getTvMovieAvgRate() {
        return tvMovieAvgRate;
    }

    public void setTvMovieAvgRate(TextView tvMovieAvgRate) {
        this.tvMovieAvgRate = tvMovieAvgRate;
    }

    public Button getBtnFavorite() {
        return btnFavorite;
    }

    public void setBtnFavorite(Button btnFavorite) {
        this.btnFavorite = btnFavorite;
    }

    public TextView getTvMovieSynopsis() {
        return tvMovieSynopsis;
    }

    public void setTvMovieSynopsis(TextView tvMovieSynopsis) {
        this.tvMovieSynopsis = tvMovieSynopsis;
    }
}
