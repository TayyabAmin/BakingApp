package com.example.bakingapp;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adapter.MainAdapter;
import com.example.bakingapp.databinding.FragmentMainBinding;
import com.example.interfaces.ListItemClickListener;
import com.example.model.RecipeData;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tayyab on 7/30/2017.
 */

public class MainFragment extends Fragment implements ListItemClickListener {
//    private RecyclerView mRecyclerView;
//    private ArrayList<RecipeData> arrayList;
    private HashMap ingredientList;
    private HashMap stepsList;
    private ArrayList<Integer> recipeIds;
//    private FragmentMainBinding mainBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Recipes");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentMainBinding mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);

        ArrayList<RecipeData> arrayList;
        arrayList = getArguments().getParcelableArrayList("list");

        ingredientList = new HashMap();
        ingredientList = (HashMap) getArguments().getSerializable("ingredient");

        stepsList = new HashMap();
        stepsList = (HashMap) getArguments().getSerializable("steps");

        recipeIds = new ArrayList<>();
        recipeIds = getArguments().getIntegerArrayList("recipe_ids");

        /*if (getActivity().getResources().getBoolean(R.bool.isTablet)) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
            mRecyclerView.setLayoutManager(layoutManager);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(layoutManager);
        }*/
        if (getActivity().getResources().getConfiguration().orientation == 2) {
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
            mainBinding.mainView.setLayoutManager(layoutManager);
        }else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mainBinding.mainView.setLayoutManager(layoutManager);
        }


        mainBinding.mainView.setHasFixedSize(true);

        MainAdapter adapter = new MainAdapter(arrayList, this, getActivity());
        mainBinding.mainView.setAdapter(adapter);

        return mainBinding.getRoot();
    }

    @Override
    public void OnListItemClickListener(int clickedItemIndex) {
//        DetailFragment detailFragment = new DetailFragment();
//        MediaFragment mediaFragment = new MediaFragment();

//        Bundle bundle = new Bundle();


//        detailFragment.setArguments(bundle);
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).addToBackStack(null).commit();
//        getFragmentManager().beginTransaction().replace(R.id.fragment_container, mediaFragment).addToBackStack(null).commit();

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("ingredient", ingredientList);
        intent.putExtra("steps", stepsList);
        intent.putExtra("ingredient_key", clickedItemIndex);
        intent.putExtra("recipe_id", recipeIds.get(clickedItemIndex));

        startActivity(intent);
    }
}
