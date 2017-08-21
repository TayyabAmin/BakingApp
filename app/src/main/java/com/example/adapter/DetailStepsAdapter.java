package com.example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.interfaces.ListItemClickListener;
import com.example.model.StepsData;

import java.util.ArrayList;

/**
 * Created by Tayyab on 8/3/2017.
 */

public class DetailStepsAdapter  extends RecyclerView.Adapter<DetailStepsAdapter.MyViewHolder> {

    private final ArrayList<StepsData> stepsArrayList;
    private final ListItemClickListener mOnClickListener;
    private final Context mContext;

    public DetailStepsAdapter(Context context, ArrayList<StepsData> stepsArrayList, ListItemClickListener listener) {
        this.stepsArrayList = stepsArrayList;
        mOnClickListener = listener;
        mContext = context;
    }

    @Override
    public DetailStepsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_view, parent, false);

//        MyViewHolder holder = new MyViewHolder(view);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailStepsAdapter.MyViewHolder holder, int position) {

        if (position == 0) {
            holder.mStepsNoView.setText(mContext.getResources().getString(R.string.txt_recipe_ingredients));
            holder.mStepDescView.setText("");
        }else {
            holder.mStepsNoView.setText(mContext.getResources().getString(R.string.txt_steps_) + position);
            holder.mStepDescView.setText(String.valueOf(stepsArrayList.get(position - 1).getShortDesc()));
        }
    }

    @Override
    public int getItemCount() {
        try {
            return stepsArrayList.size() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mStepsNoView, mStepDescView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mStepsNoView = (TextView) itemView.findViewById(R.id.step_no_txt);
            mStepDescView = (TextView) itemView.findViewById(R.id.step_desc_txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickPosition = getAdapterPosition();
            mOnClickListener.OnListItemClickListener(clickPosition);
        }
    }
}
