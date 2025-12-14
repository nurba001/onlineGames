package com.example.onlinegames.api;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Модель данных для одной игры, полученной от FreeToGame Public API.
 * Использует аннотации @SerializedName для соответствия JSON-полям FreeToGame.
 */
public class ApiGame {

    @SerializedName("id")
    private int id;

    @SerializedName("title") // FreeToGame использует "title"
    private String title;

    @SerializedName("platform") // "PC (Windows), Web Browser"
    private String platform;

    @SerializedName("genre") // FreeToGame использует "genre" как строку
    private String genre;

    @SerializedName("release_date") // Дата релиза
    private String releaseDate;

    @SerializedName("short_description") // Короткое описание
    private String description;

    @SerializedName("thumbnail") // URL для обложки/иконки
    private String imageUrl;

    // Геттеры
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getPlatform() { return platform; }
    // Внимание: теперь это getGenre() -> String
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
}