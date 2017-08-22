package com.example.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingapp.R;
import com.example.data.BakingContract;
import com.example.model.IngredientData;

import java.util.ArrayList;

import static com.example.bakingapp.R.id.ingredient_name;

/**
 * Created by Tayyab on 8/19/2017.
 */

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context mContext;
    private final int appWidgetId;
    private final SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "last_seen_ingredient";
    private ArrayList<IngredientData> mIngredientList;
    private int clickedIndex = 0;

    public WidgetRemoteViewsFactory(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d("AppWidgetId", String.valueOf(appWidgetId));
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        clickedIndex = sharedPreferences.getInt("last_seen", 0);
        fetchIngredients();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        AppWidgetManager.getInstance(mContext).notifyAppWidgetViewDataChanged(appWidgetId, ingredient_name);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return sharedPreferences.getInt("recipe_size", 9);
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("WidgetCreatingView", "WidgetCreatingView");
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),
                R.layout.ingredient_view);

        remoteView.setTextViewText(ingredient_name, mIngredientList.get(position).getRecipeIngredient());
        remoteView.setTextViewText(R.id.ingredient_quantity, mIngredientList.get(position).getRecipeQuantity());
        remoteView.setTextViewText(R.id.ingredient_measure, mIngredientList.get(position).getRecipeMeasure());

        // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
        Bundle extras = new Bundle();
        extras.putInt("ingredient_key", position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.widget_txt, fillInIntent);

        return remoteView;
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
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //Load data from database
    private void fetchIngredients() {
        String selection = "recipe_id=?";
        String[] selectionArgs = new String[]{String.valueOf(clickedIndex + 1)};
        Cursor data = mContext.getContentResolver().query(
                BakingContract.IngredientEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
        fetchIngredientList(data);
    }

    private void fetchIngredientList(Cursor mCursor) {
        mIngredientList = new ArrayList<IngredientData>();
        if (mCursor.moveToFirst()) {
            do {
                addValues(mCursor.getString(1), mCursor.getString(2), mCursor.getString(3));
            } while (mCursor.moveToNext());
        }
        if (!mCursor.isClosed()) {
            mCursor.close();
        }
    }

    private void addValues(String quantity, String measure, String name) {
        IngredientData data = new IngredientData();

        data.setRecipeQuantity(quantity);
        data.setRecipeMeasure(measure);
        data.setRecipeIngredient(name);

        mIngredientList.add(data);
    }
}
