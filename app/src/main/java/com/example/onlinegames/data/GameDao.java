package com.example.onlinegames.data;

import androidx.lifecycle.LiveData;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface GameDao {

    // Переименовали метод для ясности
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GameEntity> games);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity game);

    @Update
    void update(GameEntity game);

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

    @Query("SELECT COUNT(id) FROM game_table")
    int getGameCount();

    @Query("DELETE FROM game_table")
    void deleteAll();
}
