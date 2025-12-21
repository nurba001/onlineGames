package com.example.onlinegames.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainViewModel extends AndroidViewModel {

    private final GameRepository mRepository;
    private final LiveData<List<GameEntity>> mAllGames;

    private final MutableLiveData<String> currentGenreFilter = new MutableLiveData<>("Все");
    private final MutableLiveData<String> currentPlatformFilter = new MutableLiveData<>("Все");
    private final MutableLiveData<String> searchQuery = new MutableLiveData<>("");
    private final MutableLiveData<Boolean> isFavoriteMode = new MutableLiveData<>(false);

    private final MediatorLiveData<List<GameEntity>> _gamesDisplay = new MediatorLiveData<>();
    public final LiveData<List<GameEntity>> gamesDisplay = _gamesDisplay;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application);
        mAllGames = mRepository.getAllGames();

        // Объединяем все источники данных
        _gamesDisplay.addSource(mAllGames, this::filterAndDisplay);
        _gamesDisplay.addSource(currentGenreFilter, g -> filterAndDisplay(mAllGames.getValue()));
        _gamesDisplay.addSource(currentPlatformFilter, p -> filterAndDisplay(mAllGames.getValue()));
        _gamesDisplay.addSource(searchQuery, q -> filterAndDisplay(mAllGames.getValue()));
        _gamesDisplay.addSource(isFavoriteMode, f -> filterAndDisplay(mAllGames.getValue()));
    }

    private void filterAndDisplay(List<GameEntity> allGames) {
        if (allGames == null) return;

        List<GameEntity> filteredList = new ArrayList<>();
        String genre = currentGenreFilter.getValue();
        String platform = currentPlatformFilter.getValue();
        String query = searchQuery.getValue().toLowerCase().trim();
        boolean favoritesOnly = isFavoriteMode.getValue();

        for (GameEntity game : allGames) {
            // 1. Проверка на избранное
            if (favoritesOnly && !game.isFavorite()) continue;

            // 2. Проверка на жанр
            if (!genre.equals("Все") && !game.getGenre().contains(genre)) continue;

            // 3. Проверка на платформу
            if (!platform.equals("Все") && !game.getPlatform().contains(platform)) continue;

            // 4. Проверка на поиск (по названию)
            if (!query.isEmpty() && !game.getName().toLowerCase().contains(query)) continue;

            filteredList.add(game);
        }

        _gamesDisplay.setValue(filteredList);
    }

    public void setGenreFilter(String genre) {
        currentGenreFilter.setValue(genre);
    }

    public void setPlatformFilter(String platform) {
        currentPlatformFilter.setValue(platform);
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

    public void addGame(GameEntity game) { mRepository.insertGame(game); }
    public void deleteGame(GameEntity game) { mRepository.deleteGame(game); }
    public void updateGame(GameEntity game) { mRepository.updateGame(game); }
}
