package com.example.onlinegames.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.onlinegames.data.GameDatabase;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final GameRepository mRepository;
    private final LiveData<List<GameEntity>> mAllGames;

    // !!!  КЛЮЧ GIANT BOMB !!!
    private static final String API_KEY = "724047fdf869b1cd366ca70da7ed7a0034d6c504";

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application);
        mAllGames = mRepository.getAllGames();

        loadData();
    }

    public LiveData<List<GameEntity>> getAllGames() {
        return mAllGames;
    }

    public LiveData<List<GameEntity>> searchGames(String query) {
        return mRepository.searchGames(query);
    }

    public LiveData<List<GameEntity>> filterGames(String platform, String genre) {
        return mRepository.filterGames(platform, genre);
    }

    private void loadData() {
        // Мы используем GameDatabase.databaseWriteExecutor для фоновых операций
        GameDatabase.databaseWriteExecutor.execute(() -> {
            // 1. Проверяем, пуста ли база
            if (mRepository.getGameCount() == 0) {
                Log.d("ViewModel", "База данных пуста. Загружаем данные из API.");

                // 2. Пытаемся загрузить из сети (Giant Bomb)
                mRepository.fetchGamesFromApi(API_KEY);

                //  Мы удалили тестовые данные, чтобы загрузка из API была основной.
            } else {
                Log.d("ViewModel", "База данных уже содержит игры.");
            }
        });
    }
}