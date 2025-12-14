package com.example.onlinegames.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.onlinegames.data.GameDatabase;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.data.GameRepository;

import java.util.List;
import java.util.Objects;

public class MainViewModel extends AndroidViewModel {

    private final GameRepository mRepository;
    private LiveData<List<GameEntity>> currentDbSource;

    // Triggers for search and filter
    private final MutableLiveData<String> queryFilter = new MutableLiveData<>("");
    private final MutableLiveData<String> platformFilter = new MutableLiveData<>("Все");
    private final MutableLiveData<String> genreFilter = new MutableLiveData<>("Все");

    // Single LiveData exposed to the UI
    private final MediatorLiveData<List<GameEntity>> games = new MediatorLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new GameRepository(application);

        // Observer that reacts to any change in filters
        Observer<Object> observer = o -> {
            if (currentDbSource != null) {
                games.removeSource(currentDbSource);
            }

            String query = queryFilter.getValue();
            String platform = Objects.requireNonNull(platformFilter.getValue());
            String genre = Objects.requireNonNull(genreFilter.getValue());

            // Decide which repository method to call
            if (query != null && !query.isEmpty()) {
                currentDbSource = mRepository.searchGames(query);
            } else {
                currentDbSource = mRepository.filterGames(platform, genre);
            }
            games.addSource(currentDbSource, games::setValue);
        };

        games.addSource(queryFilter, observer);
        games.addSource(platformFilter, observer);
        games.addSource(genreFilter, observer);

        // --- THE FIX ---
        // Trigger the initial load by setting a value. This will fire the observer.
        platformFilter.setValue(platformFilter.getValue());

        loadData();
    }

    // The UI will observe this single LiveData
    public LiveData<List<GameEntity>> getGames() {
        return games;
    }

    // --- Methods to trigger updates from the UI ---

    public void setSearchQuery(String query) {
        if (!Objects.equals(query, queryFilter.getValue())) {
            queryFilter.setValue(query);
        }
    }

    public void setPlatformFilter(String platform) {
        if (!Objects.equals(platform, platformFilter.getValue())) {
            platformFilter.setValue(platform);
        }
    }

    public void setGenreFilter(String genre) {
        if (!Objects.equals(genre, genreFilter.getValue())) {
            genreFilter.setValue(genre);
        }
    }

    private void loadData() {
        GameDatabase.databaseWriteExecutor.execute(() -> {
            if (mRepository.getGameCount() == 0) {
                Log.d("ViewModel", "База данных пуста. Загружаем данные из API.");
                mRepository.fetchGamesFromApi();
            } else {
                Log.d("ViewModel", "База данных уже содержит игры.");
            }
        });
    }
}
