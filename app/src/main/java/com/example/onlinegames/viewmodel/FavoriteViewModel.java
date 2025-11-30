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

    private final LiveData<List<GameEntity>> mFavoriteGames;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        GameRepository mRepository = new GameRepository(application);
        mFavoriteGames = mRepository.getFavoriteGames();
    }

    // Получить ТОЛЬКО избранные игры
    public LiveData<List<GameEntity>> getFavoriteGames() {
        return mFavoriteGames;
    }
}
