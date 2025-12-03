package com.example.onlinegames.api;
import com.google.gson.annotations.SerializedName;
import java.util.List;

// Модель данных для одной игры Giant Bomb
public class GbGame {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("image")
    private ImageInfo image; // В Giant Bomb картинка вложена в объект

    @SerializedName("original_release_date")
    private String originalReleaseDate; // Дата выхода ("2013-09-17 00:00:00")

    @SerializedName("platforms")
    private List<NameObject> platforms; // Список платформ

    @SerializedName("genres")
    private List<NameObject> genres; // Список жанров

    @SerializedName("description")
    private String description;

    // --- Геттеры ---
    public int getId() { return id; }
    public String getName() { return name; }
    public ImageInfo getImage() { return image; }
    public String getOriginalReleaseDate() { return originalReleaseDate; }
    public List<NameObject> getPlatforms() { return platforms; }
    public List<NameObject> getGenres() { return genres; }
    public String getDescription() { return description; }

    // --- Вспомогательные классы ---

    public static class ImageInfo {
        @SerializedName("original_url")
        public String originalUrl; // Ссылка на картинку
    }

    public static class NameObject {
        @SerializedName("name")
        public String name;
    }
}
