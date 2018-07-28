package de.shaladi.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;
import java.util.Locale;

import de.shaladi.bakingapp.db.RecipeDatabase;
import de.shaladi.bakingapp.model.Ingredient;

public class ListRemoteViewsService extends RemoteViewsService {

    public static final String TAG = ListRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        //int recipeId = intent.getIntExtra(RECIPE_ID_KEY, 1);
        int recipeId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        return new ListRemoteViewsFactory(this.getApplicationContext(), recipeId);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredients;
    private String mRecipeName;
    private int mRecipeId;

    ListRemoteViewsFactory(Context mContext, int recipeId) {
        this.mContext = mContext;
        this.mRecipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mIngredients = RecipeDatabase.getsInstance(mContext).recipeWithRelationsDAO().getIngredientsByRecipeId(mRecipeId);
        mRecipeName = RecipeDatabase.getsInstance(mContext).recipeWithRelationsDAO().getRecipeName(mRecipeId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (position == 0) {

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_header);
            remoteViews.setTextViewText(R.id.ingredient_title, mRecipeName);

            return remoteViews;

        } else {

            Ingredient ingredient = mIngredients.get(position);
            String ingredientString = ingredient.getIngredient();
            String measure = ingredient.getMeasure();
            float quantity = ingredient.getQuantity();
            String text;
            if (quantity == (long) quantity)
                text = String.format(Locale.ENGLISH, "%d %s of %s", (long) quantity, measure, ingredientString);
            else
                text = String.format(Locale.ENGLISH, "%s %s of %s", quantity, measure, ingredientString);

            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_widget_list_item);
            remoteViews.setTextViewText(R.id.ingredient, text);

            return remoteViews;

        }

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
