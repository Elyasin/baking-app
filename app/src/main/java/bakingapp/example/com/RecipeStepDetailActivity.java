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

import static bakingapp.example.com.MainActivity.RECIPES_ARRAY_KEY;
import static bakingapp.example.com.MainActivity.RECIPE_POSITION_KEY;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    private Recipe[] mRecipeArray;
    private int mRecipePositionNumber;
    private int mRecipeStepPositionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                getResources().getConfiguration().smallestScreenWidthDp >= 600) {
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
            if (savedInstanceState == null) {
                // Create the detail fragment and add it to the activity
                // using a fragment transaction.
                Intent intent = getIntent();
                Parcelable[] parcelables = intent.getParcelableArrayExtra(RECIPES_ARRAY_KEY);
                mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
                mRecipePositionNumber = intent.getIntExtra(RECIPE_POSITION_KEY, 0);
                mRecipeStepPositionNumber = intent.getIntExtra(MainActivity.RECIPE_STEP_POSITION_KEY, 0);

                replaceFragment();

            } else {
                Parcelable[] parcelables = savedInstanceState.getParcelableArray(RECIPES_ARRAY_KEY);
                mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
                mRecipePositionNumber = savedInstanceState.getInt(RECIPE_POSITION_KEY);
                mRecipeStepPositionNumber = savedInstanceState.getInt(MainActivity.RECIPE_STEP_POSITION_KEY);
            }

            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                Toolbar toolbar = findViewById(R.id.detail_toolbar);
                setSupportActionBar(toolbar);

                // Show the Up button in the action bar.
                ActionBar actionBar = getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(true);
                }
                getSupportActionBar().setTitle(mRecipeArray[mRecipePositionNumber].getName());


                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setOnNavigationItemSelectedListener(
                        new BottomNavigationView.OnNavigationItemSelectedListener() {

                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_previous_step:
                                        if (mRecipeStepPositionNumber > 0) {
                                            mRecipeStepPositionNumber--;
                                            replaceFragment();
                                        }
                                        break;
                                    case R.id.menu_next_step:
                                        if (mRecipeStepPositionNumber < mRecipeArray[mRecipePositionNumber].getRecipeSteps().size() - 1) {
                                            mRecipeStepPositionNumber++;
                                            replaceFragment();
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

    private void replaceFragment() {
        Bundle arguments = new Bundle();
        arguments.putParcelableArray(RECIPES_ARRAY_KEY, mRecipeArray);
        arguments.putInt(RECIPE_POSITION_KEY, mRecipePositionNumber);
        arguments.putInt(MainActivity.RECIPE_STEP_POSITION_KEY, mRecipeStepPositionNumber);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArray(RECIPES_ARRAY_KEY, mRecipeArray);
        outState.putInt(RECIPE_POSITION_KEY, mRecipePositionNumber);
        outState.putInt(MainActivity.RECIPE_STEP_POSITION_KEY, mRecipeStepPositionNumber);
        super.onSaveInstanceState(outState);
    }

}
