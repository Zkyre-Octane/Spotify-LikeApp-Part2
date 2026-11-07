package com.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class App {

    private static Clip audioClip;
    private static final String JSON_FILE = "demo/src/main/java/com/example/audio-library.json";

    //songs list
    public static void saveSongs(List<Song> songs) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(JSON_FILE);
            gson.toJson(songs, writer);
            writer.close();
            System.out.println("Library saved.");
        } catch (Exception e) {
            System.out.println("Could not save the library.");
        }
    }

    //Play a song and show controls
    public static void play(Song song, List<Song> songs, Scanner scanner) {
        try {
            if (audioClip != null && audioClip.isOpen()) {
                audioClip.stop();
                audioClip.close();
            }

            if (song.fileName() == null || song.fileName().isEmpty()) {
                System.out.println("Audio file path is missing for: " + song.name());
                return;
            }

            File audioFile = new File(song.fileName());
            if (!audioFile.exists()) {
                System.out.println("Audio file not found: " + audioFile.getPath());
                return;
            }

            audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(audioFile));
            audioClip.start();

            System.out.println("\nNow playing: " + song.name());
            System.out.println("Artist: " + song.artist());
            System.out.println("Year: " + song.year());
            System.out.println("Genre: " + song.genre());
            System.out.println("Is Favorite: " + song.isFavorite());
            System.out.println("File: " + song.fileName());

            boolean paused = false;
            boolean stopped = false;

            while ((audioClip.isRunning() || paused) && !stopped) {
                System.out.println("\n[P]ause/Resume  [S]top  [R]ewind 5 seconds  [F]orward 5 seconds  [V]avorite");
                System.out.print("Choose a control: ");
                String control = scanner.nextLine().trim();

                switch (control.toUpperCase()) {
                    case "P":
                        if (paused) {
                            audioClip.start();
                            paused = false;
                        } else {
                            audioClip.stop();
                            paused = true;
                        }
                        break;

                    case "S":
                        audioClip.stop();
                        audioClip.setMicrosecondPosition(0);
                        stopped = true;
                        break;

                    case "R":
                        long rewindPosition = audioClip.getMicrosecondPosition() - 5000000;
                        if (rewindPosition < 0) {
                            rewindPosition = 0;
                        }
                        audioClip.setMicrosecondPosition(rewindPosition);
                        break;

                    case "F":
                        long forwardPosition = audioClip.getMicrosecondPosition() + 5000000;
                        if (forwardPosition > audioClip.getMicrosecondLength()) {
                            forwardPosition = audioClip.getMicrosecondLength();
                        }
                        audioClip.setMicrosecondPosition(forwardPosition);
                        break;

                    case "V":
                        song.setFavorite(!song.isFavorite());
                        System.out.println("Favorite: " + song.isFavorite());
                        saveSongs(songs);
                        break;

                    default:
                        System.out.println("Invalid control.");
                }
            }

            audioClip.close();
            audioClip = null;
            if (stopped) {
                System.out.println("Song stopped.");
            } else {
                System.out.println("Song finished.");
            }

        } catch (Exception e) {
            System.out.println("Could not play: " + song.name());
            e.printStackTrace();
        }
    }

    //Display all songs and allow to select one
    public static void showSongs(List<Song> displayedSongs, List<Song> allSongs, Scanner scanner) {
        if (displayedSongs.isEmpty()) {
            System.out.println("No songs to display.");
            return;
        }

        for (int i = 0; i < displayedSongs.size(); i++) {
            System.out.println((i + 1) + ". " + displayedSongs.get(i).name());
        }

        System.out.print("Enter song number to play (0 to go back): ");
        try {
            int songNumber = Integer.parseInt(scanner.nextLine());
            if (songNumber >= 1 && songNumber <= displayedSongs.size()) {
                play(displayedSongs.get(songNumber - 1), allSongs, scanner);
            } else if (songNumber != 0) {
                System.out.println("Invalid song number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number.");
        }
    }

    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader(JSON_FILE);
            Gson gson = new Gson();
            Type songListType = new TypeToken<List<Song>>() {}.getType();
            List<Song> songs = gson.fromJson(reader, songListType);
            reader.close();

            Scanner scanner = new Scanner(System.in);
            String choice = "";

            do {
                System.out.println("\n=== Spotify-like Player ===");
                System.out.println("[H] Home");
                System.out.println("[S] Search by title");
                System.out.println("[L] Library");
                System.out.println("[F] Favorites");
                System.out.println("[Q] Quit");
                System.out.print("Choose an option: ");
                choice = scanner.nextLine().trim();

                switch (choice.toUpperCase()) {
                    case "H":
                        System.out.println("\n=== Home ===");
                        showSongs(songs, songs, scanner);
                        break;

                    case "S":
                        System.out.print("\nEnter song title to search: ");
                        String searchTitle = scanner.nextLine().trim();
                        boolean found = false;

                        for (Song song : songs) {
                            if (song.name().toLowerCase().contains(searchTitle.toLowerCase())) {
                                System.out.println("\nTitle: " + song.name());
                                System.out.println("Artist: " + song.artist());
                                System.out.println("Year: " + song.year());
                                System.out.println("Genre: " + song.genre());
                                System.out.println("Is Favorite: " + song.isFavorite());
                                System.out.print("Play this song? (Y/N): ");
                                String playChoice = scanner.nextLine().trim();
                                if (playChoice.equalsIgnoreCase("Y")) {
                                    play(song, songs, scanner);
                                }
                                found = true;
                            }
                        }

                        if (!found) {
                            System.out.println("No songs found.");
                        }
                        break;

                    case "L":
                        System.out.println("\n=== Your Library ===");
                        showSongs(songs, songs, scanner);
                        break;

                    case "F":
                        System.out.println("\n=== Your Favorites ===");
                        List<Song> favorites = new java.util.ArrayList<Song>();
                        for (Song song : songs) {
                            if (song.isFavorite()) {
                                favorites.add(song);
                            }
                        }
                        showSongs(favorites, songs, scanner);
                        break;

                    case "Q":
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option. Please choose H, S, L, F, or Q.");
                }

            } while (!choice.equalsIgnoreCase("Q"));

            scanner.close();

        } catch (Exception e) {
            System.out.println("Error loading songs.");
            e.printStackTrace();
        }
    }
}
