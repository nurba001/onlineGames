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
    private GameEntity currentGame; // Здесь мы храним текущую загруженную игру
    private int gameId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Получаем ID игры из Intent
        gameId = getIntent().getIntExtra("GAME_ID", -1);
        if (gameId == -1) {
            Toast.makeText(this, "Ошибка: Игра не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Инициализируем ViewModel
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // 3. Подписываемся на данные игры
        detailViewModel.getGameById(gameId).observe(this, new Observer<GameEntity>() {
            @Override
            public void onChanged(GameEntity gameEntity) {
                if (gameEntity != null) {
                    currentGame = gameEntity; // Запоминаем игру
                    updateUI(gameEntity);     // Обновляем экран
                }
            }
        });

        // 4. Настройка кнопки "Сохранить в избранное"
        binding.buttonSaveFavorite.setOnClickListener(v -> {
            saveFavoriteData();
        });
    }

    private void updateUI(GameEntity game) {
        // Картинка
        Glide.with(this)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.detailImageCover); // Убедись, что ID в XML такой же (detailImageCover или detailCover)

        // Тексты
        binding.detailTextTitle.setText(game.getName());
        binding.detailTextDescription.setText(game.getDescription());
        binding.detailTextPlatform.setText("Платформы: " + game.getPlatform());
        binding.detailTextGenre.setText("Жанр: " + game.getGenre());

        // Если мы уже сохраняли отзыв раньше, показываем его
        binding.editTextComment.setText(game.getComment());
        binding.ratingBarUser.setRating(game.getUserRating());

        // Меняем текст кнопки, если игра уже в избранном
        if (game.isFavorite()) {
            binding.buttonSaveFavorite.setText("Обновить отзыв");
        } else {
            binding.buttonSaveFavorite.setText("Сохранить в избранное");
        }
    }

    private void saveFavoriteData() {
        if (currentGame == null) return;

        // 1. Берем данные из полей ввода
        String comment = binding.editTextComment.getText().toString();
        float rating = binding.ratingBarUser.getRating();

        // 2. Обновляем объект игры
        currentGame.setComment(comment);
        currentGame.setUserRating(rating);
        currentGame.setFavorite(true); // Теперь это ИЗБРАННАЯ игра!

        // 3. Сохраняем в базу данных через ViewModel
        detailViewModel.updateGame(currentGame);

        Toast.makeText(this, "Добавлено в избранное!", Toast.LENGTH_SHORT).show();

        // Можно закрыть экран после сохранения, или остаться
        finish();
    }
}