package bakingapp.example.com;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import bakingapp.example.com.model.Ingredient;
import bakingapp.example.com.model.Recipe;
import bakingapp.example.com.model.RecipeStep;

import static bakingapp.example.com.RecipeStepDetailActivity.RECIPE_STEP_POSITION_KEY;
import static bakingapp.example.com.RecipeStepsListActivity.RECIPES_ARRAY_KEY;
import static bakingapp.example.com.RecipeStepsListActivity.RECIPE_POSITION_KEY;

/**
 * A fragment representing a single Instruction detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {

    private Recipe[] mRecipeArray;
    private int mRecipePosition;
    private int mRecipeStepPosition;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null &&
                bundle.containsKey(RECIPES_ARRAY_KEY) &&
                bundle.containsKey(RECIPE_POSITION_KEY) &&
                bundle.containsKey(RECIPE_STEP_POSITION_KEY)) {

            Parcelable[] parcelables = bundle.getParcelableArray(RECIPES_ARRAY_KEY);
            mRecipeArray = Arrays.copyOf(parcelables, parcelables.length, Recipe[].class);
            mRecipePosition = bundle.getInt(RECIPE_POSITION_KEY);
            Recipe recipe = mRecipeArray[mRecipePosition];
            mRecipeStepPosition = bundle.getInt(RECIPE_STEP_POSITION_KEY);
            RecipeStep recipeStep = recipe.getRecipeSteps().get(mRecipeStepPosition);

            AppCompatActivity activity = (AppCompatActivity) this.getActivity();
            Toolbar toolbar = activity.findViewById(R.id.detail_toolbar);
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setTitle(mRecipeArray[mRecipePosition].getName());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);

        if (mRecipeStepPosition == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            List<Ingredient> ingredientList = mRecipeArray[mRecipePosition].getIngredients();
            for (int i = 0; i < ingredientList.size(); i++) {
                String ingredient = ingredientList.get(i).getIngredient();
                String measure = ingredientList.get(i).getMeasure();
                float quantity = ingredientList.get(i).getQuantity();
                String text;
                if (quantity == (long) quantity)
                    text = String.format("%d %s of %s", (long) quantity, measure, ingredient);
                else
                    text = String.format("%s %s of %s", quantity, measure, ingredient);
                ((TextView) rootView
                        .findViewById(R.id.recipe_step_detail))
                        .append(text + "\n");
            }
        } else {
            RecipeStep recipeStep = mRecipeArray[mRecipePosition].getRecipeSteps().get(mRecipeStepPosition);
            if (recipeStep != null) {
                ((TextView) rootView.findViewById(R.id.recipe_step_detail)).setText(recipeStep.getDescription());
            }
        }

        return rootView;
    }
}
