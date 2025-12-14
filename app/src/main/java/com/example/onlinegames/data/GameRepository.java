package com.example.onlinegames.data;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.onlinegames.api.GameApiService;
import com.example.onlinegames.api.RetrofitClient;
import com.example.onlinegames.api.ApiGame;

import java.util.List;
import java.util.concurrent.ExecutorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameRepository {

    private final GameDao mGameDao;
    private final LiveData<List<GameEntity>> mAllGames;
    private final ExecutorService mExecutorService;

    public GameRepository(Application application) {
        GameDatabase db = GameDatabase.getDatabase(application);
        mGameDao = db.gameDao();
        mAllGames = mGameDao.getAllGames();
        mExecutorService = GameDatabase.databaseWriteExecutor;
        // Мы убрали отсюда вызов loadData(), ViewModel теперь главный.
    }

    public LiveData<List<GameEntity>> getAllGames() { return mAllGames; }
    public LiveData<List<GameEntity>> searchGames(String query) { return mGameDao.searchGames("%" + query + "%"); }
    public LiveData<List<GameEntity>> filterGames(String p, String g) {
        String pq = (p.equals("Все платформы") || p.equals("Все")) ? "%%" : "%" + p + "%";
        String gq = (g.equals("Все жанры") || g.equals("Все")) ? "%%" : "%" + g + "%";
        return mGameDao.filterGames(pq, gq);
    }
    public void insert(GameEntity game) { mExecutorService.execute(() -> mGameDao.insert(game)); }
    public void update(GameEntity game) { mExecutorService.execute(() -> mGameDao.update(game)); }
    public LiveData<List<GameEntity>> getFavoriteGames() { return mGameDao.getFavoriteGames(); }
    public LiveData<GameEntity> getGameById(int id) { return mGameDao.getGameById(id); }
    public int getGameCount() { return mGameDao.getGameCount(); }

    public void fetchGamesFromApi() {
        GameApiService apiService = RetrofitClient.getApiService();
        Call<List<ApiGame>> call = apiService.getGames();

        call.enqueue(new Callback<List<ApiGame>>() {
            @Override
            public void onResponse(@NonNull Call<List<ApiGame>> call, @NonNull Response<List<ApiGame>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ApiGame> allGames = response.body();

                    // --- ИЗМЕНЕНИЕ: Берем только первые 40 игр ---
                    List<ApiGame> limitedGames = allGames.subList(0, Math.min(allGames.size(), 40));

                    List<GameEntity> entities = GameConverter.fromApiGames(limitedGames);

                    mExecutorService.execute(() -> {
                        mGameDao.deleteAll();
                        mGameDao.insertAll(entities);
                        Log.d("API", "Загружено " + entities.size() + " игр.");
                    });
                } else {
                    Log.e("API", "Ошибка: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ApiGame>> call, @NonNull Throwable t) {
                Log.e("API", "Ошибка сети: " + t.getMessage());
            }
        });
    }
}