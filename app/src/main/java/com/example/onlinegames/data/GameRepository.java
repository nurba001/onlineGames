package com.example.onlinegames.data;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.onlinegames.api.ApiGame;
import com.example.onlinegames.api.GameApiService;
import com.example.onlinegames.api.GameResponse;
import com.example.onlinegames.api.RetrofitClient;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {

    private final GameDao mGameDao;
    private final ExecutorService mExecutorService;

    public GameRepository(Application application) {
        GameDatabase db = GameDatabase.getDatabase(application);
        mGameDao = db.gameDao();
        mExecutorService = GameDatabase.databaseWriteExecutor;
    }

    // --- Стандартные методы DAO ---

    public LiveData<List<GameEntity>> getAllGames() {
        return mGameDao.getAllGames();
    }

    public LiveData<List<GameEntity>> searchGames(String query) {
        return mGameDao.searchGames("%" + query + "%");
    }

    public LiveData<List<GameEntity>> filterGames(String platform, String genre) {
        return mGameDao.filterGames(platform, genre);
    }

    public LiveData<GameEntity> getGameById(int id) {
        return mGameDao.getGameById(id);
    }

    public LiveData<List<GameEntity>> getFavoriteGames() {
        return mGameDao.getFavoriteGames();
    }

    public void update(GameEntity game) {
        mExecutorService.execute(() -> mGameDao.update(game));
    }

    public int getGameCount() {
        return mGameDao.getGameCount();
    }

    // --- ЗАГРУЗКА ИЗ ИНТЕРНЕТА ---

    public void fetchGamesFromApi(String apiKey) {
        GameApiService apiService = RetrofitClient.getApiService();

        // Запрашиваем 40 игр, сортируем по рейтингу
        Call<GameResponse> call = apiService.getGames(apiKey, 40, null, "-rating");

        call.enqueue(new Callback<GameResponse>() {
            @Override
            public void onResponse(@NonNull Call<GameResponse> call, @NonNull Response<GameResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApiGame> apiGames = response.body().getResults();
                    // Сохраняем полученные игры в базу данных
                    saveGamesToDatabase(apiGames);
                } else {
                    Log.e("GameRepository", "Ошибка сервера: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<GameResponse> call, @NonNull Throwable t) {
                Log.e("GameRepository", "Ошибка сети: " + t.getMessage());
            }
        });
    }

    private void saveGamesToDatabase(List<ApiGame> apiGames) {
        mExecutorService.execute(() -> {
            for (ApiGame apiGame : apiGames) {
                // 1. Преобразуем сложные списки (жанры, платформы) в простые строки
                String genres = formatGenres(apiGame.getGenres());
                String platforms = formatPlatforms(apiGame.getParentPlatforms());
                int year = parseYear(apiGame.getReleased());

                // 2. Создаем Entity для нашей базы
                GameEntity gameEntity = new GameEntity(
                        apiGame.getName(),
                        genres,
                        platforms,
                        year,
                        "Рейтинг RAWG: " + apiGame.getRating(), // Временное описание
                        apiGame.getBackgroundImage()
                );

                // 3. Сохраняем в базу
                mGameDao.insert(gameEntity);
            }
        });
    }

    // --- Вспомогательные методы для преобразования данных ---

    private String formatGenres(List<ApiGame.Genre> genres) {
        if (genres == null || genres.isEmpty()) return "Unknown";
        StringBuilder sb = new StringBuilder();
        for (ApiGame.Genre g : genres) {
            sb.append(g.name).append(", ");
        }
        // Удаляем последнюю запятую
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        return sb.toString();
    }

    private String formatPlatforms(List<ApiGame.PlatformWrapper> platforms) {
        if (platforms == null || platforms.isEmpty()) return "PC";
        // Для простоты берем только первую платформу или пишем Multi
        if (platforms.size() > 1) return "PC/Console";
        return platforms.get(0).platform.name;
    }

    private int parseYear(String releasedDate) {
        // Дата приходит в формате "2013-09-17". Берем первые 4 символа.
        if (releasedDate != null && releasedDate.length() >= 4) {
            try {
                return Integer.parseInt(releasedDate.substring(0, 4));
            } catch (NumberFormatException e) {
                return 2023;
            }
        }
        return 2023;
    }
}