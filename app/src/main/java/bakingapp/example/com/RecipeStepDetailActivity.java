package bakingapp.example.com;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.Arrays;

import bakingapp.example.com.model.Recipe;

import static bakingapp.example.com.RecipeStepsListActivity.RECIPES_ARRAY_KEY;
import static bakingapp.example.com.RecipeStepsListActivity.RECIPE_POSITION_KEY;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    public static final String RECIPE_STEP_POSITION_KEY = "recipe_step_position_key";
    private Recipe[] mRecipeArray;
    private int mRecipePos;
    private int mRecipeStepPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if (findViewById(R.id.recipe_step_detail_container) != null) {

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                Toolbar toolbar = findViewById(R.id.detail_toolbar);
                setSupportActionBar(toolbar);

                // Show the Up button in the action bar.
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }

                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {

                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_previous_step:
                                        if (mRecipeStepPos > 0) {
                                            mRecipeStepPos--;
                                            replaceFragment(mRecipePos, mRecipeStepPos);
                                        }
                                        break;
                                    case R.id.menu_next_step:
                                        if (mRecipeStepPos < mRecipeArray[mRecipePos].getRecipeSteps().size() - 1) {
                                            mRecipeStepPos++;
                                            replaceFragment(mRecipePos, mRecipeStepPos);
                                        }
                                        break;
                                }
                                return true;
                            }
                        }
                );

            }
            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Intent intent = getIntent();
                Parcelable[] parcelables = intent.getParcelableArrayExtra(RECIPES_ARRAY_KEY);
                mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
                mRecipePos = intent.getIntExtra(RECIPE_POSITION_KEY, 0);
                mRecipeStepPos = intent.getIntExtra(RECIPE_STEP_POSITION_KEY, 0);

                replaceFragment(mRecipePos, mRecipeStepPos);
            }

        }

    }

    private void replaceFragment(int recipePos, int recipeStepPos) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArray(RECIPES_ARRAY_KEY, mRecipeArray);
        arguments.putInt(RECIPE_POSITION_KEY, recipePos);
        arguments.putInt(RECIPE_STEP_POSITION_KEY, recipeStepPos);
        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipe_step_detail_container, fragment)
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
}
