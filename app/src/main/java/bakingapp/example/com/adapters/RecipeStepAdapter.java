package bakingapp.example.com.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bakingapp.example.com.R;
import bakingapp.example.com.RecipeStepDetailActivity;
import bakingapp.example.com.RecipeStepDetailFragment;
import bakingapp.example.com.RecipeStepsListActivity;
import bakingapp.example.com.model.Ingredient;
import bakingapp.example.com.model.Recipe;

import static bakingapp.example.com.MainActivity.RECIPE_STEP_POSITION_KEY;
import static bakingapp.example.com.MainActivity.RECIPES_ARRAY_KEY;
import static bakingapp.example.com.MainActivity.RECIPE_POSITION_KEY;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeStepAdapter.class.getSimpleName();

    private static final int INGREDIENTS_VIEW_TYPE = 0;
    private static final int RECIPE_STEP_VIEW_TYPE = 1;

    private final RecipeStepsListActivity mParentActivity;

    private final Recipe[] mRecipeArray;
    private final int mRecipePositionNumber;

    private final boolean mTwoPane;

    public RecipeStepAdapter(RecipeStepsListActivity parent,
                             Recipe[] recipeArray,
                             int position,
                             boolean twoPane) {
        mRecipeArray = recipeArray;
        mParentActivity = parent;
        mRecipePositionNumber = position;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
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
                Log.e(TAG, "View type is not supported: " + viewType);
                throw new IllegalArgumentException("The view type is not supported: " + viewType);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case RECIPE_STEP_VIEW_TYPE:
                RecipeStepViewHolder vHolder1 = (RecipeStepViewHolder) holder;
                String shortDesc = mRecipeArray[mRecipePositionNumber].getRecipeSteps().get(position).getShortDescription();
                StringBuilder stringBuilder = new StringBuilder(String.valueOf(vHolder1.getAdapterPosition()))
                        .append(". ")
                        .append(shortDesc);
                vHolder1.recipeStepTextView.setText(stringBuilder);
                break;
            case INGREDIENTS_VIEW_TYPE:
                RecipeIngredientsViewHolder vHolder2 = (RecipeIngredientsViewHolder) holder;
                List<Ingredient> ingredientList = mRecipeArray[mRecipePositionNumber].getIngredients();
                stringBuilder = new StringBuilder();
                for (int i = 0; i < ingredientList.size(); i++) {
                    stringBuilder.append(ingredientList.get(i).getIngredient());
                    if (i < ingredientList.size() - 1)
                        stringBuilder.append(", ");
                }
                vHolder2.recipeStepIngredients.setText(stringBuilder);
        }
        //holder.itemView.setTag(mRecipeArray[mRecipePositionNumber].getRecipeSteps().get(position));
        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mTwoPane) {
                            //RecipeStep recipeStep = (RecipeStep) view.getTag();
                            Bundle arguments = new Bundle();
                            arguments.putParcelableArray(RECIPES_ARRAY_KEY, mRecipeArray);
                            arguments.putInt(RECIPE_POSITION_KEY, mRecipePositionNumber);
                            arguments.putInt(RECIPE_STEP_POSITION_KEY, holder.getAdapterPosition());
                            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                            fragment.setArguments(arguments);
                            mParentActivity.getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.recipe_step_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = view.getContext();
                            Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                            intent.putExtra(RECIPES_ARRAY_KEY, mRecipeArray);
                            intent.putExtra(RECIPE_POSITION_KEY, mRecipePositionNumber);
                            intent.putExtra(RECIPE_STEP_POSITION_KEY, holder.getAdapterPosition());
                            context.startActivity(intent);
                        }
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return mRecipeArray[mRecipePositionNumber].getRecipeSteps().size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder {
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
        if (mRecipeArray[mRecipePositionNumber].getRecipeSteps().get(position).getId() == 0)
            return INGREDIENTS_VIEW_TYPE;
        else
            return RECIPE_STEP_VIEW_TYPE;
    }
}
