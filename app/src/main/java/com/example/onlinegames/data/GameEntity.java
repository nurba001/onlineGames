package com.example.onlinegames.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_table")
public class GameEntity {

    @PrimaryKey // The ID from the API will be our primary key
    private int id;

    private String name;
    private String imageUrl;
    private String platform;
    private String genre;
    private String year;
    private String description;


    private boolean isFavorite = false;
    private float userRating = 0.0f;
    private String comment = "";

    public GameEntity() {
    }

    @Ignore
    public GameEntity(int id, String name, String imageUrl, String platform, String genre, String year, String description, boolean isFavorite, float userRating, String comment) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.platform = platform;
        this.genre = genre;
        this.year = year;
        this.description = description;
        this.isFavorite = isFavorite;
        this.userRating = userRating;
        this.comment = comment;
    }

    // --- GETTERS & SETTERS

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    public float getUserRating() {
        return userRating;
    }
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
}
