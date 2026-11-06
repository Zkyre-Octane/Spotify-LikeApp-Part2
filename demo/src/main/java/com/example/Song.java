package com.example;

public class Song {

    private String name;
    private String artist;
    private String fileName;
    private int year;
    private String genre;

    // Serializes attributes into a string
    public String toString() {
        String s;

        s = "{ ";
        s += "name: " + name;
        s += ", ";
        s += "artist: " + artist;
        s += ", ";
        s += "fileName: " + fileName;
        s += ", ";
        s += "year: " + year;
        s += ", ";
        s += "genre: " + genre;
        s += " }";

        return s;
    }

    // Getters
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

}