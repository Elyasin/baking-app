package bakingapp.example.com;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.Arrays;

import bakingapp.example.com.adapters.RecipeAdapter;
import bakingapp.example.com.model.Recipe;
import bakingapp.example.com.retrofit.BakingApiController;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPES_ARRAY_KEY = "recipe_array_key";
    public static final String RECIPE_POSITION_KEY = "recipe_position_key";
    public static final String RECIPE_STEP_POSITION_KEY = "recipe_step_position_key";

    private Recipe[] mRecipeArray;

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_activity_title);
        setSupportActionBar(toolbar);

        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.rv_recipes);

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        mRecyclerView.setHasFixedSize(true);

        if (getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
            else
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        } else {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
            else
                mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }

        mRecyclerView.setSaveEnabled(true);
        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (savedInstanceState == null) {
            BakingApiController bakingApiController = new BakingApiController(this);
            bakingApiController.start();
        } else {
            Parcelable[] parcelables = savedInstanceState.getParcelableArray(RECIPES_ARRAY_KEY);
            mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
            displayRecipes(mRecipeArray);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(RECIPES_ARRAY_KEY, mRecipeArray);
        super.onSaveInstanceState(outState);
    }

    public void displayRecipes(Recipe[] recipeArray) {
        mRecipeArray = recipeArray;
        mRecipeAdapter.swap(mRecipeArray);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
