package com.example.onlinegames.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GameApiService {

    // Запрос списка игр
    // Пример запроса: https://api.rawg.io/api/games?key=ТВОЙ_КЛЮЧ&page_size=20&search=Witcher
    @GET("games")
    Call<GameResponse> getGames(
            @Query("key") String apiKey,         // Твой API ключ
            @Query("page_size") int pageSize,    // Сколько игр загружать (например, 20)
            @Query("search") String searchQuery, // Поисковый запрос (если есть)
            @Query("ordering") String ordering   // Сортировка (например, "-rating")
    );
}
