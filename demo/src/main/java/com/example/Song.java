package com.example;

public class Song {

    private String name;
    private String artist;
    private String fileName;
    private int year;
    private String genre;
    private boolean isFavorite;

    //Getters
    public String name() {
        return this.name;
    }

    public String artist() {
        return this.artist;
    }

    public String fileName() {
        return this.fileName;
    }

    public int year() {
        return this.year;
    }

    public String genre() {
        return this.genre;
    }

    public boolean isFavorite() {
        return this.isFavorite;
    }

    //Favorite
    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
    }
}
