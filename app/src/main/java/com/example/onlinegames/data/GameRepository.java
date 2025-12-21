package com.example.onlinegames.data;

import android.app.Application;
import android.util.Log;
import androidx.lifecycle.LiveData;
import java.util.List;

public class GameRepository {
    private final GameDao gameDao;
    private final LiveData<List<GameEntity>> allGames;

    public GameRepository(Application application) {
        GameDatabase db = GameDatabase.getDatabase(application);
        gameDao = db.gameDao();
        allGames = gameDao.getAllGames();
    }

    public LiveData<List<GameEntity>> getAllGames() {
        return allGames;
    }

    // ВАЖНО: Возвращаем метод для проверки количества игр
    public int getGameCount() {
        try {
            return gameDao.getGameCount();
        } catch (Exception e) {
            Log.e("CRUD_ERROR", "Ошибка при подсчете игр: " + e.getMessage());
            return 0;
        }
    }

    // CREATE: Метод для ручного добавления игры с try-catch
    public void insertGame(GameEntity game) {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            try {
                gameDao.insert(game);
                Log.d("CRUD", "Игра успешно добавлена: " + game.getName());
            } catch (Exception e) {
                Log.e("CRUD_ERROR", "Ошибка при вставке игры: " + e.getMessage());
            }
        });
    }

    // DELETE: Метод для удаления игры с try-catch
    public void deleteGame(GameEntity game) {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            try {
                gameDao.delete(game);
                Log.d("CRUD", "Игра успешно удалена: " + game.getName());
            } catch (Exception e) {
                Log.e("CRUD_ERROR", "Ошибка при удалении игры: " + e.getMessage());
            }
        });
    }

    // UPDATE: Обновление (для рейтинга и избранного)
    public void updateGame(GameEntity game) {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            try {
                gameDao.update(game);
            } catch (Exception e) {
                Log.e("CRUD_ERROR", "Ошибка при обновлении игры: " + e.getMessage());
            }
        });
    }

    // Остальные методы для поиска и фильтрации
    public LiveData<List<GameEntity>> searchGames(String query) { return gameDao.searchGames(query); }
    public LiveData<List<GameEntity>> filterGames(String platform, String genre) { return gameDao.filterGames(platform, genre); }
    public LiveData<List<GameEntity>> getFavoriteGames() { return gameDao.getFavoriteGames(); }
    public LiveData<GameEntity> getGameById(int id) { return gameDao.getGameById(id); }
}