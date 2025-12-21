package com.example.onlinegames.view;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.onlinegames.data.GameEntity;
import com.example.onlinegames.databinding.ActivityAddGameBinding;
import com.example.onlinegames.viewmodel.MainViewModel;
import java.util.Random;

public class AddGameActivity extends AppCompatActivity {
    private ActivityAddGameBinding binding;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        binding.buttonSaveNewGame.setOnClickListener(v -> {
            String title = binding.editAddTitle.getText().toString().trim();
            String genre = binding.editAddGenre.getText().toString().trim();
            String platform = binding.editAddPlatform.getText().toString().trim();

            if (title.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Заполните название и жанр!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Создаем ID (для ручного добавления используем рандом или таймштамп)
                int id = new Random().nextInt(100000) + 900000;

                GameEntity newGame = new GameEntity(
                        id, title, "", platform, genre, "2024",
                        "Добавлено вручную пользователем", false, 0, ""
                );

                // Вызываем метод Create через репозиторий (через ViewModel)
                // Тебе нужно добавить метод addGame во ViewModel, который просто вызывает repository.insertGame
                viewModel.addGame(newGame);

                Toast.makeText(this, "Игра добавлена!", Toast.LENGTH_SHORT).show();
                finish(); // Закрываем экран
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка при сохранении!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}