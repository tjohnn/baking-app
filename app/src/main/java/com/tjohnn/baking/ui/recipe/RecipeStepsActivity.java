package com.tjohnn.baking.ui.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tjohnn.baking.App;
import com.tjohnn.baking.R;
import com.tjohnn.baking.data.dto.Step;
import com.tjohnn.baking.ui.recipes.RecipesActivity;
import com.tjohnn.baking.util.AppScheduler;
import com.tjohnn.baking.util.Provider;
import com.tjohnn.baking.util.SimpleIdlingResource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeStepsActivity extends AppCompatActivity {

    private boolean mTwoPane;
    public static final String RECIPE_ID_KEY = "RECIPE_ID_KEY";

    @BindView(R.id.recipe_step_list)
    RecyclerView recyclerView;

    @BindView(R.id.tv_ingredients)
    TextView ingredientsTextView;

    @BindView(R.id.ingredients_container)
    ScrollView ingredientsContainer;
    private RecipeStepsViewModel mViewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar ;

    Animation slideDown;

    Animation slideUp;
    private long recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (findViewById(R.id.recipe_detail_container) != null) {
            mTwoPane = true;
        }

        recipeId = getIntent().getLongExtra(RECIPE_ID_KEY, 1);

        mViewModel = ViewModelProviders.of(this,
                new RecipeStepsViewModel.ViewModelFactory(App.getInstance(),
                        Provider.getRecipeRepository(), recipeId, AppScheduler.getInstance())).get(RecipeStepsViewModel.class);

        mViewModel.loadRecipeById();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new RecipeStepsAdapter(this, new ArrayList<>(), mTwoPane));

       loadAnims();
    }

    private void loadAnims() {
        slideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        slideDown.setFillAfter(true);
        slideUp.setFillAfter(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mViewModel.getRecipe().observe(this, d -> {
            if(d == null || d.steps == null) return;
            ((RecipeStepsAdapter) recyclerView.getAdapter()).updateItems(d.steps);
            setTitle(d.name);

        });
        mViewModel.getIngredients().observe(this, s -> {
            if(s == null) return;
            ingredientsTextView.setText(s);
        });
        mViewModel.getToaster().observe(this, s -> {
            if(s == null || s.getContentIfNotUsed() == null) return;
            Toast.makeText(this, s.peekContent(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, RecipesActivity.class));
        }
        else if(id == R.id.ingredients){
            if(ingredientsContainer.getVisibility() == View.VISIBLE) {
                ingredientsContainer.startAnimation(slideDown);
                ingredientsContainer.setVisibility(View.INVISIBLE);
            } else {
                ingredientsContainer.startAnimation(slideUp);
                ingredientsContainer.setVisibility(View.VISIBLE);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public long getRecipeId() {
        return recipeId;
    }

    @VisibleForTesting
    SimpleIdlingResource getIdlingResource(){
        return SimpleIdlingResource.getInstance();
    }
}
