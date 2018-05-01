package bakingapp.example.com;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import bakingapp.example.com.retrofit.BakingApiController;
import bakingapp.example.com.retrofit.model.Recipe;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ProgressBar mProgressBar;

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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipeAdapter = new RecipeAdapter();
        mRecyclerView.setAdapter(mRecipeAdapter);

        BakingApiController bakingApiController = new BakingApiController(this);
        bakingApiController.start();

    }


    public void displayText(List<Recipe> recipeList) {
        mRecipeAdapter.swap(recipeList.toArray(new Recipe[recipeList.size()]));
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
