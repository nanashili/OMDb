package com.test.omdb.data.models;

import androidx.annotation.NonNull;

import java.util.List;

public class Movie {

    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Runtime;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private List<Ratings> Ratings;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String Dvd;
    private String BoxOffice;
    private String Production;
    private String Website;

    //Series
    private int totalSeasons;

    public Movie(){}

    public Movie(@NonNull String title, @NonNull String year, @NonNull String rated, @NonNull String released, @NonNull String genre,
                 @NonNull String director, @NonNull String writer, @NonNull String actors, @NonNull String plot, @NonNull String Runtime,
                 @NonNull String language, @NonNull String country, @NonNull String awards, @NonNull String poster,
                 @NonNull List<Ratings> ratings, String metascore, String imdbRating, @NonNull String imdbVotes, @NonNull String imdbID,
                 @NonNull String type, @NonNull String dvd, @NonNull String boxOffice, @NonNull String production, @NonNull String website,
                 int totalSeasons){
        this.Title = title;
        this.Year = year;
        this.Rated = rated;
        this.Released = released;
        this.Genre = genre;
        this.Director = director;
        this.Writer = writer;
        this.Actors = actors;
        this.Plot = plot;
        this.Runtime = Runtime;
        this.Language = language;
        this.Country = country;
        this.Awards = awards;
        this.Poster = poster;
        this.Ratings = ratings;
        this.Metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.Type = type;
        this.Dvd = dvd;
        this.BoxOffice = boxOffice;
        this.Production = production;
        this.Website = website;
        this.totalSeasons = totalSeasons;

    }

    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getGenre() {
        return Genre;
    }

    public String getDirector() {
        return Director;
    }

    public String getWriter() {
        return Writer;
    }

    public String getActors() {
        return Actors;
    }

    public String getPlot() {
        return Plot;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String getLanguage() {
        return Language;
    }

    public String getCountry() {
        return Country;
    }

    public String getAwards() {
        return Awards;
    }

    public String getPoster() {
        return Poster;
    }

    public List<Ratings> getRatings() {
        return Ratings;
    }

    public String getMetascore() {
        return Metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getDvd() {
        return Dvd;
    }

    public String getBoxOffice() {
        return BoxOffice;
    }

    public String getProduction() {
        return Production;
    }

    public String getWebsite() {
        return Website;
    }

    public int getTotalSeasons() {
        return totalSeasons;
    }
}
