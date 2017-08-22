package com.example.bakingapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.adapter.DetailIngredientAdapter;
import com.example.data.BakingContract;
import com.example.model.IngredientData;

import java.util.ArrayList;

/**
 * Created by Tayyab on 8/19/2017.
 */

public class IngredientActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private RecyclerView recyclerView;
    private ArrayList<IngredientData> mIngredientList;
    private int clickedIndex;
    private static final int LOADER_ID = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ingredients);

        recyclerView = (RecyclerView) findViewById(R.id.recipe_ingredient_list);

//        HashMap<Integer, ArrayList> ingredientList;
//        ingredientList = MainActivity.ingredientHashMap;

//        recipeId = getArguments().getInt("recipe_id");
        clickedIndex = getIntent().getIntExtra("ingredient_key", 0);

        fetchIngredients();

        LinearLayoutManager layoutManagerIngredient = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManagerIngredient);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(),
                        layoutManagerIngredient.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
//
//        ingredientList(MainActivity.ingredientHashMap);
//        DetailIngredientAdapter adapter = new DetailIngredientAdapter(mIngredientList);
//        recyclerView.setAdapter(adapter);

    }

    /*private void ingredientList(HashMap<Integer, ArrayList> ingredientList) {
        for (Map.Entry<Integer, ArrayList> entry : ingredientList.entrySet()) {

            if (entry.getKey() == clickedIndex) {
                mIngredientList = entry.getValue();
            }
        }
    }*/

    private void fetchIngredients() {

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Integer> ingredientLoader = loaderManager.getLoader(LOADER_ID);
        if (ingredientLoader == null) {
            loaderManager.initLoader(LOADER_ID, null, this);
        }else {
            loaderManager.restartLoader(LOADER_ID, null, this);
        }

        /*try {
            String selection = "recipe_id=?";
            String[] selectionArgs = new String[]{String.valueOf(recipe_id)};
            Cursor cursor = getContentResolver().query(BakingContract.IngredientEntry.CONTENT_URI,
                    null,
                    selection,
                    selectionArgs,
                    null);

            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }*/
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection = "recipe_id=?";
        String[] selectionArgs = new String[]{String.valueOf(clickedIndex + 1)};
        return new CursorLoader(IngredientActivity.this,
                BakingContract.IngredientEntry.CONTENT_URI,
                null,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        fetchIngredientList(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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

        DetailIngredientAdapter adapter = new DetailIngredientAdapter(mIngredientList);
        recyclerView.setAdapter(adapter);
    }

    private void addValues(String quantity, String measure, String name) {
        IngredientData data = new IngredientData();

        data.setRecipeQuantity(quantity);
        data.setRecipeMeasure(measure);
        data.setRecipeIngredient(name);

        mIngredientList.add(data);
    }
}