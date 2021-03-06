package de.shaladi.bakingapp.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import de.shaladi.bakingapp.R;
import de.shaladi.bakingapp.adapters.RecipeStepAdapter;
import de.shaladi.bakingapp.db.RecipeDatabase;
import de.shaladi.bakingapp.viewmodel.RecipeStepListViewModelFactory;
import de.shaladi.bakingapp.viewmodel.RecipeStepViewModel;

/**
 * An activity representing a list of Instructions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepsListActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepsListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private int mRecipeId;

    private RecipeStepAdapter mRecipeStepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);

        if (getIntent().hasExtra(MainActivity.RECIPE_ID_KEY)) {

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            // Show the Up button in the action bar.
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }

            if (findViewById(R.id.recipe_step_detail_container) != null) {
                // The detail container view will be present only in the
                // large-screen layouts (res/values-w900dp).
                // If this view is present, then the
                // activity should be in two-pane mode.
                mTwoPane = true;
            }


            mRecipeId = getIntent().getIntExtra(MainActivity.RECIPE_ID_KEY, -1);
            if (mRecipeId == -1)
                throw new IllegalArgumentException("Recipe ID should be > 0.");


            RecipeDatabase db = RecipeDatabase.getsInstance(this);
            RecipeStepListViewModelFactory factory = new RecipeStepListViewModelFactory(db, mRecipeId);
            RecipeStepViewModel viewModel =
                    ViewModelProviders.of(this, factory).get(RecipeStepViewModel.class);
            viewModel.getRecipe().observe(this, recipe -> {
                mRecipeStepAdapter.setRecipe(recipe);
                getSupportActionBar().setTitle(recipe.getName());
            });


            RecyclerView mRecyclerView = findViewById(R.id.recipe_steps_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mRecipeStepAdapter = new RecipeStepAdapter(
                    this,
                    mTwoPane);
            mRecyclerView.setAdapter(mRecipeStepAdapter);


        } else {
            Log.e(TAG, "Intent does not have an 'extra'. Recipe object cannot be retrieved. Activity ended.");
            Toast.makeText(this,
                    "Intent does not have an 'extra'. Recipe object cannot be retrieved. Activity ended.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MainActivity.RECIPE_ID_KEY, mRecipeId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
