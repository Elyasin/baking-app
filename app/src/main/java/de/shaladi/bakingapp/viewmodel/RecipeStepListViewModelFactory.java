package de.shaladi.bakingapp.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import de.shaladi.bakingapp.db.RecipeDatabase;

public class RecipeStepListViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final RecipeDatabase db;
    private final int recipeId;

    public RecipeStepListViewModelFactory(RecipeDatabase db, int recipeId) {
        this.db = db;
        this.recipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeStepViewModel(db, recipeId);
    }
}
