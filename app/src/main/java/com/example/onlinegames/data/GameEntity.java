package com.example.onlinegames.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// @Entity указывает, что этот класс является таблицей в базе данных Room.
// tableName = "game_catalog" — это имя нашей таблицы.
@Entity(tableName = "game_catalog")
public class GameEntity {

    // @PrimaryKey(autoGenerate = true) делает 'id' уникальным
    // и автоматически присваивает ему новое значение (1, 2, 3...)
    @PrimaryKey(autoGenerate = true)
    private int id;

    // --- Поля из вашего задания ---
    private String title;       // Название
    private String genre;       // Жанр
    private String platform;    // Платформа
    private int year;           // Год

    // --- Дополнительные поля для деталей и избранного ---

    // Описание игры (для экрана "Детали")
    private String description;

    // Ссылка на обложку/скриншот
    private String imageUrl;

    // Поля для "Избранного"
    private String comment;     // Комментарий пользователя
    private float userRating;   // Оценка пользователя (например, от 0 до 5)

    // Поле, чтобы помечать игры как "избранные"
    private boolean isFavorite;

    // --- Конструктор ---
    // (Room будет использовать его для создания объектов)
    // Мы можем создать конструктор для обязательных полей
    public GameEntity(String title, String genre, String platform, int year, String description, String imageUrl) {
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.year = year;
        this.description = description;
        this.imageUrl = imageUrl;

        // По умолчанию игра не в избранном, без комментариев
        this.isFavorite = false;
        this.comment = "";
        this.userRating = 0.0f;
    }

    // --- Геттеры и Сеттеры (Getters and Setters) ---
    // Room использует их для чтения и записи данных в поля.
    // Они ОБЯЗАТЕЛЬНЫ для всех полей.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
