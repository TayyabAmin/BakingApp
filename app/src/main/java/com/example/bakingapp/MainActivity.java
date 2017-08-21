package com.example.bakingapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.data.BakingContract;
import com.example.model.IngredientData;
import com.example.model.RecipeData;
import com.example.model.StepsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private final String URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private static final String LIFECYCLE_RECIPES = "callback_recipes";
    private static final String LIFECYCLE_INGREDIENT = "callback_ingredientHashMap";
    private static final String LIFECYCLE_STEPS = "callback_stepsHashMap";
    private static final String LIFECYCLE_RECIPE_IDS = "callback_recipeIds";
    private static final String LIFECYCLE_FRAGMENT = "callback_fragment";
    private ArrayList<RecipeData> recipeArrayList;
    private ArrayList<IngredientData> ingredientArrayList;
    private HashMap<Integer, ArrayList> ingredientHashMap;
    private ArrayList<StepsData> stepsArrayList;
    private HashMap<Integer, ArrayList> stepsHashMap;
    private ArrayList<Integer> recipeIds;
    private ProgressDialog progressDialog;
    private boolean isFragmentAdded = false;
//    private ActivityMainBinding mainBinding;

    private static final String MyPREFERENCES = "RecipeList";
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (savedInstanceState != null) {
            recipeArrayList = savedInstanceState.getParcelableArrayList(LIFECYCLE_RECIPES);
            ingredientHashMap = (HashMap<Integer, ArrayList>)
                    savedInstanceState.getSerializable(LIFECYCLE_INGREDIENT);
            stepsHashMap = (HashMap<Integer, ArrayList>)
                    savedInstanceState.getSerializable(LIFECYCLE_STEPS);
            recipeIds = savedInstanceState.getIntegerArrayList(LIFECYCLE_RECIPE_IDS);
            isFragmentAdded = savedInstanceState.getBoolean(LIFECYCLE_FRAGMENT);

            if (recipeArrayList !=null && ingredientArrayList !=null
                    && stepsHashMap !=null && recipeIds !=null) {
                if (!isFragmentAdded) {
                    addFragment();
                }
            }else {
//                Toast.makeText(this, "error rendering detail", Toast.LENGTH_SHORT).show();
                volleyJsonArrayRequest(URL);
            }
        }else {
            volleyJsonArrayRequest(URL);
        }
    }

    private void volleyJsonArrayRequest(String url){
        try {
            final String TAG = "Json_Response";
            String  REQUEST_TAG = "com.example.bakingapp";
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            JsonArrayRequest jsonArrayReq = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            Log.d(TAG, response.toString());

                            parseJson(response);
                            if (!isFragmentAdded) {
                                addFragment();
                            }
                            progressDialog.hide();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    progressDialog.hide();
                }
            });

            // Adding JsonObject request to request queue
            AppSinglton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayReq, REQUEST_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseJson(JSONArray response) {
        try {
            ingredientHashMap = new HashMap<>();
            stepsHashMap = new HashMap<>();
            recipeArrayList = new ArrayList<>();
            recipeIds = new ArrayList<>();
            boolean isDataExist = checkDataExist();
            for (int i=0; i<response.length(); i++) {
                JSONObject jsonObject = response.getJSONObject(i);
                RecipeData recipeData = new RecipeData();
                recipeData.setRecipeId(jsonObject.getInt("id"));
                recipeData.setRecipeName(jsonObject.getString("name"));
                recipeData.setRecipeServing(jsonObject.getInt("servings"));
                recipeData.setImageUrl(jsonObject.getString("image"));

                recipeArrayList.add(recipeData);

                recipeIds.add(jsonObject.getInt("id"));

                ingredientArrayList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONArray("ingredients");


                for (int j=0; j<jsonArray.length(); j++) {
                    JSONObject jsonObjectIngredient = jsonArray.getJSONObject(j);
                    IngredientData ingredientData = new IngredientData();
                    ingredientData.setRecipeQuantity(jsonObjectIngredient.getString("quantity"));
                    ingredientData.setRecipeMeasure(jsonObjectIngredient.getString("measure"));
                    ingredientData.setRecipeIngredient(jsonObjectIngredient.getString("ingredient"));

                    ingredientArrayList.add(ingredientData);

                    //insert Ingredient list in database
                    if (!isDataExist) {
                        addIngredient(jsonObjectIngredient.getString("measure"),
                                jsonObjectIngredient.getString("quantity"),
                                jsonObjectIngredient.getString("ingredient"),
                                jsonObject.getInt("id"));
                    }
                }
                ingredientHashMap.put(i, ingredientArrayList);

                stepsArrayList = new ArrayList<>();
                JSONArray jsonArraySteps = jsonObject.getJSONArray("steps");
                for (int j=0; j<jsonArraySteps.length(); j++) {
                    JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(j);
                    StepsData stepsData = new StepsData();
                    stepsData.setStepId(jsonObjectSteps.getInt("id"));
                    stepsData.setShortDesc(jsonObjectSteps.getString("shortDescription"));
                    stepsData.setDetailDesc(jsonObjectSteps.getString("description"));
                    stepsData.setVideoUrl(jsonObjectSteps.getString("videoURL"));
                    stepsData.setThumbUrl(jsonObjectSteps.getString("thumbnailURL"));

                    stepsArrayList.add(stepsData);
                }
                stepsHashMap.put(i, stepsArrayList);
            }

            SharedPreferences.Editor editor = sharedpreferences.edit();
            for (int i =0; i<recipeArrayList.size(); i++) {
                editor.putString("recipe_"+i, recipeArrayList.get(i).getRecipeName());
            }
            editor.putInt("recipe_size", recipeArrayList.size());
            editor.apply();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addIngredient(String measure, String quantity, String name, int recipe_id) {

        Uri uri = null;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(BakingContract.IngredientEntry.INGREDIENT_MEASURE, measure);
            contentValues.put(BakingContract.IngredientEntry.INGREDIENT_QUANTITY, quantity);
            contentValues.put(BakingContract.IngredientEntry.INGREDIENT_NAME, name);
            contentValues.put(BakingContract.IngredientEntry.RECIPE_ID, recipe_id);

            uri = getContentResolver().insert(BakingContract.IngredientEntry.CONTENT_URI, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Excp", e.getMessage());
        }
    }

    private void addFragment() {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new MainFragment();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("list", recipeArrayList);
            bundle.putSerializable("ingredient", ingredientHashMap);
            bundle.putSerializable("steps", stepsHashMap);
            bundle.putIntegerArrayList("recipe_ids", recipeIds);

            fragment.setArguments(bundle);

            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
            isFragmentAdded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(LIFECYCLE_RECIPES, recipeArrayList);
        outState.putSerializable(LIFECYCLE_INGREDIENT, ingredientHashMap);
        outState.putSerializable(LIFECYCLE_STEPS, stepsHashMap);
        outState.putIntegerArrayList(LIFECYCLE_RECIPE_IDS, recipeIds);
        outState.putBoolean(LIFECYCLE_FRAGMENT, isFragmentAdded);
        super.onSaveInstanceState(outState);
    }

    private boolean checkDataExist() {
        try {
            Cursor cursor = getContentResolver().query(BakingContract.IngredientEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
