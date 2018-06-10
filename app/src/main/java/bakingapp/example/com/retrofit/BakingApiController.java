package bakingapp.example.com.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import bakingapp.example.com.AppExecutors;
import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.Step;
import bakingapp.example.com.retrofit.model.Ingredient;
import bakingapp.example.com.retrofit.model.Recipe;
import bakingapp.example.com.retrofit.model.RecipeStep;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingApiController implements Callback<List<Recipe>> {


    public interface OnDataLoadedListener {
        void displayRecipes(List<bakingapp.example.com.db.model.Recipe> recipeList);
    }


    private static final String BASE_URL = "http://go.udacity.com/";

    private static final String TAG = BakingApiController.class.getSimpleName();

    private OnDataLoadedListener mListener;
    private Context mContext;


    public BakingApiController(Context context) {
        if (!(context instanceof OnDataLoadedListener))
            throw new IllegalArgumentException("Class must implement OnDataLoadedListener");
        this.mContext = context;
        this.mListener = (OnDataLoadedListener) context;
    }

    public void start() {
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create(gson)).
                build();
        BakingApi bakingApi = retrofit.create(BakingApi.class);
        Call<List<Recipe>> call = bakingApi.loadRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
        if (response.isSuccessful()) {
            final List<Recipe> recipes = response.body();

            AppExecutors.getsInstance().roomDb().execute(new Runnable() {
                @Override
                public void run() {
                    final List<bakingapp.example.com.db.model.Recipe> recipeList = insertRecipesIntoRoom(recipes);
                    ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.displayRecipes(recipeList);
                        }
                    });
                }
            });
        } else {
            Log.e(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        t.printStackTrace();
    }


    private List<bakingapp.example.com.db.model.Recipe> insertRecipesIntoRoom(List<Recipe> recipes) {

        RecipeDatabase db = RecipeDatabase.getsInstance(mContext.getApplicationContext());

        for (Recipe r : recipes) {

            bakingapp.example.com.db.model.Recipe recipe = new bakingapp.example.com.db.model.Recipe(r);
            db.recipeDAO().insertRecipe(recipe);

            for (Ingredient i : r.getIngredients()) {
                bakingapp.example.com.db.model.Ingredient ingredient =
                        new bakingapp.example.com.db.model.Ingredient(recipe.getId(), i);
                db.ingredientDAO().insertIngredient(ingredient);
            }

            for (RecipeStep recipeStep : r.getRecipeSteps()) {
                Step step = new Step(r.getId(), recipeStep);
                db.stepDAO().insertStep(step);
            }

        }

        return db.recipeDAO().loadAllRecipes();

    }
}
