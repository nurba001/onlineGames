package com.example.onlinegames.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.onlinegames.R;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.databinding.ActivityDetailBinding;
import com.example.onlinegames.viewmodel.DetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DetailViewModel detailViewModel;
    private GameEntity currentGame;
    private int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameId = getIntent().getIntExtra("GAME_ID", -1);
        if (gameId == -1) {
            Toast.makeText(this, "Ошибка: Игра не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        detailViewModel.getGameById(gameId).observe(this, gameEntity -> {
            if (gameEntity != null) {
                currentGame = gameEntity;
                updateUI(gameEntity);
            }
        });

        // --- НОВАЯ ЛОГИКА ДЛЯ КНОПОК ---

        // Кнопка для добавления/удаления из избранного
        binding.buttonToggleFavorite.setOnClickListener(v -> {
            toggleFavoriteStatus();
        });

        // Кнопка для сохранения отзыва
        binding.buttonSaveReview.setOnClickListener(v -> {
            saveReviewData();
        });
    }

    private void updateUI(GameEntity game) {
        Glide.with(this)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.detailImageCover);

        binding.detailTextTitle.setText(game.getName());
        binding.detailTextDescription.setText(game.getDescription());
        binding.detailTextPlatform.setText("Платформы: " + game.getPlatform());
        binding.detailTextGenre.setText("Жанр: " + game.getGenre());

        binding.editTextComment.setText(game.getComment());
        binding.ratingBarUser.setRating(game.getUserRating());

        // Обновляем текст кнопки избранного
        if (game.isFavorite()) {
            binding.buttonToggleFavorite.setText("Удалить из избранного");
        } else {
            binding.buttonToggleFavorite.setText("Добавить в избранное");
        }
    }

    // --- НОВЫЕ МЕТОДЫ ДЛЯ ОБРАБОТКИ КНОПОК ---

    private void toggleFavoriteStatus() {
        if (currentGame == null) return;

        // Инвертируем статус (true -> false, false -> true)
        boolean newFavoriteStatus = !currentGame.isFavorite();
        currentGame.setFavorite(newFavoriteStatus);

        detailViewModel.updateGame(currentGame);

        if (newFavoriteStatus) {
            Toast.makeText(this, "Добавлено в избранное!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Удалено из избранного", Toast.LENGTH_SHORT).show();
        }
        // UI обновится автоматически благодаря LiveData
    }

    private void saveReviewData() {
        if (currentGame == null) return;

        String comment = binding.editTextComment.getText().toString();
        float rating = binding.ratingBarUser.getRating();

        // Обновляем только поля отзыва
        currentGame.setComment(comment);
        currentGame.setUserRating(rating);

        detailViewModel.updateGame(currentGame);

        Toast.makeText(this, "Отзыв сохранен!", Toast.LENGTH_SHORT).show();
    }
}
