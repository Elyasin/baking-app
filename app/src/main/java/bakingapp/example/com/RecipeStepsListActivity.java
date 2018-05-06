package bakingapp.example.com;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.Arrays;

import bakingapp.example.com.adapters.RecipeStepAdapter;
import bakingapp.example.com.model.Recipe;

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

    public static final String RECIPES_ARRAY_KEY = "recipe_array_key";
    public static final String RECIPE_POSITION_KEY = "recipe_position_key";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Recipe[] mRecipeArray;
    private int mRecipePositionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_list);

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

        if (savedInstanceState == null)
            if (getIntent().hasExtra(RECIPES_ARRAY_KEY)) {
                mRecipePositionNumber = getIntent().getIntExtra(RECIPE_POSITION_KEY, 0);
                Parcelable[] parcelables = getIntent().getParcelableArrayExtra(RECIPES_ARRAY_KEY);
                mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
            } else
                Log.e(TAG, "Intent does not have an 'extra'. Recipe object cannot be retrieved.");

        getSupportActionBar().setTitle(mRecipeArray[mRecipePositionNumber].getName());

        RecyclerView mRecyclerView = findViewById(R.id.recipe_steps_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setSaveEnabled(true);

        RecipeStepAdapter mRecipeStepAdapter = new RecipeStepAdapter(
                this,
                mRecipeArray,
                mRecipePositionNumber,
                mTwoPane);
        mRecyclerView.setAdapter(mRecipeStepAdapter);
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
