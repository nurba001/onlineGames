package com.example.onlinegames.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// @Dao указывает, что это Data Access Object
@Dao
public interface GameDao {

    // --- Вставка и Обновление ---

    // (onConflict = OnConflictStrategy.REPLACE) означает:
    // если мы вставляем игру с id, который уже есть, просто заменяем старую.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GameEntity game);

    // Обновляем существующую игру (например, когда добавляем отзыв)
    @Update
    void update(GameEntity game);


    // --- Запросы (Query) ---

    // Получить ВСЕ игры из каталога
    // Мы используем LiveData, чтобы UI (RecyclerView) автоматически обновлялся
    // при изменении данных в таблице.
    @Query("SELECT * FROM game_catalog ORDER BY title ASC")
    LiveData<List<GameEntity>> getAllGames();

    // Получить только ИЗБРАННЫЕ игры
    @Query("SELECT * FROM game_catalog WHERE isFavorite = 1 ORDER BY title ASC")
    LiveData<List<GameEntity>> getFavoriteGames();

    // Получить одну игру по ее ID (для экрана "Детали")
    @Query("SELECT * FROM game_catalog WHERE id = :gameId")
    LiveData<GameEntity> getGameById(int gameId);

    // --- Поиск и Фильтры (из вашего задания) ---

    // Поиск по названию
    // (Мы ищем совпадения в 'title'. :query — это параметр, который мы передаем)
    // "SELECT * FROM game_catalog WHERE title LIKE '%' || :query || '%'"
    @Query("SELECT * FROM game_catalog WHERE title LIKE :query")
    LiveData<List<GameEntity>> searchGames(String query);

    // Фильтр по платформе ИЛИ жанру
    // (Этот запрос немного сложнее, он позволяет фильтровать)
    @Query("SELECT * FROM game_catalog WHERE (:platform = 'Все' OR platform = :platform) AND (:genre = 'Все' OR genre = :genre)")
    LiveData<List<GameEntity>> filterGames(String platform, String genre);

    // --- Вспомогательный метод ---
    // Нужен, чтобы проверить, пустая ли база данных
    @Query("SELECT COUNT(*) FROM game_catalog")
    int getGameCount();
}
