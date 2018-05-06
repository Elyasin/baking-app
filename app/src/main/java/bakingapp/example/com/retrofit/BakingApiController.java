package bakingapp.example.com.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import bakingapp.example.com.MainActivity;
import bakingapp.example.com.model.Recipe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingApiController implements Callback<List<Recipe>> {

    private static final String BASE_URL = "http://go.udacity.com/";

    private static final String TAG = BakingApiController.class.getSimpleName();

    private MainActivity mActivity;

    public BakingApiController(MainActivity mActivity) {
        this.mActivity = mActivity;
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
            List<Recipe> recipeList = response.body();
            mActivity.displayRecipes(recipeList.toArray(new Recipe[recipeList.size()]));
        } else {
            Log.e(TAG, response.errorBody().toString());
        }
    }

    @Override
    public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
        t.printStackTrace();
    }
}
