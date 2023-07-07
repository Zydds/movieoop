package com.moviefix;

public class Movie {
    private String name;
    private String genre;
    private String duration;

    public Movie(String name, String genre, String duration) {
        this.name = name;
        this.genre = genre;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }
}
