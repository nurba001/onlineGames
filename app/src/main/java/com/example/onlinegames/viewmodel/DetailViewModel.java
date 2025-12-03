package com.example.onlinegames.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

// ViewModel для Экрана "Детали"
public class DetailViewModel extends AndroidViewModel {

    private final GameRepository mRepository;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application);
    }
    // Получить одну игру по ID
    public LiveData<GameEntity> getGameById(int gameId) {
        return mRepository.getGameById(gameId);
    }
    // Обновить игру (когда пользователь сохраняет отзыв/рейтинг)
    public void updateGame(GameEntity game) {
        mRepository.update(game);
    }
}
