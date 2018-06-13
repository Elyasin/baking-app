package bakingapp.example.com;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import bakingapp.example.com.adapters.RecipeAdapter;
import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.RecipeWithRelations;
import bakingapp.example.com.retrofit.BakingApiController;

public class MainActivity extends AppCompatActivity
        implements BakingApiController.OnDataLoadedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPE_ID_KEY = "recipe_id_key";
    public static final String RECIPE_STEP_NO_KEY = "recpie_step_id_key";

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

        Configuration config = getResources().getConfiguration();

        if (config.smallestScreenWidthDp >= 600) {
            if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }

    private void loadRecipes() {

        RecipeDatabase db = RecipeDatabase.getsInstance(getApplicationContext());

        LiveData<List<RecipeWithRelations>> listLiveData =
                db.recipeWithRelationsDAO().loadRecipesWithRelationsLive();

        listLiveData.observe(this, new Observer<List<RecipeWithRelations>>() {
            @Override
            public void onChanged(@Nullable List<RecipeWithRelations> recipes) {
                if (recipes == null || recipes.isEmpty()) {
                    new BakingApiController(MainActivity.this).start();
                } else {
                    displayRecipes(recipes);
                }
            }
        });

    }

    public void displayRecipes(List<RecipeWithRelations> recipes) {
        mRecipeAdapter.setRecipes(recipes);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
