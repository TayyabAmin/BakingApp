package com.example.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.example.bakingapp.IngredientActivity;
import com.example.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class BackingAppWidget extends AppWidgetProvider {

    public static final String WIDGET_IDS_KEY ="mywidgetproviderwidgetids";
    public static final String WIDGET_DATA_KEY ="mywidgetproviderwidgetdata";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(WIDGET_IDS_KEY)) {
            int[] appWidgetIds = intent.getExtras().getIntArray(WIDGET_IDS_KEY);
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, AppWidgetManager.getInstance(context), appWidgetId);
            }
        } else super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews rv = new RemoteViews(context.getPackageName(),
                R.layout.backing_app_widget);

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        rv.setRemoteAdapter(R.id.app_widget_listview, intent);

        // Set the DetailActivity intent to launch when clicked
        Intent appIntent = new Intent(context, IngredientActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.app_widget_listview, appPendingIntent);
//            rv.setOnClickPendingIntent(R.id.app_widget_listview, appPendingIntent);

//            rv.setOnClickFillInIntent();

        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }
}

