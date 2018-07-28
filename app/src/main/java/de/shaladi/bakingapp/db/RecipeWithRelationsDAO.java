package de.shaladi.bakingapp.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.util.ArrayList;
import java.util.List;

import de.shaladi.bakingapp.model.Ingredient;
import de.shaladi.bakingapp.model.Recipe;
import de.shaladi.bakingapp.model.RecipeAndRelations;
import de.shaladi.bakingapp.model.Step;

@Dao
public abstract class RecipeWithRelationsDAO {


    private static final String TAG = RecipeWithRelationsDAO.class.getSimpleName();


    // Retrive ingredients of a recipe
    @Query("select * from ingredients where recipe_id = :recipeId")
    public abstract List<Ingredient> getIngredientsByRecipeId(int recipeId);

    // Retrive the recipe name
    @Query("select name from recipes where id = :recipeId")
    public abstract String getRecipeName(int recipeId);

    // Retreive data with relations

    @Transaction
    @Query("select * from recipes where id = :recipeId")
    public abstract LiveData<RecipeAndRelations> getRecipeById(int recipeId);

    @Transaction
    @Query("select * from recipes")
    public abstract LiveData<List<RecipeAndRelations>> getAllRecipes();

    @Query("select count(1) from recipes")
    public abstract int checkRecipes();


    //Save data

    @Insert
    public abstract void insertRecipes(List<Recipe> recipes);

    @Insert
    public abstract void insertIngredients(List<Ingredient> ingredients);

    @Insert
    public abstract void insertSteps(List<Step> steps);


    @Transaction
    public void save(List<Recipe> recipes) {

        //first insert recipes; foreign key constraints
        insertRecipes(recipes);

        List<Ingredient> ingredients = new ArrayList<>();
        List<Step> steps = new ArrayList<>();

        for (Recipe recipe : recipes) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                ingredient.setRecipeID(recipe.getId());
                ingredients.add(ingredient);
            }
            for (Step step : recipe.getSteps()) {
                step.setRecipeID(recipe.getId());
                steps.add(step);
            }
        }

        insertIngredients(ingredients);
        insertSteps(steps);

    }


    // check for data
    public boolean hasData() {
        return checkRecipes() > 0;
    }

}
