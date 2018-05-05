package bakingapp.example.com;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

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

    public static final String RECIPE_INTENT_KEY = "recipient_intent_key";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Recipe mRecipe;

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

        if (findViewById(R.id.instruction_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        if (savedInstanceState == null)
            if (getIntent().hasExtra(RECIPE_INTENT_KEY))
                mRecipe = getIntent().getParcelableExtra(RECIPE_INTENT_KEY);
            else
                Log.e(TAG, "Intent does not have an 'extra'. Recipe object cannot be retrieved.");

        getSupportActionBar().setTitle(mRecipe.getName());

        RecyclerView mRecyclerView = findViewById(R.id.instruction_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setSaveEnabled(true);

        RecipeStepAdapter mRecipeStepAdapter = new RecipeStepAdapter(
                this,
                mRecipe,
                mTwoPane);
        mRecyclerView.setAdapter(mRecipeStepAdapter);
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
