package com.example.onlinegames.data;

import com.example.onlinegames.api.ApiGame;
import java.util.ArrayList;
import java.util.List;

public class GameConverter {

    public static List<GameEntity> fromApiGames(List<ApiGame> apiGames) {
        List<GameEntity> gameEntities = new ArrayList<>();

        if (apiGames == null) {
            return gameEntities; // Return empty list if the input is null
        }

        for (ApiGame apiGame : apiGames) {
            if (apiGame == null) continue; // Skip if any game object in the list is null

            // Safely extract data from ApiGame, providing default values
            String year = "";
            if (apiGame.getReleaseDate() != null && apiGame.getReleaseDate().length() >= 4) {
                year = apiGame.getReleaseDate().substring(0, 4);
            }

            String genreStr = apiGame.getGenre() != null ? apiGame.getGenre() : "Unknown Genre";
            String platformStr = apiGame.getPlatform() != null ? apiGame.getPlatform() : "Unknown Platform";
            String imageUrl = apiGame.getImageUrl() != null ? apiGame.getImageUrl() : "";
            String description = apiGame.getDescription() != null ? apiGame.getDescription() : "No description available.";
            String title = apiGame.getTitle() != null ? apiGame.getTitle() : "Untitled";

            // Create the entity using the single, correct constructor from GameEntity
            GameEntity entity = new GameEntity(
                    apiGame.getId(),
                    title,
                    imageUrl,
                    platformStr,
                    genreStr,
                    year,
                    description
            );
            gameEntities.add(entity);
        }
        return gameEntities;
    }
}
