package com.example.onlinegames.data;

import com.example.onlinegames.api.ApiGame;
import java.util.ArrayList;
import java.util.List;

public class GameConverter {

    public static List<GameEntity> fromApiGames(List<ApiGame> apiGames) {
        List<GameEntity> gameEntities = new ArrayList<>();

        if (apiGames == null) {
            return gameEntities; // Если входные данные равны null, возвращается пустой список.
        }

        for (ApiGame apiGame : apiGames) {
            if (apiGame == null) continue; // Пропустить, если какой-либо игровой объект в списке равен null

            // Безопасное извлечение данных из ApiGame с указанием значений по умолчанию.
            String year = "";
            if (apiGame.getReleaseDate() != null && apiGame.getReleaseDate().length() >= 4) {
                year = apiGame.getReleaseDate().substring(0, 4);
            }

            String genreStr = apiGame.getGenre() != null ? apiGame.getGenre() : "Unknown Genre";
            String platformStr = apiGame.getPlatform() != null ? apiGame.getPlatform() : "Unknown Platform";
            String imageUrl = apiGame.getImageUrl() != null ? apiGame.getImageUrl() : "";
            String description = apiGame.getDescription() != null ? apiGame.getDescription() : "No description available.";
            String title = apiGame.getTitle() != null ? apiGame.getTitle() : "Untitled";

            // Создайте сущность, используя единственный и правильный конструктор из класса GameEntity
            GameEntity entity = new GameEntity(
                    apiGame.getId(),
                    title,
                    imageUrl,
                    platformStr,
                    genreStr,
                    year,
                    description,
                    false, 0, "");
            gameEntities.add(entity);
        }
        return gameEntities;
    }
}
