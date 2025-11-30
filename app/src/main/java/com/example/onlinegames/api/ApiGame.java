package com.example.onlinegames.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

// Этот класс повторяет структуру JSON, который приходит от RAWG
public class ApiGame {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("background_image")
    private String backgroundImage; // Ссылка на картинку

    @SerializedName("released")
    private String released; // Дата выхода (например, "2013-09-17")

    @SerializedName("rating")
    private float rating;

    // Вложенный класс для платформ (RAWG присылает их хитро)
    @SerializedName("parent_platforms")
    private List<PlatformWrapper> parentPlatforms;

    @SerializedName("genres")
    private List<Genre> genres;

    // --- Геттеры ---
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBackgroundImage() { return backgroundImage; }
    public String getReleased() { return released; }
    public float getRating() { return rating; }
    public List<PlatformWrapper> getParentPlatforms() { return parentPlatforms; }
    public List<Genre> getGenres() { return genres; }

    // --- Вспомогательные классы ---

    public static class PlatformWrapper {
        @SerializedName("platform")
        public Platform platform;
    }

    public static class Platform {
        @SerializedName("name")
        public String name;
    }

    public static class Genre {
        @SerializedName("name")
        public String name;
    }
}
