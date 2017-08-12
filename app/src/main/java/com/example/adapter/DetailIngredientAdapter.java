package com.example.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.model.IngredientData;

import java.util.ArrayList;

/**
 * Created by Tayyab on 8/3/2017.
 */

public class DetailIngredientAdapter extends RecyclerView.Adapter<DetailIngredientAdapter.MyViewHolder> {

    private final ArrayList<IngredientData> ingredientArrayList;
//    private ArrayList<IngredientData> data;

    public DetailIngredientAdapter(ArrayList<IngredientData> ingredientArrayList) {
        this.ingredientArrayList = ingredientArrayList;
    }

    @Override
    public DetailIngredientAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_view, parent, false);

//        MyViewHolder holder = new MyViewHolder(view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailIngredientAdapter.MyViewHolder holder, int position) {

        holder.mIngredientView.setText(ingredientArrayList.get(position).getRecipeIngredient());
        holder.mQuantityView.setText(ingredientArrayList.get(position).getRecipeQuantity());
        holder.mMeasureView.setText(ingredientArrayList.get(position).getRecipeMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView mIngredientView, mQuantityView, mMeasureView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mIngredientView = (TextView) itemView.findViewById(R.id.ingredient_name);
            mQuantityView = (TextView) itemView.findViewById(R.id.ingredient_quantity);
            mMeasureView = (TextView) itemView.findViewById(R.id.ingredient_measure);
        }
    }
}
