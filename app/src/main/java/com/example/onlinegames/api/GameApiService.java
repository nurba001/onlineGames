package com.example.onlinegames.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;


public interface GameApiService {

    // Запрос к FreeToGame для получения списка всех бесплатных игр
    // Ключ API не требуется!
    @GET("games")
    Call<List<ApiGame>> getGames(); // Возвращает List<ApiGame>, а не GameResponse
}
