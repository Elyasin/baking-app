package de.shaladi.bakingapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import de.shaladi.bakingapp.db.RecipeDatabase;
import de.shaladi.bakingapp.model.RecipeAndRelations;

public class RecipeStepViewModel extends ViewModel {

    private LiveData<RecipeAndRelations> recipe;

    RecipeStepViewModel(RecipeDatabase db, int recipeId) {
        recipe = db.recipeWithRelationsDAO().getRecipeById(recipeId);
    }

    public LiveData<RecipeAndRelations> getRecipe() {
        return recipe;
    }

}
