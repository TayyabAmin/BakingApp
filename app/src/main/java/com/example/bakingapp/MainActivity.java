package com.example.bakingapp;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        } catch (JSONException e) {
            e.printStackTrace();
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
}
