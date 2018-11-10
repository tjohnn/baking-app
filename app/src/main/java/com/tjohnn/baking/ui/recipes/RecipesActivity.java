package com.tjohnn.baking.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.tjohnn.baking.App;
import com.tjohnn.baking.R;
import com.tjohnn.baking.util.AppScheduler;
import com.tjohnn.baking.util.AutoFitRecyclerView;
import com.tjohnn.baking.util.Provider;
import com.tjohnn.baking.util.SimpleIdlingResource;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesActivity extends AppCompatActivity {

    @BindView(R.id.rv_recipes)
    AutoFitRecyclerView recyclerView;
    private RecipesViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecipesAdapter(this, new ArrayList<>()));

        mViewModel = ViewModelProviders.of(this,
                new RecipesViewModel.ViewModelFactory(App.getInstance(), Provider.getRecipeRepository(), AppScheduler.getInstance())).get(RecipesViewModel.class);

        mViewModel.loadRecipes();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mViewModel.getRecipes().observe(this, d -> {
            if(d == null) return;
            ((RecipesAdapter) recyclerView.getAdapter()).updateItems(d);
        });

        mViewModel.getToaster().observe(this, s -> {
            if(s != null && s.getContentIfNotUsed() != null){
                Toast.makeText(this, s.peekContent(), Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getToaster().observe(this, s -> {
            if(s == null || s.getContentIfNotUsed() == null) return;
            Toast.makeText(this, s.peekContent(), Toast.LENGTH_SHORT).show();
        });
    }


    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        return SimpleIdlingResource.getInstance();
    }
}
