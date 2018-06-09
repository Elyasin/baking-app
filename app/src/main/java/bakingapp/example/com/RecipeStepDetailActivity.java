package bakingapp.example.com;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.db.model.Step;

import static bakingapp.example.com.MainActivity.RECIPE_ID_KEY;
import static bakingapp.example.com.MainActivity.RECIPE_STEP_ID_KEY;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    private Recipe mRecipe;
    private Step mStep;

    private RecipeDatabase mDb;

    private RecipeStepDetailFragment mRecipeDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        //Activity never used in tablet when in landscape mode.
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            finish();
            return;
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {

            mDb = RecipeDatabase.getsInstance(getApplicationContext());

            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            final int maxSteps, recipeId, stepId;
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Intent intent = getIntent();

                recipeId = intent.getIntExtra(RECIPE_ID_KEY, 0);
                stepId = intent.getIntExtra(RECIPE_STEP_ID_KEY, 0);
                maxSteps = loadData(recipeId, stepId);

                replaceFragment(mStep.getStepId());

            } else {
                recipeId = savedInstanceState.getInt(RECIPE_ID_KEY);
                stepId = savedInstanceState.getInt(RECIPE_STEP_ID_KEY);
                maxSteps = loadData(recipeId, stepId);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

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

                            int currentStep = mStep.getStepId();

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
                                        if (currentStep < maxSteps - 1) {
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
        }
    }

    private void replaceFragment(int stepId) {
        Bundle arguments = new Bundle();
        arguments.putInt(RECIPE_ID_KEY, mRecipe.getId());
        arguments.putInt(RECIPE_STEP_ID_KEY, stepId);
        mRecipeDetailFragment = new RecipeStepDetailFragment();
        mRecipeDetailFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, mRecipeDetailFragment)
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
        outState.putInt(RECIPE_STEP_ID_KEY, mStep.getStepId());
        super.onSaveInstanceState(outState);
    }


    private int loadData(int recipeId, int stepId) {
        mRecipe = mDb.recipeDAO().loadRecipe(recipeId);
        mStep = mDb.stepDAO().loadStep(recipeId, stepId);
        return mDb.stepDAO().loadNoOfStepsOfRecipe(recipeId);
    }

}
