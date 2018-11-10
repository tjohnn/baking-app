package com.tjohnn.baking.ui.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tjohnn.baking.R;
import com.tjohnn.baking.util.SimpleIdlingResource;

import timber.log.Timber;


public class RecipeStepDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putLong(RecipeStepDetailFragment.ARG_STEP_INDEX,
                    getIntent().getLongExtra(RecipeStepDetailFragment.ARG_STEP_INDEX, 0));

            arguments.putLong(RecipeStepDetailFragment.ARG_RECIPE_ID,
                    getIntent().getLongExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, 0));
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, RecipeStepsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @VisibleForTesting
    SimpleIdlingResource getIdlingResource(){
        return SimpleIdlingResource.getInstance();
    }
}
