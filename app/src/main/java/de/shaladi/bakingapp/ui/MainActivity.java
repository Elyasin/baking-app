package de.shaladi.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import de.shaladi.bakingapp.R;
import de.shaladi.bakingapp.adapters.RecipeAdapter;
import de.shaladi.bakingapp.viewmodel.RecipesViewModel;

public class MainActivity extends AppCompatActivity {

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

        RecipesViewModel viewModel = ViewModelProviders.of(this).get(RecipesViewModel.class);
        viewModel.getRecipes().observe(this, recipes -> {
            //TODO onChange is called twice: 1st with "empty" data, 2nd with data
            //Problem: Progressbar disappears while data still loading. Not so good.
            if (recipes != null && !recipes.isEmpty()) {
                mRecipeAdapter.setRecipes(recipes);
                mProgressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        });

    }

}
