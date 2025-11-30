package com.example.onlinegames.viewmodel;
import android.app.Application;

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

    // ВСТАВЬ СЮДА СВОЙ РЕАЛЬНЫЙ КЛЮЧ RAWG, КОГДА ПОЛУЧИШЬ ЕГО!
    // Пока используем заглушку, чтобы код компилировался.
    // Если RAWG заработает, ты просто заменишь эту строку.
    private static final String API_KEY = "TEST_KEY_CHANGE_ME_LATER";

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application);
        mAllGames = mRepository.getAllGames();

        // Запускаем загрузку данных при создании ViewModel
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
        // Проверяем количество игр в базе данных в фоновом потоке
        GameDatabase.databaseWriteExecutor.execute(() -> {
            // Если база пустая (т.е. приложение запускается в первый раз),
            // пробуем скачать игры из интернета
            if (mRepository.getGameCount() == 0) {
                // Вызываем метод загрузки из сети, передавая API ключ
                mRepository.fetchGamesFromApi(API_KEY);
            }
        });
    }
}