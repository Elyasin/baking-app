package de.shaladi.bakingapp.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import de.shaladi.bakingapp.AppExecutors;
import de.shaladi.bakingapp.api.BakingApiService;
import de.shaladi.bakingapp.db.RecipeDatabase;
import de.shaladi.bakingapp.db.RecipeWithRelationsDAO;
import de.shaladi.bakingapp.model.Recipe;
import de.shaladi.bakingapp.model.RecipeAndRelations;
import retrofit2.Response;

public class Repository {


    private static final String TAG = Repository.class.getSimpleName();


    private static final Object LOCK = new Object();


    private static Repository sInstance;

    private final RecipeWithRelationsDAO dao;


    private Repository(Application application) {
        this.dao = RecipeDatabase.getsInstance(application).recipeWithRelationsDAO();
    }


    public synchronized static Repository getInstance(Application application) {
        Log.d(TAG, "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository(application);
                Log.d(TAG, "Made new repository");
            }
        }
        return sInstance;
    }


    public LiveData<List<RecipeAndRelations>> getAllRecipes() {
        refreshRecipes();
        //return LiveData from db
        return dao.getAllRecipes();
    }

    private void refreshRecipes() {
        AppExecutors.getsInstance().roomDb().execute(() -> {
            boolean dataExists = dao.hasData();
            if (!dataExists) {
                try {
                    Response<List<Recipe>> response =
                            BakingApiService.retrofit.create(BakingApiService.class)
                                    .getRecipes()
                                    .execute();
                    dao.save(response.body());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
