package com.example.onlinegames.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "game_table")
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;        // Название
    private String genre;       // Жанр
    private String platform;    // Платформа
    private int year;           // Год
    private String description; // Описание игры

    // Ссылка на обложку/скриншот
    private String imageUrl;

    // !!! НОВЫЕ ПОЛЯ ДЛЯ ПОЛЬЗОВАТЕЛЬСКОГО ВВОДА !!!
    private String comment = "";        // Комментарий пользователя (по умолчанию пустая строка)
    private float userRating = 0.0f;    // Рейтинг пользователя (по умолчанию 0)

    // Поле для избранного. По умолчанию - false (не избранное)
    private boolean isFavorite = false;

    // --- КОНСТРУКТОР ---
    // Конструктор должен включать все поля, которые мы получаем из API
    public GameEntity(String name, String genre, String platform, int year, String description, String imageUrl) {
        this.name = name;
        this.genre = genre;
        this.platform = platform;
        this.year = year;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // --- GETTERS & SETTERS (ОБЯЗАТЕЛЬНО ДЛЯ ROOM) ---

    // Геттеры и сеттеры для ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    // Геттеры и сеттеры для основных полей
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // !!! НОВЫЕ: Геттеры и сеттеры для Комментария и Рейтинга
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public float getUserRating() {
        return userRating;
    }
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    // Геттер и сеттер для isFavorite
    public boolean isFavorite() {
        return isFavorite;
    }
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}