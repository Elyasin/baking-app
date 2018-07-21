package de.shaladi.bakingapp.api;

import com.google.gson.GsonBuilder;

import java.util.List;

import de.shaladi.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface BakingApiService {

    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://go.udacity.com/")
            .addConverterFactory(GsonConverterFactory.create(
                    new GsonBuilder()
                            .excludeFieldsWithoutExposeAnnotation()
                            .create()
                    )
            )
            .build();

}
