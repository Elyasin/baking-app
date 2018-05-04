package bakingapp.example.com;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import bakingapp.example.com.adapters.InstructionAdapter;
import bakingapp.example.com.model.Recipe;

/**
 * An activity representing a list of Instructions. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link InstructionDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class InstructionListActivity extends AppCompatActivity {

    private static final String TAG = InstructionListActivity.class.getSimpleName();

    public static final String RECIPE_INTENT_KEY = "recipient_intent_key";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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

        Recipe recipe = null;
        if (getIntent().hasExtra(RECIPE_INTENT_KEY))
            recipe = getIntent().getParcelableExtra(RECIPE_INTENT_KEY);
        else
            Log.e(TAG, "Intent does not have an 'extra'. Recipe object cannot be retrieved.");

        RecyclerView mRecyclerView = findViewById(R.id.instruction_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        assert recipe != null;
        InstructionAdapter mInstructionAdapter = new InstructionAdapter(
                this,
                recipe.getSteps(),
                mTwoPane);
        mRecyclerView.setAdapter(mInstructionAdapter);
    }

}
