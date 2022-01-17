package com.test.omdb.data.models;

import androidx.annotation.NonNull;

public class Search {
    private String Title;
    private String Year;
    private String imdbID;
    private String Type;
    private String Poster;

    public Search(){}

    public Search(@NonNull String Title, @NonNull String Year, @NonNull String imdbID,
                  @NonNull String Type, @NonNull String Poster){
        this.Title = Title;
        this.Year = Year;
        this.imdbID = imdbID;
        this.Type = Type;
        this.Poster = Poster;
    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getPoster() {
        return Poster;
    }
}
