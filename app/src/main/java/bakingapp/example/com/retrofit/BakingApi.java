package bakingapp.example.com.retrofit;

import java.util.List;

import bakingapp.example.com.retrofit.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingApi {
    @GET("android-baking-app-json")
    Call<List<Recipe>> loadRecipes();
}
