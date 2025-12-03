package com.example.onlinegames.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://www.giantbomb.com/api/";
    private static Retrofit retrofit = null;

    // Метод для получения экземпляра API сервиса
    public static GameApiService getApiService() {
        if (retrofit == null) {

            // 1. Создаем "Клиента", который будет добавлять заголовок User-Agent
            // Giant Bomb ТРЕБУЕТ этот заголовок, иначе он блокирует запрос
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("User-Agent", "GameCatalogApp-Nurbek-StudentProject") // Наше "Имя"
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            // 2. Подключаем этого клиента к Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // <-- Важная добавка!
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GameApiService.class);
    }
}