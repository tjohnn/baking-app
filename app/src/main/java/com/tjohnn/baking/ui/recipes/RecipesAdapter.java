package com.tjohnn.baking.ui.recipes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tjohnn.baking.R;
import com.tjohnn.baking.data.dto.Ingredient;
import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.ui.recipe.RecipeStepsActivity;
import com.tjohnn.baking.util.Provider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter  extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    private final RecipesActivity mParentActivity;
    private List<Recipe> mValues;
    private long desciredRecipeId;

    public RecipesAdapter(RecipesActivity parent,
                                     List<Recipe> items) {
        mValues = items;
        mParentActivity = parent;
        desciredRecipeId = Provider.getPreferencesHelper().getDesiredRecipeId();
    }

    @NonNull
    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.nameView.setText(mValues.get(position).name);

        holder.ingredientsView.setText(
                String.format(mParentActivity.getString(R.string.num_of_ingredients_format),
                        mValues.get(position).ingredients.size()));
        holder.servingView.setText(String.format(mParentActivity.getString(R.string.servings_format),
                mValues.get(position).servings));
        if(mValues.get(position).id == desciredRecipeId){
            holder.desiredButton.setImageResource(R.drawable.ic_star_orange_24dp);
        }
        else {
            holder.desiredButton.setImageResource(R.drawable.ic_star_border_orange_24dp);
        }

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mParentActivity, RecipeStepsActivity.class);
            i.putExtra(RecipeStepsActivity.RECIPE_ID_KEY, mValues.get(position).id);
            mParentActivity.startActivity(i);
        });
        holder.desiredButton.setOnClickListener(v -> {
            updateDesiredRecipe(position);
            notifyDataSetChanged();
        });

    }

    private void updateDesiredRecipe(int position) {
        desciredRecipeId = mValues.get(position).id;
        Provider.getPreferencesHelper().setDesiredRecipeId(desciredRecipeId);
        List<Ingredient> ingredients = mValues.get(position).ingredients;
        StringBuilder builder = new StringBuilder();
        for (Ingredient ingredient :
                ingredients) {
            builder.append(String.format("%s %s of %s. \n", ingredient.quantity, ingredient.measure, ingredient.name));
        }
        Provider.getPreferencesHelper().setDesiredRecipeIngredients(builder.toString());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void updateItems(List<Recipe> items) {
        mValues = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name)
        TextView nameView;

        @BindView(R.id.number_of_ingredients)
        TextView ingredientsView;

        @BindView(R.id.servings)
        TextView servingView;

        @BindView(R.id.btn_desired_recipe)
        ImageButton desiredButton;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
