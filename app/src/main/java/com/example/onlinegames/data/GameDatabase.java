package com.example.onlinegames.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 1. Увеличили версию до 3
@Database(entities = {GameEntity.class}, version =3, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {

    public abstract GameDao gameDao();

    private static volatile GameDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GameDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GameDatabase.class, "game_database")
                            // 2. Добавили стратегию миграции
                            // Это удалит старую базу данных и создаст новую
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}