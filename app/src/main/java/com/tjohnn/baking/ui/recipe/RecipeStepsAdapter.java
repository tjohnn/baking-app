package com.tjohnn.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tjohnn.baking.R;
import com.tjohnn.baking.data.dto.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsAdapter
        extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private final RecipeStepsActivity mParentActivity;
    private List<Step> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            long stepIndex = Long.parseLong(view.getTag().toString());

            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putLong(RecipeStepDetailFragment.ARG_STEP_INDEX, stepIndex);
                arguments.putLong(RecipeStepDetailFragment.ARG_RECIPE_ID, mParentActivity.getRecipeId());
                RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.recipe_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                intent.putExtra(RecipeStepDetailFragment.ARG_STEP_INDEX, stepIndex);
                intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, mParentActivity.getRecipeId());
                context.startActivity(intent);
            }
        }
    };

    public RecipeStepsAdapter(RecipeStepsActivity parent,
                              List<Step> items,
                              boolean twoPane) {
        mValues = items;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe_steps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.descriptionView.setText(mValues.get(position).description);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateItems(List<Step> l) {
        mValues = l;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_description) TextView descriptionView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
