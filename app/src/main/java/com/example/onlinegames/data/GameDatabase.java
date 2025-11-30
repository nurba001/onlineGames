package com.example.onlinegames.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// @Database указывает Room, что это класс базы данных.
// entities = {GameEntity.class} — перечисляем все наши таблицы (Entity).
// version = 1 — версия базы данных.
@Database(entities = {GameEntity.class}, version = 1, exportSchema = false)
public abstract class GameDatabase extends RoomDatabase {

    // Абстрактный метод, который возвращает наш DAO
    public abstract GameDao gameDao();

    // --- Singleton (Синглтон) ---
    // Мы делаем так, чтобы у приложения был только ОДИН экземпляр
    // этой базы данных (это экономит ресурсы).

    private static volatile GameDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    // ExecutorService нужен для выполнения операций с базой данных
    // (например, вставка) в фоновом потоке, чтобы не "вешать" UI.
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Метод, который создает или возвращает существующий экземпляр БД
    public static GameDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GameDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GameDatabase.class, "game_database")
                            // .addCallback(sRoomDatabaseCallback) // Мы добавим это позже
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Мы можем добавить Callback, чтобы заполнить базу данных
    // начальными данными (играми) при первом запуске.
    // Но пока сделаем основу.
}
