package com.example.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Tayyab on 8/20/2017.
 */

public class BakingContract {

    public static final String AUTHORITY = "com.example.bakingapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_INGREDIENT = "ingredient_list";

    public static final class IngredientEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "ingredient_list";
        public static final String INGREDIENT_QUANTITY = "quantity";
        public static final String INGREDIENT_MEASURE = "measure";
        public static final String INGREDIENT_NAME = "name";
        public static final String RECIPE_ID = "recipe_id";
    }
}
