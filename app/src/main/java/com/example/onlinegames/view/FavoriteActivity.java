package com.example.onlinegames.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlinegames.databinding.ActivityFavoriteBinding;
import com.example.onlinegames.viewmodel.FavoriteViewModel;

public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;
    private FavoriteViewModel viewModel;
    private GameAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Настройка RecyclerView
        adapter = new GameAdapter(this, game -> {
            // При клике открываем детали игры
            Intent intent = new Intent(FavoriteActivity.this, DetailActivity.class);
            intent.putExtra("GAME_ID", game.getId());
            startActivity(intent);
        });

        binding.recyclerViewFavorites.setAdapter(adapter);
        binding.recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        // Подключаем ViewModel
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        // Наблюдаем за списком избранного
        viewModel.getFavoriteGames().observe(this, games -> {
            if (games != null && !games.isEmpty()) {
                adapter.submitList(games);
                binding.textEmpty.setVisibility(View.GONE);
            } else {
                binding.textEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
}