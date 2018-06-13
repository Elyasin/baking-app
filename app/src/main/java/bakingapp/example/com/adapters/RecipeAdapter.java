package bakingapp.example.com.adapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bakingapp.example.com.MainActivity;
import bakingapp.example.com.R;
import bakingapp.example.com.RecipeStepsListActivity;
import bakingapp.example.com.db.model.RecipeWithRelations;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private List<RecipeWithRelations> mRecipes;

    private final MainActivity mContext;

    public RecipeAdapter(MainActivity parent) {
        mContext = parent;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mRecipeName;
        TextView mServings;

        ViewHolder(View view) {
            super(view);
            this.mRecipeName = view.findViewById(R.id.recipe_name);
            this.mServings = view.findViewById(R.id.servings);
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
    public void onBindViewHolder(@NonNull final RecipeAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.mRecipeName.setText(mRecipes.get(position).getName());
        viewHolder.mServings.setText(String.valueOf(mRecipes.get(position).getServings()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeStepsListActivity.class);
                intent.putExtra(MainActivity.RECIPE_ID_KEY, mRecipes.get(viewHolder.getAdapterPosition()).getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipes(List<RecipeWithRelations> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }
}
