package com.example.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tayyab on 8/2/2017.
 */

public class IngredientData implements Parcelable {

    private String recipeQuantity;
    private String recipeMeasure;
    private String recipeIngredient;

    public IngredientData () {}

    private IngredientData(Parcel in) {
        recipeQuantity = in.readString();
        recipeMeasure = in.readString();
        recipeIngredient = in.readString();
    }

    public static final Creator<IngredientData> CREATOR = new Creator<IngredientData>() {
        @Override
        public IngredientData createFromParcel(Parcel in) {
            return new IngredientData(in);
        }

        @Override
        public IngredientData[] newArray(int size) {
            return new IngredientData[size];
        }
    };

    public String getRecipeQuantity() {
        return recipeQuantity;
    }

    public void setRecipeQuantity(String recipeQuantity) {
        this.recipeQuantity = recipeQuantity;
    }

    public String getRecipeMeasure() {
        return recipeMeasure;
    }

    public void setRecipeMeasure(String recipeMeasure) {
        this.recipeMeasure = recipeMeasure;
    }

    public String getRecipeIngredient() {
        return recipeIngredient;
    }

    public void setRecipeIngredient(String recipeIngredient) {
        this.recipeIngredient = recipeIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(recipeQuantity);
        dest.writeString(recipeMeasure);
        dest.writeString(recipeIngredient);
    }
}
