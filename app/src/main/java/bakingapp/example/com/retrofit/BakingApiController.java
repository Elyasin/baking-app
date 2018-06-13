package bakingapp.example.com.retrofit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import bakingapp.example.com.AppExecutors;
import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.Ingredient;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.db.model.RecipeWithRelations;
import bakingapp.example.com.db.model.Step;
import bakingapp.example.com.retrofit.model.IngredientApi;
import bakingapp.example.com.retrofit.model.RecipeApi;
import bakingapp.example.com.retrofit.model.StepApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingApiController implements Callback<List<RecipeApi>> {


    public interface OnDataLoadedListener {
        void displayRecipes(List<RecipeWithRelations> recipes);
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
        Call<List<RecipeApi>> call = bakingApi.loadRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<List<RecipeApi>> call, @NonNull Response<List<RecipeApi>> response) {

        if (response.isSuccessful()) {

            final List<RecipeApi> recipeApiList = response.body();

            if (recipeApiList == null || recipeApiList.isEmpty()) {
                Log.e(TAG, "No recipes available");
                return;
            }

            AppExecutors.getsInstance().roomDb().execute(new Runnable() {
                @Override
                public void run() {

                    final List<RecipeWithRelations> recipes = insertRecipesIntoRoom(recipeApiList);

                    ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mListener.displayRecipes(recipes);
                        }
                    });
                }
            });

        } else {
            Log.e(TAG, "Unsuccessful data retrieval");
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<RecipeApi>> call, @NonNull Throwable t) {
        t.printStackTrace();
    }


    private List<RecipeWithRelations> insertRecipesIntoRoom(List<RecipeApi> recipeApiList) {

        RecipeDatabase db = RecipeDatabase.getsInstance(mContext.getApplicationContext());

        List<Recipe> recipeList = new ArrayList<>(recipeApiList.size());
        List<Ingredient> ingredientList = new ArrayList<>();
        List<Step> stepList = new ArrayList<>();

        for (RecipeApi recipeApi : recipeApiList) {

            recipeList.add(new Recipe(recipeApi));

            for (IngredientApi ingredientApi : recipeApi.getIngredients()) {
                ingredientList.add(new Ingredient(recipeApi.getId(), ingredientApi));
            }

            for (StepApi stepApi : recipeApi.getSteps()) {
                stepList.add(new Step(recipeApi.getId(), stepApi));
            }

        }

        db.recipeWithRelationsDAO().insertRecipesWithRelations(
                recipeList, ingredientList, stepList
        );

        return db.recipeWithRelationsDAO().loadRecipesWithRelations();

    }
}
