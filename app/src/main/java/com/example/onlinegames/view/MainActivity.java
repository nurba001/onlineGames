package com.example.onlinegames.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinegames.R;
import com.example.onlinegames.databinding.ActivityMainBinding;
import com.example.onlinegames.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;
    private GameAdapter gameAdapter;

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
             Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
             startActivity(intent);
        });

        // Observe the single LiveData from the ViewModel
        mainViewModel.getGames().observe(this, games -> {
            if (games != null) {
                gameAdapter.submitList(games);
            }
        });
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
                mainViewModel.setSearchQuery(query);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mainViewModel.setSearchQuery(newText);
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
                String platform = parent.getItemAtPosition(position).toString();
                mainViewModel.setPlatformFilter(platform.equals("Все платформы") ? "Все" : platform);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genre = parent.getItemAtPosition(position).toString();
                mainViewModel.setGenreFilter(genre.equals("Все жанры") ? "Все" : genre);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}
