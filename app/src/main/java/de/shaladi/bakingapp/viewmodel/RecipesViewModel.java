package de.shaladi.bakingapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import de.shaladi.bakingapp.model.RecipeAndRelations;
import de.shaladi.bakingapp.repository.Repository;

public class RecipesViewModel extends AndroidViewModel {

    private LiveData<List<RecipeAndRelations>> recipes;

    public RecipesViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<RecipeAndRelations>> getRecipes() {
        if (recipes == null) {
            recipes = Repository.getInstance(this.getApplication()).getAllRecipes();
        }
        return recipes;
    }
}
