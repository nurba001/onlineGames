package com.example.onlinegames.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

public class DetailViewModel extends AndroidViewModel {
    private final GameRepository repository;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        repository = new GameRepository(application);
    }

    public LiveData<GameEntity> getGameById(int gameId) {
        return repository.getGameById(gameId);
    }

    public void updateGame(GameEntity game) {
        repository.updateGame(game);
    }

    // Метод для удаления игры (DELETE)
    public void deleteGame(GameEntity game) {
        repository.deleteGame(game);
    }
}