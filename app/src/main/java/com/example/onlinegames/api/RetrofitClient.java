package com.example.onlinegames.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.rawg.io/api/";
    private static Retrofit retrofit = null;

    // Метод для получения экземпляра API сервиса
    public static GameApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(GameApiService.class);
    }
}
