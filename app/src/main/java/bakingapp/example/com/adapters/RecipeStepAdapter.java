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

import java.util.List;

import bakingapp.example.com.R;
import bakingapp.example.com.RecipeStepDetailActivity;
import bakingapp.example.com.RecipeStepDetailFragment;
import bakingapp.example.com.RecipeStepsListActivity;
import bakingapp.example.com.db.model.Ingredient;
import bakingapp.example.com.db.model.Recipe;
import bakingapp.example.com.db.model.Step;

import static bakingapp.example.com.MainActivity.RECIPE_ID_KEY;
import static bakingapp.example.com.MainActivity.RECIPE_STEP_ID_KEY;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeStepAdapter.class.getSimpleName();

    private static final int INGREDIENTS_VIEW_TYPE = 0;
    private static final int RECIPE_STEP_VIEW_TYPE = 1;

    private RecipeStepsListActivity mParentActivity;

    private Recipe mRecipe;
    private List<Step> mSteps;
    private List<Ingredient> mIngredients;

    private final boolean mTwoPane;


    public RecipeStepAdapter(RecipeStepsListActivity parent,
                             boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case INGREDIENTS_VIEW_TYPE:
                viewHolder = new RecipeIngredientsViewHolder(
                        layoutInflater.inflate(
                                R.layout.recipe_steps_list_ingredients,
                                parent,
                                false)
                );
                break;
            case RECIPE_STEP_VIEW_TYPE:
                viewHolder = new RecipeStepViewHolder(
                        layoutInflater.inflate(
                                R.layout.recipe_steps_list_content,
                                parent,
                                false)
                );
                break;
            default:
                throw new IllegalArgumentException("The view type is not supported: " + viewType);
        }

        //Click on item -> display the item
        //2 pane mode uses fragment
        //1 pane mode launches activity
        viewHolder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTwoPane) {
                            //RecipeStep recipeStep = (RecipeStep) view.getTag();
                            Bundle arguments = new Bundle();
                            arguments.putInt(RECIPE_ID_KEY, mRecipe.getId());
                            arguments.putInt(RECIPE_STEP_ID_KEY, mSteps.get(viewHolder.getAdapterPosition()).getStepId());
                            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                            fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipe_step_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                            intent.putExtra(RECIPE_ID_KEY, mRecipe.getId());
                            intent.putExtra(RECIPE_STEP_ID_KEY, mSteps.get(viewHolder.getAdapterPosition()).getStepId());
                            context.startActivity(intent);
                        }
                    }
                }
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case RECIPE_STEP_VIEW_TYPE:
                RecipeStepViewHolder vHolder1 = (RecipeStepViewHolder) holder;
                String shortDesc = mSteps.get(position).getShortDescription();
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(vHolder1.getAdapterPosition()))
                        .append(". ")
                        .append(shortDesc);
                vHolder1.recipeStepTextView.setText(stringBuilder);
                break;
            case INGREDIENTS_VIEW_TYPE:
                RecipeIngredientsViewHolder vHolder2 = (RecipeIngredientsViewHolder) holder;
                stringBuilder = new StringBuilder();
                for (int i = 0; i < mIngredients.size(); i++) {
                    stringBuilder.append(mIngredients.get(i).getIngredient());
                    if (i < mIngredients.size() - 1)
                        stringBuilder.append(", ");
                }
                vHolder2.recipeStepIngredients.setText(stringBuilder);
        }

        // when in 2 pane mode display first item by default (ingredients)
        if (position == 0 && mTwoPane) {
            holder.itemView.performClick();
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 0;
        return mSteps.size();
    }

    static class RecipeStepViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeStepTextView;

        RecipeStepViewHolder(View view) {
            super(view);
            recipeStepTextView = view.findViewById(R.id.recipe_step_description);
        }

    }

    class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {
        final TextView recipeStepIngredients;

        RecipeIngredientsViewHolder(View view) {
            super(view);
            recipeStepIngredients = view.findViewById(R.id.recipe_step_ingredients);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mSteps.get(position).getStepId() == 0)
            return INGREDIENTS_VIEW_TYPE;
        else
            return RECIPE_STEP_VIEW_TYPE;
    }

    public void setSteps(Recipe recipe, List<Step> steps, List<Ingredient> ingredients) {
        this.mRecipe = recipe;
        this.mSteps = steps;
        this.mIngredients = ingredients;
        notifyDataSetChanged();
    }
}
