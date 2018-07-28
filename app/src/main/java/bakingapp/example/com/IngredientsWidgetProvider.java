package bakingapp.example.com;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import de.shaladi.bakingapp.ListRemoteViewsService;
import de.shaladi.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    public final static String TAG = IngredientsWidgetProvider.class.getSimpleName();

    private int mRecipeId = -1;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getData() != null)
            mRecipeId = Integer.valueOf(intent.getData().getSchemeSpecificPart());
        super.onReceive(context, intent);
    }

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews remoteViews = getRecipeListRemoteView(context, recipeId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    private static RemoteViews getRecipeListRemoteView(Context context, int recipeId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget_list_view);
        Intent intent = new Intent(context, ListRemoteViewsService.class);
        intent.setData(Uri.fromParts("content", String.valueOf(recipeId), null));
        remoteViews.setRemoteAdapter(R.id.ingredients_widget_list_view, intent);
        remoteViews.setEmptyView(R.id.ingredients_widget_list_view, R.id.empty_view);
        return remoteViews;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context.getApplicationContext(), appWidgetManager, appWidgetId, mRecipeId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

