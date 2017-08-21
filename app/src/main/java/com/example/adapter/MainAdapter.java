package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.interfaces.ListItemClickListener;
import com.example.model.RecipeData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tayyab on 7/30/2017.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {

    private final ArrayList<RecipeData> recipeArrayList;
    private final ListItemClickListener mOnClickListener;
    private final Context mContext;
    public MainAdapter(ArrayList<RecipeData> recipeArrayList,
                       ListItemClickListener listener, Context ctx) {
        this.recipeArrayList = recipeArrayList;
        this.mOnClickListener = listener;
        this.mContext = ctx;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_view_items, parent, false);
//        MyViewHolder holder = new MyViewHolder(view);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            holder.mRecipeView.setText(recipeArrayList.get(position).getRecipeName());
            if (!recipeArrayList.get(position).getImageUrl().equals("")) {
                Picasso.with(mContext)
                        .load(recipeArrayList.get(position).getImageUrl())
                        .placeholder(R.drawable.appwidget)
                        .error(R.drawable.appwidget)
                        .into(holder.mRecipeImageView);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return recipeArrayList.size();
    }

    /*public interface ListItemClickListener {
        void OnListItemClickListener (int clickedItemIndex);
    }*/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mRecipeView;
        public final ImageView mRecipeImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mRecipeView = (TextView) itemView.findViewById(R.id.recipe_name_txt);
            mRecipeImageView = (ImageView) itemView.findViewById(R.id.recipe_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.OnListItemClickListener(clickPosition);
        }
    }
}
