package com.example.onlinegames.data;

// !!! ЭТИ ДВА ИМПОРТА ДОЛЖНЫ БЫТЬ ОБЯЗАТЕЛЬНО !!!
import androidx.lifecycle.LiveData;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface GameDao {

    // Вставка новой игры (или замена существующей, если конфликтует)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity game);

    // Обновление существующей игры (используется для Избранного)
    @Update
    void update(GameEntity game);

    // Получение всех игр в LiveData
    @Query("SELECT * FROM game_table ORDER BY year DESC")
    LiveData<List<GameEntity>> getAllGames();

    // Получение всех игр в LiveData, которые отмечены как избранные (isFavorite = 1)
    @Query("SELECT * FROM game_table WHERE isFavorite = 1 ORDER BY name ASC")
    LiveData<List<GameEntity>> getFavoriteGames();

    // Поиск по названию, жанру и платформе
    @Query("SELECT * FROM game_table WHERE name LIKE :query OR genre LIKE :query OR platform LIKE :query ORDER BY name ASC")
    LiveData<List<GameEntity>> searchGames(String query);

    // Фильтрация по платформе и жанру
    @Query("SELECT * FROM game_table WHERE platform LIKE :platform AND genre LIKE :genre ORDER BY name ASC")
    LiveData<List<GameEntity>> filterGames(String platform, String genre);

    // Получение игры по ID
    @Query("SELECT * FROM game_table WHERE id = :id")
    LiveData<GameEntity> getGameById(int id);

    // Получение количества игр (для проверки, пуста ли база)
    @Query("SELECT COUNT(id) FROM game_table")
    int getGameCount();

    // Метод для удаления всех игр перед загрузкой свежих данных
    @Query("DELETE FROM game_table")
    void deleteAll();
}