package de.shaladi.bakingapp;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppExecutors {

    private static AppExecutors sInstance;

    private final Executor roomDb;


    private AppExecutors(Executor roomDb) {
        this.roomDb = roomDb;
    }

    public static AppExecutors getsInstance() {
        if (sInstance == null) {
            synchronized (AppExecutors.class) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor roomDb() {
        return this.roomDb;
    }

}
