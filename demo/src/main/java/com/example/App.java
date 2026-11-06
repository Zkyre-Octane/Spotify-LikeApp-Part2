package com.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class App {
    public static void play(Song song) {

        try {
            File audioFile = new File(song.fileName()); // locate audio file

            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(audioFile));

            System.out.println("\nNow playing: " + song.name());
            System.out.println("Artist: " + song.artist());
            System.out.println("Year: " + song.year());
            System.out.println("Genre: " + song.genre());
            System.out.println("Filepath: " + song.fileName());

            clip.start(); // begins playback

        } catch (Exception e) {
            System.out.println("Could not play: " + song.name());
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        int songNumber; // user input for song number used by home and library

        try {
            FileReader reader = new FileReader("demo\\src\\main\\java\\com\\example\\audio-library.json");

            Gson gson = new Gson();

            Type songListType = new TypeToken<List<Song>>() {
            }.getType();

            List<Song> songs = gson.fromJson(reader, songListType);

            reader.close();

            Scanner scanner = new Scanner(System.in);
            String choice;

            // Main menu
            do {
                System.out.println("\n=== Spotify-like Player ===");
                System.out.println("[H] Home");
                System.out.println("[S] Search");
                System.out.println("[L] Library");
                System.out.println("[Q] Quit");
                System.out.print("Choose an option: ");

                choice = scanner.nextLine().trim();

                switch (choice.toUpperCase()) {

                    case "H": // HOME
                        System.out.println("\n=== Home ===");

                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println((i + 1) + ". " + songs.get(i).name()); // songs listed by number
                        }
                        System.out.print("Enter song number to play: ");

                        songNumber = Integer.parseInt(scanner.nextLine());

                        if (songNumber >= 1 && songNumber <= songs.size()) {
                            play(songs.get(songNumber - 1));
                        } else {
                            System.out.println("Invalid song number.");
                        }
                        break;

                    case "S": // SEARCH
                        System.out.print("\nEnter song title to search: ");
                        String searchTitle = scanner.nextLine().trim();

                        boolean found = false;

                        for (Song song : songs) {
                            if (song.name().toLowerCase().contains(searchTitle.toLowerCase())) { // using contains so
                                                                                                 // the user can search
                                                                                                 // a part of a title
                                System.out.println("\nTitle: " + song.name());
                                System.out.println("Artist: " + song.artist());
                                System.out.println("Year: " + song.year());
                                System.out.println("Genre: " + song.genre());

                                System.out.print("\nPlay this song? (Y/N): "); // ask if user wants to play the song
                                String playChoice = scanner.nextLine().trim();

                                if (playChoice.equalsIgnoreCase("Y")) {
                                    play(song); // play the song if the user chooses yes
                                }

                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No songs found."); // if no songs found, print this message
                        }

                        break;

                    case "L": // LIBRARY
                        System.out.println("\n=== Your Library ===");

                        for (int i = 0; i < songs.size(); i++) {
                            System.out.println((i + 1) + "." + songs.get(i).name());
                        }

                        System.out.print("Enter song number to play: ");

                        songNumber = Integer.parseInt(scanner.nextLine());

                        if (songNumber >= 1 && songNumber <= songs.size()) {
                            play(songs.get(songNumber - 1));
                        } else {
                            System.out.println("Invalid song number.");
                        }
                        break;

                    case "Q": // QUIT
                        System.out.println("Goodbye!");
                        break;

                    default: // INVALID INPUT
                        System.out.println(
                                "Invalid option. Please choose H, S, L, or Q.");

                }

            } while (!choice.equalsIgnoreCase("Q"));

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error loading songs.");
            e.printStackTrace();
        }
    }
}
