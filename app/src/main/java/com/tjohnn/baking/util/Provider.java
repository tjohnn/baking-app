package com.tjohnn.baking.util;

import android.content.Context;

import com.tjohnn.baking.App;
import com.tjohnn.baking.data.PreferencesHelper;
import com.tjohnn.baking.data.repository.RecipeRepository;
import com.tjohnn.baking.data.source.ApiClient;
import com.tjohnn.baking.data.source.RecipeDataSource;

public class Provider {

    private static RecipeRepository  recipeRepository;
    private static PreferencesHelper preferencesHelper;

    public static RecipeRepository getRecipeRepository() {
        if(recipeRepository == null){
            recipeRepository = new RecipeRepository(ApiClient.getResourceApiService());
        }
        return recipeRepository;
    }


    public static PreferencesHelper getPreferencesHelper() {
        if(preferencesHelper == null){
            preferencesHelper = new PreferencesHelper(App.getInstance());
        }
        return preferencesHelper;
    }

    // for widget in case app is not running
    public static PreferencesHelper getPreferencesHelper(Context context) {
        if(preferencesHelper == null){
            preferencesHelper = new PreferencesHelper(context);
        }
        return preferencesHelper;
    }
}
