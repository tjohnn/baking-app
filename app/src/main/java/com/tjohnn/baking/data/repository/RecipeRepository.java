package com.tjohnn.baking.data.repository;

import android.arch.lifecycle.LiveData;

import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.data.source.RecipeDataSource;
import com.tjohnn.baking.util.DataWrapper;

import java.util.List;

import io.reactivex.Single;

public class RecipeRepository {

    private final RecipeDataSource recipeDataSource;

    public RecipeRepository(RecipeDataSource recipeDataSource) {
        this.recipeDataSource = recipeDataSource;
    }

    public Single<List<Recipe>> getRecipes(){
        return Single.fromCallable(recipeDataSource::getRecipes);
    }

    public Single<Recipe> getRecipeById(long id){
        return Single.fromCallable(() -> recipeDataSource.getRecipeById(id));
    }

}
