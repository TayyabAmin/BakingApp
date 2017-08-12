package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.DetailStepsAdapter;
import com.example.bakingapp.databinding.FragmentDetailBinding;
import com.example.interfaces.ListItemClickListener;
import com.example.model.StepsData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tayyab on 8/3/2017.
 */

public class DetailFragment extends Fragment implements ListItemClickListener {

//    private RecyclerView mRecyclerView;
//    private RecyclerView mIngredientRecyclerView;
    private HashMap<Integer, ArrayList> ingredientList;
//    private HashMap<Integer, ArrayList> stepsList;
//    private ArrayList<IngredientData> mIngredientList;
    private ArrayList<StepsData> mStepsList;
//    private int recipeId;
    private int clickedIndex;
//    private FragmentDetailBinding detailBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Recipe Details");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentDetailBinding detailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);

//        mIngredientRecyclerView = (RecyclerView) rootView.findViewById(R.id.recipe_ingredient_list);
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_view);

        ingredientList = new HashMap<>();
        ingredientList = (HashMap) getArguments().getSerializable("ingredient");

//        int recipeId = getArguments().getInt("recipe_id");
        clickedIndex = getArguments().getInt("ingredient_key");

        HashMap<Integer, ArrayList> stepsList;
        stepsList = (HashMap) getArguments().getSerializable("steps");

        /*LinearLayoutManager layoutManagerIngredient = new LinearLayoutManager(getContext());
        mIngredientRecyclerView.setLayoutManager(layoutManagerIngredient);
        mIngredientRecyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mIngredientRecyclerView.getContext(),
                layoutManagerIngredient.getOrientation());
        mIngredientRecyclerView.addItemDecoration(dividerItemDecoration);*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        detailBinding.detailView.setLayoutManager(layoutManager);
        detailBinding.detailView.setHasFixedSize(true);

        /*ingredientList(ingredientList);
        DetailIngredientAdapter adapter = new DetailIngredientAdapter(mIngredientList);
        mIngredientRecyclerView.setAdapter(adapter);*/

        stepsList(stepsList);
        DetailStepsAdapter stepsAdapter = new DetailStepsAdapter(getActivity(), mStepsList, this);
        detailBinding.detailView.setAdapter(stepsAdapter);
        return detailBinding.getRoot();
    }

    /*public void ingredientList(HashMap<Integer, ArrayList> ingredientList) {
        for (Map.Entry<Integer, ArrayList> entry : ingredientList.entrySet()) {

            if (entry.getKey() == clickedIndex) {
                mIngredientList = entry.getValue();
            }
        }
    }*/

    private void stepsList(HashMap<Integer, ArrayList> stepsList) {
        for (Map.Entry<Integer, ArrayList> entry : stepsList.entrySet()) {

            if (entry.getKey() == clickedIndex) {
                mStepsList = entry.getValue();
            }
        }
    }

    @Override
    public void OnListItemClickListener(int clickedItemIndex) {
        IngredientFragment ingredientFragment = new IngredientFragment();
        MediaFragment mediaFragment = new MediaFragment();

        Bundle bundle = new Bundle();

        if (clickedItemIndex == 0) {
            bundle.putSerializable("ingredient", ingredientList);
            bundle.putInt("ingredient_key", clickedIndex);
            ingredientFragment.setArguments(bundle);
            if (getResources().getBoolean(R.bool.isTablet) && (getResources().getConfiguration().orientation == 2)) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container2, ingredientFragment).addToBackStack(null).commit();
            }else {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, ingredientFragment).addToBackStack(null).commit();
            }
        }else {
            bundle.putParcelableArrayList("step_data", mStepsList);
            bundle.putInt("position", clickedItemIndex - 1);
            mediaFragment.setArguments(bundle);
            if (getResources().getBoolean(R.bool.isTablet) && (getResources().getConfiguration().orientation == 2)) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container2, mediaFragment).addToBackStack(null).commit();
            } else {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, mediaFragment).addToBackStack(null).commit();
            }
        }
    }
}
