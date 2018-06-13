package bakingapp.example.com;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.RecipeWithRelations;

import static bakingapp.example.com.MainActivity.RECIPE_ID_KEY;
import static bakingapp.example.com.MainActivity.RECIPE_STEP_NO_KEY;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    private RecipeWithRelations mRecipe;
    private int mStepNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Activity never used in tablet when in landscape mode.
        final Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE && config.smallestScreenWidthDp >= 600) {
            finish();
            return;
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {

            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            final int recipeId;
            final boolean replaceFragment;
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Intent intent = getIntent();
                recipeId = intent.getIntExtra(RECIPE_ID_KEY, 0);
                mStepNo = intent.getIntExtra(RECIPE_STEP_NO_KEY, 0);
                replaceFragment = true;
            } else {
                recipeId = savedInstanceState.getInt(RECIPE_ID_KEY);
                mStepNo = savedInstanceState.getInt(RECIPE_STEP_NO_KEY);
                replaceFragment = false;
            }


            RecipeDatabase db = RecipeDatabase.getsInstance(getApplicationContext());
            LiveData<RecipeWithRelations> recipe = db.recipeWithRelationsDAO().loadRecipeWithRelations(recipeId);
            recipe.observe(this, new Observer<RecipeWithRelations>() {
                @Override
                public void onChanged(@Nullable RecipeWithRelations recipeWithRelations) {
                    mRecipe = recipeWithRelations;
                    if (replaceFragment)
                        replaceFragment(mRecipe.steps.get(mStepNo).getStepNo());
                    if (config.orientation == Configuration.ORIENTATION_PORTRAIT)
                        portraitUISetup();
                }
            });

        }
    }

    private void portraitUISetup() {

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setTitle(mRecipe.getName());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {

                    int currentStep = mRecipe.steps.get(mStepNo).getStepNo();

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_previous_step:
                                if (currentStep > 0) {
                                    currentStep--;
                                    replaceFragment(currentStep);
                                }
                                break;
                            case R.id.menu_next_step:
                                if (currentStep < mRecipe.steps.size() - 1) {
                                    currentStep++;
                                    replaceFragment(currentStep);
                                }
                                break;
                        }
                        return true;
                    }
                }
        );
    }

    private void replaceFragment(int stepId) {

        Bundle arguments = new Bundle();
        arguments.putInt(RECIPE_ID_KEY, mRecipe.getId());
        arguments.putInt(RECIPE_STEP_NO_KEY, stepId);

        RecipeStepDetailFragment recipeDetailFragment = new RecipeStepDetailFragment();
        recipeDetailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, recipeDetailFragment)
                .commit();

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(RECIPE_ID_KEY, mRecipe.getId());
        outState.putInt(RECIPE_STEP_NO_KEY, mStepNo);
        super.onSaveInstanceState(outState);
    }


}
