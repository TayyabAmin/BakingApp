package com.example.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tayyab on 8/7/2017.
 */

public class DetailActivity extends AppCompatActivity {

    private HashMap<Integer, ArrayList> ingredientList;
    private HashMap<Integer, ArrayList> stepsList;
//    private ArrayList<IngredientData> mIngredientList;
//    private ArrayList<StepsData> mStepsList;
    private int recipeId;
    private int clickedIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ingredientList = new HashMap<>();
        ingredientList = (HashMap<Integer, ArrayList>) getIntent().getSerializableExtra("ingredient");

        recipeId = getIntent().getIntExtra("recipe_id",0);
        clickedIndex = getIntent().getIntExtra("ingredient_key", 0);

        stepsList = new HashMap<>();
        stepsList = (HashMap<Integer, ArrayList>) getIntent().getSerializableExtra("steps");

        if (savedInstanceState == null) {
            addFragment();
            if (getResources().getBoolean(R.bool.isTablet) && (getResources().getConfiguration().orientation == 2)) {
                addIngredientFragment();
            }
        }
    }

    private void addFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new DetailFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("ingredient", ingredientList);
            bundle.putSerializable("steps", stepsList);
            bundle.putInt("recipe_id", recipeId);
            bundle.putInt("ingredient_key", clickedIndex);

            fragment.setArguments(bundle);

            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addIngredientFragment() {
        FragmentManager fm = getSupportFragmentManager();
        IngredientFragment ingredientFragment = new IngredientFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("ingredient", ingredientList);
        bundle.putInt("ingredient_key", clickedIndex);
        ingredientFragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container2, ingredientFragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isFragmentAdded", true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    DetailActivity.this.finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
