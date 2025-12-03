package com.example.onlinegames.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.onlinegames.R;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.databinding.ActivityDetailBinding; // <-- Импорт ViewBinding
import com.example.onlinegames.viewmodel.DetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private DetailViewModel detailViewModel;
    private GameEntity currentGame; // Храним текущую игру

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. Получаем ID игры, который передали из MainActivity
        int gameId = getIntent().getIntExtra("GAME_ID", -1);
        if (gameId == -1) {
            // Если ID не пришел, закрываем Activity
            Toast.makeText(this, "Ошибка: ID игры не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 2. Инициализация ViewModel
        detailViewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // 3. Подписка на LiveData
        // Мы "наблюдаем" за игрой по ее ID
        detailViewModel.getGameById(gameId).observe(this, new Observer<GameEntity>() {
            @Override
            public void onChanged(GameEntity gameEntity) {
                // Этот код выполнится, когда Room найдет игру
                if (gameEntity != null) {
                    currentGame = gameEntity; // Сохраняем игру
                    updateUI(gameEntity);
                }
            }
        });

        // 4. Настройка кнопки "Сохранить"
        binding.buttonSaveFavorite.setOnClickListener(v -> {
            saveFavoriteData();
        });
    }

    // Метод для обновления UI
    private void updateUI(GameEntity game) {
        // Загружаем обложку
        Glide.with(this)
                .load(game.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.detailImageCover);

        // Заполняем текстовые поля
        binding.detailTextTitle.setText(game.getName());
        binding.detailTextDescription.setText(game.getDescription());
        binding.detailTextPlatform.setText("Платформы: " + game.getPlatform());
        binding.detailTextGenre.setText("Жанр: " + game.getGenre());

        // Заполняем поля "Избранного" (если они уже есть)
        binding.editTextComment.setText(game.getComment());
        binding.ratingBarUser.setRating(game.getUserRating());
    }

    // Метод для сохранения отзыва
    private void saveFavoriteData() {
        if (currentGame == null) return;

        // Получаем данные из полей
        String comment = binding.editTextComment.getText().toString();
        float rating = binding.ratingBarUser.getRating();

        // Обновляем наш объект 'currentGame'
        currentGame.setComment(comment);
        currentGame.setUserRating(rating);
        currentGame.setFavorite(true); // Помечаем как избранное

        // Вызываем метод ViewModel для обновления в базе данных
        detailViewModel.updateGame(currentGame);

        // Показываем сообщение
        Toast.makeText(this, "Отзыв сохранен!", Toast.LENGTH_SHORT).show();
        finish(); // Возвращаемся на главный экран
    }
}
