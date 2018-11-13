package com.tjohnn.baking.data.repository;

import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.data.source.ResourceApiService;

import java.util.List;

import io.reactivex.Single;

public class RecipeRepository {

    private final ResourceApiService service;

    public RecipeRepository(ResourceApiService service) {
        this.service = service;
    }

    public Single<List<Recipe>> getRecipes(){
        return service.getRecipes();
    }

    public Single<Recipe> getRecipeById(long id){
        return service.getRecipes()
                .toFlowable()
                .flatMapIterable(r -> r)
                .filter(r -> id == r.id)
                .firstElement()
                .toSingle();
    }

}
