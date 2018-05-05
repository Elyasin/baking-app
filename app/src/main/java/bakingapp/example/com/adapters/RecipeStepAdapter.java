package bakingapp.example.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bakingapp.example.com.R;
import bakingapp.example.com.RecipeStepDetailActivity;
import bakingapp.example.com.RecipeStepDetailFragment;
import bakingapp.example.com.RecipeStepsListActivity;
import bakingapp.example.com.model.Recipe;
import bakingapp.example.com.model.RecipeStep;

import static bakingapp.example.com.RecipeStepDetailActivity.RECIPE_STEP_NUMBER_INTENT_KEY;
import static bakingapp.example.com.RecipeStepDetailFragment.RECIPE_STEP_INTENT_KEY;
import static bakingapp.example.com.RecipeStepsListActivity.RECIPE_INTENT_KEY;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.ViewHolder> {

    private static final String TAG = RecipeStepAdapter.class.getSimpleName();

    private final RecipeStepsListActivity mParentActivity;

    private final Recipe mRecipe;

    private final boolean mTwoPane;

    public RecipeStepAdapter(RecipeStepsListActivity parent,
                             Recipe recipe,
                             boolean twoPane) {
        mRecipe = recipe;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_steps_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mIdView.setText(String.valueOf(mRecipe.getRecipeSteps().get(position).getId()));
        holder.mContentView.setText(mRecipe.getRecipeSteps().get(position).getDescription());

        holder.itemView.setTag(mRecipe.getRecipeSteps().get(position));
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTwoPane) {
                            RecipeStep recipeStep = (RecipeStep) view.getTag();
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(RECIPE_STEP_INTENT_KEY, recipeStep);
                            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                            fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.instruction_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                            intent.putExtra(RECIPE_INTENT_KEY, mRecipe);
                            intent.putExtra(RECIPE_STEP_NUMBER_INTENT_KEY, holder.getAdapterPosition());
                            context.startActivity(intent);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return mRecipe.getRecipeSteps().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mIdView = view.findViewById(R.id.id_text);
            mContentView = view.findViewById(R.id.content);
        }

    }
}
