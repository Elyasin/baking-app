package bakingapp.example.com.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import bakingapp.example.com.db.dao.IngredientDAO;
import bakingapp.example.com.db.dao.RecipeDAO;
import bakingapp.example.com.db.dao.RecipeWithRelationsDAO;
import bakingapp.example.com.db.dao.StepDAO;
import bakingapp.example.com.db.model.Ingredient;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.db.model.Step;

@Database(entities = {Recipe.class, Ingredient.class, Step.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    public static final String TAG = RecipeDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "recipes";

    private static RecipeDatabase sInstance;

    public static RecipeDatabase getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (RecipeDatabase.class) {
                Log.d(TAG, "Creating recipes database");
                sInstance = Room.databaseBuilder(
                        context.getApplicationContext(),
                        RecipeDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract RecipeDAO recipeDAO();

    public abstract StepDAO stepDAO();

    public abstract IngredientDAO ingredientDAO();

    public abstract RecipeWithRelationsDAO recipeWithRelationsDAO();
}
