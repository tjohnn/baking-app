package com.tjohnn.baking.data.source;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.res.AssetManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tjohnn.baking.data.dto.Recipe;
import com.tjohnn.baking.util.DataWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;

public class RecipeDataSource {

    private final Application application;
    private  Gson gson;

    public RecipeDataSource(Application application) {
        this.application = application;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gson = gsonBuilder.create();
    }

    public List<Recipe> getRecipes() throws IOException {

        List<Recipe> recipes;
        AssetManager am = application.getAssets();
        InputStreamReader inputStreamReader = new InputStreamReader(am.open("recipes.json"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder();
        while (( line = bufferedReader.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        Type typeToken = new TypeToken<List<Recipe>>(){}.getType();
        recipes = gson.fromJson(text.toString(), typeToken);

        return recipes;
    }


    public Recipe getRecipeById(long id) throws IOException {

        Recipe recipe = null;
        AssetManager am = application.getAssets();
        InputStreamReader inputStreamReader = new InputStreamReader(am.open("recipes.json"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        StringBuilder text = new StringBuilder();
        while (( line = bufferedReader.readLine()) != null) {
            text.append(line);
            text.append('\n');
        }
        Type typeToken = new TypeToken<List<Recipe>>(){}.getType();
        List<Recipe> recipes = gson.fromJson(text.toString(), typeToken);
        for (Recipe r : recipes) {
            if (r.id == id){
                recipe = r;
                Timber.d(gson.toJson(r));
                break;
            }
        }

        return recipe;
    }
}
