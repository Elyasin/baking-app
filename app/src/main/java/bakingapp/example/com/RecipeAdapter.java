package bakingapp.example.com;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bakingapp.example.com.retrofit.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Recipe[] mRecipes;

    RecipeAdapter() {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mRecipeId;
        TextView mRecipeName;
        TextView mNoIngredients;
        TextView mNoSteps;
        TextView mServings;

        ViewHolder(View view) {
            super(view);
            this.mRecipeId = view.findViewById(R.id.recipe_id);
            this.mRecipeName = view.findViewById(R.id.recipe_name);
            this.mNoIngredients = view.findViewById(R.id.recipe_no_ingredients);
            this.mNoSteps = view.findViewById(R.id.recipe_no_steps);
            this.mServings = view.findViewById(R.id.recipe_servings);
        }
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder viewHolder, int position) {
        viewHolder.mRecipeId.setText(String.valueOf(mRecipes[position].getId()));
        viewHolder.mRecipeName.setText(mRecipes[position].getName());
        viewHolder.mNoIngredients.setText(String.valueOf(mRecipes[position].getIngredients().size()));
        viewHolder.mNoSteps.setText(String.valueOf(mRecipes[position].getSteps().size()));
        viewHolder.mServings.setText(String.valueOf(mRecipes[position].getServings()));
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.length;
    }

    public void swap(Recipe[] recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }
}
