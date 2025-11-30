package com.example.onlinegames.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinegames.R;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.databinding.ActivityMainBinding;
import com.example.onlinegames.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;
    private GameAdapter gameAdapter;

    // Переменные для хранения состояния фильтров
    private String currentPlatformFilter = "Все платформы";
    private String currentGenreFilter = "Все жанры";

    private LiveData<List<GameEntity>> currentLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupRecyclerView();
        setupSearchView();
        setupSpinners();

        binding.buttonShowFavorites.setOnClickListener(v -> {
            // Пока просто покажем сообщение, так как FavoriteActivity еще нет
            Toast.makeText(this, "Скоро будет!", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            // startActivity(intent);
        });

        observeLiveData(mainViewModel.getAllGames());
    }

    private void setupRecyclerView() {
        gameAdapter = new GameAdapter(this, game -> {
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("GAME_ID", game.getId());
            startActivity(intent);
        });

        binding.recyclerViewGames.setAdapter(gameAdapter);
        binding.recyclerViewGames.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                // Скрываем клавиатуру после поиска
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    applyFilters();
                } else {
                    performSearch(newText);
                }
                return true;
            }
        });
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> platformAdapter = ArrayAdapter.createFromResource(this,
                R.array.platforms_array, android.R.layout.simple_spinner_item);
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlatform.setAdapter(platformAdapter);

        ArrayAdapter<CharSequence> genreAdapter = ArrayAdapter.createFromResource(this,
                R.array.genres_array, android.R.layout.simple_spinner_item);
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGenre.setAdapter(genreAdapter);

        binding.spinnerPlatform.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPlatformFilter = parent.getItemAtPosition(position).toString();
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGenreFilter = parent.getItemAtPosition(position).toString();
                applyFilters();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilters() {
        // --- ИСПРАВЛЕНИЕ ОШИБКИ ---
        // Мы УБРАЛИ строку: binding.searchView.setQuery("", false);
        // Это разрывает бесконечный цикл.

        String platform = currentPlatformFilter.equals("Все платформы") ? "Все" : currentPlatformFilter;
        String genre = currentGenreFilter.equals("Все жанры") ? "Все" : currentGenreFilter;

        observeLiveData(mainViewModel.filterGames(platform, genre));
    }

    private void performSearch(String query) {
        observeLiveData(mainViewModel.searchGames(query));
    }

    private void observeLiveData(LiveData<List<GameEntity>> newLiveData) {
        if (currentLiveData != null) {
            currentLiveData.removeObservers(this);
        }

        currentLiveData = newLiveData;

        currentLiveData.observe(this, new Observer<List<GameEntity>>() {
            @Override
            public void onChanged(List<GameEntity> games) {
                if (games != null) {
                    gameAdapter.submitList(games);
                }
            }
        });
    }
}