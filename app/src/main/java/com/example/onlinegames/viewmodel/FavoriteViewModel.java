package com.example.onlinegames.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

import java.util.List;

// ViewModel для Экрана "Избранное"
public class FavoriteViewModel extends AndroidViewModel {

    // 1. Объявляем репозиторий как поле класса
    private final GameRepository mRepository;
    private final LiveData<List<GameEntity>> mFavoriteGames;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        // 2. В конструкторе ТОЛЬКО создаем репозиторий
        mRepository = new GameRepository(application);
        // 3. Получаем данные из репозитория
        mFavoriteGames = mRepository.getFavoriteGames();
    }

    // Получить ТОЛЬКО избранные игры
    public LiveData<List<GameEntity>> getFavoriteGames() {
        return mFavoriteGames;
    }
}
