package com.ms.moviesapp.moviesapp.entities;

import java.io.Serializable;

/**
 * Created by Mohammad-Sayed-PC on 12/18/2015.
 */
public class Movie implements Serializable {

    private String posterPath;//"poster_path"
    private String synopsis; //"overview"
    private String title;//"title"
    private String thumbnail; //"backdrop_path"
    private float usersRating; //"vote_average"
    private String releaseDate;//"release_date"

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public float getUsersRating() {
        return usersRating;
    }

    public void setUsersRating(float usersRating) {
        this.usersRating = usersRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
