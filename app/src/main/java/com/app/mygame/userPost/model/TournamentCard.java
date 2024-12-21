package com.app.mygame.userPost.model;

import java.io.Serializable;

public class TournamentCard implements Serializable {
    private String title;
    private String description;
    private String imageUrl;
    private String date;
    private String prize;

    public TournamentCard(String title, String description, String imageUrl, String date, String prize) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.prize = prize;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getDate() { return date; }
    public String getPrize() { return prize; }
}

