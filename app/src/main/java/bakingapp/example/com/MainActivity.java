package bakingapp.example.com;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import bakingapp.example.com.adapters.RecipeAdapter;
import bakingapp.example.com.db.RecipeDatabase;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.retrofit.BakingApiController;

public class MainActivity extends AppCompatActivity
        implements BakingApiController.OnDataLoadedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String RECIPE_ID_KEY = "recipe_id_key";
    public static final String RECIPE_STEP_ID_KEY = "recpie_step_id_key";

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    private ProgressBar mProgressBar;

    private RecipeDatabase mDb;

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

        mDb = RecipeDatabase.getsInstance(getApplicationContext());

        loadRecipes();
    }

    private void loadRecipes() {

        AppExecutors.getsInstance().roomDb().execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Actively retrieving data from database");
                List<Recipe> recipeList = mDb.recipeDAO().loadAllRecipes();
                //Room is empty. Get data from internet
                if (recipeList == null || recipeList.isEmpty()) {
                    Log.d(TAG, "Database empty. Retrieving data from internet");
                    new BakingApiController(MainActivity.this).start();
                } else { //Room has data. Display it.
                    displayRecipes(recipeList);
                }
            }
        });
    }

    public void displayRecipes(List<Recipe> recipeList) {
        mRecipeAdapter.setRecipes(recipeList);
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
