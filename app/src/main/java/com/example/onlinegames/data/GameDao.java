package com.example.onlinegames.data;

import androidx.lifecycle.LiveData;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface GameDao {

    // --- CREATE (Создание) ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity game);

    // --- UPDATE (Обновление) ---
    @Update
    void update(GameEntity game);

    // --- READ (Чтение) ---
    @Query("SELECT * FROM game_table ORDER BY year DESC")
    LiveData<List<GameEntity>> getAllGames();

    @Query("SELECT * FROM game_table WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<GameEntity>> getFavoriteGames();

    @Query("SELECT * FROM game_table WHERE name LIKE :query OR genre LIKE :query OR platform LIKE :query ORDER BY name ASC")
    LiveData<List<GameEntity>> searchGames(String query);

    @Query("SELECT * FROM game_table WHERE platform LIKE :platform AND genre LIKE :genre ORDER BY name ASC")
    LiveData<List<GameEntity>> filterGames(String platform, String genre);

    @Query("SELECT * FROM game_table WHERE id = :id")
    LiveData<GameEntity> getGameById(int id);

    // Метод для проверки количества игр (нужен для логики "Один раз")
    @Query("SELECT COUNT(id) FROM game_table")
    int getGameCount();

    // --- DELETE (Удаление) ---
    @Query("DELETE FROM game_table")
    void deleteAll();

    // 1. Удаление конкретной игры
    @Delete
    void delete(GameEntity game);
}
