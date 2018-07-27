package de.shaladi.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import de.shaladi.bakingapp.db.RecipeDatabase;
import de.shaladi.bakingapp.model.Ingredient;

public class ListRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Ingredient> mIngredients;

    public ListRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredients = RecipeDatabase.getsInstance(mContext).recipeWithRelationsDAO().getIngredientsByRecipeId(1);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d("Elyasin", "Size: " + mIngredients.size());
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.recipe_steps_list_content);
        remoteViews.setTextViewText(R.id.recipe_step_description, mIngredients.get(position).getIngredient());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return mIngredients.get(position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
