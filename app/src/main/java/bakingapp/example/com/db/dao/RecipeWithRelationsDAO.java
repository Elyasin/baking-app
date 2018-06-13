package bakingapp.example.com.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.List;

import bakingapp.example.com.db.model.Ingredient;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.db.model.RecipeWithRelations;
import bakingapp.example.com.db.model.Step;

@Dao
public abstract class RecipeWithRelationsDAO {

    @Transaction
    @Query("select * from recipes where id = :recipeId")
    public abstract LiveData<RecipeWithRelations> loadRecipeWithRelations(int recipeId);

    @Transaction
    @Query("select * from recipes")
    public abstract List<RecipeWithRelations> loadRecipesWithRelations();

    @Transaction
    @Query("select * from recipes")
    public abstract LiveData<List<RecipeWithRelations>> loadRecipesWithRelationsLive();

    @Insert
    public abstract void insertRecipes(List<Recipe> recipes);

    @Insert
    public abstract void insertIngredients(List<Ingredient> ingredients);

    @Insert
    public abstract void insertSteps(List<Step> steps);

    @Transaction
    public void insertRecipesWithRelations(List<Recipe> recipes, List<Ingredient> ingredients, List<Step> steps) {
        insertRecipes(recipes);
        insertIngredients(ingredients);
        insertSteps(steps);
    }


}
