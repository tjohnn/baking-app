package com.tjohnn.baking.data;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {

    private static final String APP_NAME = "RECIPE_APP";
    private static final String DESIRED_RECIPE_ID_KEY = "DESIRED_RECIPE_ID_KEY";
    private static final String DESIRED_RECIPE_INGREDIENTS_KEY = "DESIRED_RECIPE_INGREDIENTS_KEY";

    private SharedPreferences mSharedPreferences;

    public PreferencesHelper(Context context) {
        mSharedPreferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
    }

    public void setDesiredRecipeId(long id){
        mSharedPreferences.edit().putLong(DESIRED_RECIPE_ID_KEY, id).apply();
    }

    public long getDesiredRecipeId(){
        return mSharedPreferences.getLong(DESIRED_RECIPE_ID_KEY, 0);
    }

    public void setDesiredRecipeIngredients(String ingredients){
        mSharedPreferences.edit().putString(DESIRED_RECIPE_INGREDIENTS_KEY, ingredients).apply();
    }

    public String getDesiredRecipeIngredients(){
        return mSharedPreferences.getString(DESIRED_RECIPE_INGREDIENTS_KEY, "");
    }

}
