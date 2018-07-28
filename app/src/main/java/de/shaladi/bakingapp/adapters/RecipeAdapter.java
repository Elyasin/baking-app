package de.shaladi.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import bakingapp.example.com.IngredientsWidgetProvider;
import de.shaladi.bakingapp.R;
import de.shaladi.bakingapp.model.RecipeAndRelations;
import de.shaladi.bakingapp.ui.MainActivity;
import de.shaladi.bakingapp.ui.RecipeStepsListActivity;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private List<RecipeAndRelations> mRecipes;

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
        viewHolder.mRecipeName.setText(mRecipes.get(viewHolder.getAdapterPosition()).getName());
        viewHolder.mServings.setText(String.valueOf(mRecipes.get(viewHolder.getAdapterPosition()).getServings()));
        viewHolder.itemView.setOnClickListener(view -> {
            //Update widgets
            Intent widgetIntent = new Intent(mContext, IngredientsWidgetProvider.class);
            widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            int[] ids = AppWidgetManager.getInstance(mContext).getAppWidgetIds(
                    new ComponentName(mContext, IngredientsWidgetProvider.class)
            );
            widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            //Set data on intent so that app widget is updated, "user extras are ignored"
            widgetIntent.setData(Uri.fromParts("content", String.valueOf(mRecipes.get(viewHolder.getAdapterPosition()).getId()), null));
            mContext.sendBroadcast(widgetIntent);

            //Start activity
            Intent intent = new Intent(mContext, RecipeStepsListActivity.class);
            intent.putExtra(MainActivity.RECIPE_ID_KEY, mRecipes.get(viewHolder.getAdapterPosition()).getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipes(List<RecipeAndRelations> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }
}
