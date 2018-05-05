package bakingapp.example.com;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import bakingapp.example.com.model.Recipe;
import bakingapp.example.com.model.RecipeStep;

import static bakingapp.example.com.RecipeStepDetailFragment.RECIPE_STEP_INTENT_KEY;
import static bakingapp.example.com.RecipeStepsListActivity.RECIPE_INTENT_KEY;

/**
 * An activity representing a single Instruction detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepsListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String TAG = RecipeStepDetailActivity.class.getSimpleName();

    public static final String RECIPE_STEP_NUMBER_INTENT_KEY = "RECIPE_STEP_NUMBER_INTENT_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
            Recipe recipe = getIntent().getParcelableExtra(RECIPE_INTENT_KEY);
            int recipeStepNumber = getIntent().getIntExtra(RECIPE_STEP_NUMBER_INTENT_KEY, 0);
            RecipeStep recipeStep = recipe.getRecipeSteps().get(recipeStepNumber);

            Bundle arguments = new Bundle();
            arguments.putParcelable(RECIPE_STEP_INTENT_KEY, recipeStep);
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.instruction_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
