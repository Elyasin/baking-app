package de.shaladi.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.shaladi.bakingapp.R;
import de.shaladi.bakingapp.model.Ingredient;
import de.shaladi.bakingapp.model.IngredientParcelable;
import de.shaladi.bakingapp.model.RecipeAndRelations;
import de.shaladi.bakingapp.model.Step;
import de.shaladi.bakingapp.ui.RecipeStepDetailActivity;
import de.shaladi.bakingapp.ui.RecipeStepDetailFragment;
import de.shaladi.bakingapp.ui.RecipeStepsListActivity;

import static de.shaladi.bakingapp.ui.MainActivity.RECIPE_ID_KEY;
import static de.shaladi.bakingapp.ui.MainActivity.RECIPE_STEP_NO_KEY;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeStepAdapter.class.getSimpleName();

    private static final int INGREDIENTS_VIEW_TYPE = 0;
    private static final int RECIPE_STEP_VIEW_TYPE = 1;

    private RecipeStepsListActivity mParentActivity;

    private final boolean mTwoPane;

    private RecipeAndRelations mRecipe;


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
                view -> {
                    List<Step> steps = mRecipe.getSteps();
                    Step step = steps.get(viewHolder.getAdapterPosition());
                    if (mTwoPane) {

                        Bundle arguments = new Bundle();
                        arguments.putInt(RecipeStepDetailFragment.step_number_key, step.getStepNo());
                        arguments.putString(RecipeStepDetailFragment.step_description_key, step.getDescription());
                        arguments.putParcelableArrayList(
                                RecipeStepDetailFragment.list_of_ingredients_key,
                                IngredientParcelable.makeParcelable(mRecipe.getIngredients())
                        );
                        arguments.putString(
                                RecipeStepDetailFragment.video_url_key,
                                !TextUtils.isEmpty(step.getVideoURL()) ? step.getVideoURL() : step.getThumbnailURL()
                        );

                        RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                        fragment.setArguments(arguments);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.recipe_step_detail_container, fragment)
                                .commit();

                    } else {
                        Context context = view.getContext();
                        Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                        intent.putExtra(RECIPE_ID_KEY, step.getRecipeID());
                        intent.putExtra(RECIPE_STEP_NO_KEY, step.getStepNo());
                        context.startActivity(intent);

                    }
                }
        );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            //TODO Improve layout/design
            case RECIPE_STEP_VIEW_TYPE:
                List<Step> steps = mRecipe.getSteps();
                RecipeStepViewHolder vHolder1 = (RecipeStepViewHolder) holder;
                String shortDesc = steps.get(holder.getAdapterPosition()).getShortDescription();
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(vHolder1.getAdapterPosition()))
                        .append(". ")
                        .append(shortDesc);
                vHolder1.recipeStepTextView.setText(stringBuilder);
                break;
            case INGREDIENTS_VIEW_TYPE:
                List<Ingredient> ingredients = mRecipe.getIngredients();
                RecipeIngredientsViewHolder vHolder2 = (RecipeIngredientsViewHolder) holder;
                stringBuilder = new StringBuilder();
                for (int i = 0; i < ingredients.size(); i++) {
                    stringBuilder.append(ingredients.get(i).getIngredient());
                    if (i < ingredients.size() - 1)
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
        if (mRecipe == null || mRecipe.getSteps() == null) return 0;
        return mRecipe.getSteps().size();
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
        if (mRecipe.getSteps().get(position).getStepNo() == 0)
            return INGREDIENTS_VIEW_TYPE;
        else
            return RECIPE_STEP_VIEW_TYPE;
    }


    public void setRecipe(RecipeAndRelations recipe) {
        this.mRecipe = recipe;
        notifyDataSetChanged();
    }

}
