package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tayyab on 8/1/2017.
 */

public class RecipeData implements Parcelable {

    private int recipeId;
    private String recipeName;
    private int recipeServing;
    private String imageUrl;

    public RecipeData() {}

    private RecipeData(Parcel in) {
        recipeId = in.readInt();
        recipeName = in.readString();
        recipeServing = in.readInt();
        imageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(recipeId);
        dest.writeString(recipeName);
        dest.writeInt(recipeServing);
        dest.writeString(imageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeData> CREATOR = new Creator<RecipeData>() {
        @Override
        public RecipeData createFromParcel(Parcel in) {
            return new RecipeData(in);
        }

        @Override
        public RecipeData[] newArray(int size) {
            return new RecipeData[size];
        }
    };

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getRecipeServing() {
        return recipeServing;
    }

    public void setRecipeServing(int recipeServing) {
        this.recipeServing = recipeServing;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
