package com.example.onlinegames.api;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Интерфейс для Giant Bomb API
public interface GameApiService {

    // ВАЖНО: Мы меняем метод, чтобы он принимал 4 параметра,
    // которые требует Giant Bomb, и возвращал новый тип ответа (GbResponse)
    @GET("games/")
    Call<GbResponse> getGames(
            @Query("api_key") String apiKey,     // Твой API ключ (можно оставить пустым)
            @Query("format") String format,      // Обязательно "json"
            @Query("limit") int limit,           // Ограничение на количество игр (40)
            @Query("sort") String sort           // Сортировка (по дате выхода)
    );
}
