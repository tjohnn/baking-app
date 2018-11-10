package com.tjohnn.baking.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable {

    public long id;

    public String name;

    public int servings;

    public List<Ingredient> ingredients;

    public List<Step> steps;


    protected Recipe(Parcel in) {
        id = in.readLong();
        name = in.readString();
        servings = in.readInt();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", servings=" + servings +
                ", ingredients=" + ingredients +
                '}';
    }
}
