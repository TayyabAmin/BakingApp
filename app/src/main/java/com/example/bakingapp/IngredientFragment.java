package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.DetailIngredientAdapter;
import com.example.bakingapp.databinding.FragmentIngredientsBinding;
import com.example.model.IngredientData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tayyab on 8/8/2017.
 */

public class IngredientFragment extends Fragment {


//    private RecyclerView mIngredientRecyclerView;
//    private HashMap<Integer, ArrayList> ingredientList;
    private ArrayList<IngredientData> mIngredientList;
//    private int recipeId;
    private int clickedIndex;
//    private FragmentIngredientsBinding ingredientsBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentIngredientsBinding ingredientsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ingredients, container, false);

//        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_ingredient_list);

        HashMap<Integer, ArrayList> ingredientList;
        ingredientList = (HashMap) getArguments().getSerializable("ingredient");

//        recipeId = getArguments().getInt("recipe_id");
        clickedIndex = getArguments().getInt("ingredient_key");

        LinearLayoutManager layoutManagerIngredient = new LinearLayoutManager(getContext());
        ingredientsBinding.recipeIngredientList.setLayoutManager(layoutManagerIngredient);
        ingredientsBinding.recipeIngredientList.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(ingredientsBinding.recipeIngredientList.getContext(),
                layoutManagerIngredient.getOrientation());
        ingredientsBinding.recipeIngredientList.addItemDecoration(dividerItemDecoration);

        ingredientList(ingredientList);
        DetailIngredientAdapter adapter = new DetailIngredientAdapter(mIngredientList);
        ingredientsBinding.recipeIngredientList.setAdapter(adapter);

        return ingredientsBinding.getRoot();
    }

    private void ingredientList(HashMap<Integer, ArrayList> ingredientList) {
        for (Map.Entry<Integer, ArrayList> entry : ingredientList.entrySet()) {

            if (entry.getKey() == clickedIndex) {
                mIngredientList = entry.getValue();
            }
        }
    }
}
