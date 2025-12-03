package com.example.onlinegames.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.onlinegames.api.GameApiService;
import com.example.onlinegames.api.RetrofitClient;
import com.example.onlinegames.api.GbGame;
import com.example.onlinegames.api.GbResponse;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {

    private final GameDao mGameDao;
    private final LiveData<List<GameEntity>> mAllGames;
    private  ExecutorService mExecutorService;

    public GameRepository(Application application) {
        GameDatabase db = GameDatabase.getDatabase(application);
        mGameDao = db.gameDao();
        mAllGames = mGameDao.getAllGames();
        mExecutorService = GameDatabase.databaseWriteExecutor;
    }

    public LiveData<List<GameEntity>> getAllGames() {
        return mAllGames;
    }
    public LiveData<GameEntity> getGameById(int gameId) {
        return mGameDao.getGameById(gameId);
    }

    public LiveData<List<GameEntity>> searchGames(String query) {
        return mGameDao.searchGames("%" + query + "%");
    }

    public LiveData<List<GameEntity>> filterGames(String platform, String genre) {
        String platformQuery = (platform.equals("Все платформы") || platform.equals("Все")) ? "%%" : "%" + platform + "%";
        String genreQuery = (genre.equals("Все жанры") || genre.equals("Все")) ? "%%" : "%" + genre + "%";
        return mGameDao.filterGames(platformQuery, genreQuery);
    }

    public void insert(GameEntity game) {
        mExecutorService.execute(() -> mGameDao.insert(game));
    }

    public void update(GameEntity game) {
        mExecutorService.execute(() -> mGameDao.update(game));
    }

    public LiveData<List<GameEntity>> getFavoriteGames() {
        return mGameDao.getFavoriteGames();
    }

    public int getGameCount() {
        return mGameDao.getGameCount();
    }

    // --- ЛОГИКА API ---

    public void fetchGamesFromApi(String apiKey) {
        GameApiService apiService = RetrofitClient.getApiService();
        Call<GbResponse> call = apiService.getGames(
                apiKey,
                "json",
                50,
                "original_release_date:desc"
        );

        call.enqueue(new Callback<GbResponse>() {
            @Override
            public void onResponse(Call<GbResponse> call, Response<GbResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GbGame> games = response.body().getResults();
                    if (games != null && !games.isEmpty()) {
                        Log.d("API", "Успешно получено " + games.size() + " игр.");
                        saveGamesToDatabase(games);
                    } else {
                        Log.d("API", "Успешный ответ, но список игр пуст.");
                    }
                } else {
                    Log.e("API", "Ошибка ответа API: Код " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GbResponse> call, Throwable t) {
                Log.e("API", "Ошибка соединения с API: " + t.getMessage());
            }
        });
    }

    private void saveGamesToDatabase(List<GbGame> gbGames) {
        mExecutorService.execute(() -> {
            mGameDao.deleteAll();
            Log.d("Database", "База данных очищена.");

            for (GbGame gbGame : gbGames) {
                // Используем исправленный метод formatNameObjects
                String genres = formatNameObjects(gbGame.getGenres());
                String platforms = formatNameObjects(gbGame.getPlatforms());

                int year = 0;
                String releaseDate = gbGame.getOriginalReleaseDate();
                if (releaseDate != null && releaseDate.length() >= 4) {
                    try {
                        year = Integer.parseInt(releaseDate.substring(0, 4));
                    } catch (NumberFormatException e) {
                        Log.w("Parser", "Ошибка даты");
                    }
                }

                String imageUrl = "";
                if (gbGame.getImage() != null && gbGame.getImage().originalUrl != null) {
                    imageUrl = gbGame.getImage().originalUrl;
                }

                String description = gbGame.getDescription() != null ? gbGame.getDescription() : "Нет описания.";

                GameEntity entity = new GameEntity(
                        gbGame.getName(),
                        genres,
                        platforms,
                        year,
                        description,
                        imageUrl
                );
                mGameDao.insert(entity);
            }
            Log.d("Database", "Успешно сохранено " + gbGames.size() + " игр.");
        });
    }

    // !!! ИСПРАВЛЕНИЕ: Используем GbGame.NameObject вместо ObjectInfo !!!
    private String formatNameObjects(List<GbGame.NameObject> objects) {
        if (objects == null || objects.isEmpty()) return "Неизвестно";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < objects.size(); i++) {
            sb.append(objects.get(i).name); // Доступ к полю name напрямую
            if (i < objects.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }
}