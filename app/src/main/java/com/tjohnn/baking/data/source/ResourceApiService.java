package com.tjohnn.baking.data.source;

import com.tjohnn.baking.data.dto.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ResourceApiService {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Single<List<Recipe>> getRecipes();

}
