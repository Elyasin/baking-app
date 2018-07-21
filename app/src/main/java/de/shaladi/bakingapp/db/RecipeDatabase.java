package de.shaladi.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import de.shaladi.bakingapp.model.Ingredient;
import de.shaladi.bakingapp.model.Recipe;
import de.shaladi.bakingapp.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public static final String TAG = RecipeDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "baking_db";

    private static RecipeDatabase sInstance;

    public static RecipeDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecipeDatabase.class) {
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        RecipeDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract RecipeWithRelationsDAO recipeWithRelationsDAO();
}
