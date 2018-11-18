package com.jman.baking_time.ui;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.models.Ingredient;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.testing.SimpleIdlingResource;
import com.jman.baking_time.widget.RecipesWidgetIntentService;


import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {

    // To save data in default shared prefs for widget
    public static final String RECIPE_INGREDIENTS_DEFAULT_SHARED_PREF = "recipe_ingredients";
    public static final String RECIPE_NAME_DEFAULT_SHARED_PREF = "recipe_name";

    private RecipesFragment recipesFragment;

    // this variable will be null in production
    @Nullable
    private SimpleIdlingResource mIdlingResource;


    /*
    * creates a SimpleIdlingResource instance
    * */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the initialised SimpleIdlingResource instance
        getIdlingResource();

        // create new instance of fragment and display using fragment manager
        recipesFragment = new RecipesFragment();

        // use a FragmentManager and transaction to add fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Fragment transaction
        fragmentManager.beginTransaction()
                .add(R.id.recipes_container, recipesFragment)
                .commit();
    }

    /*
    * Saves selected recipe to default shared prefs
    * */
    private void setRecipeWidgetIngredientsList(List<Ingredient> ingredients, String recipeName) {
        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(ingredients);
        Log.v("MAIN ACTIVITY GSON", "GSON CONVERSION " + jsonIngredients);

//        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(RECIPE_NAME_DEFAULT_SHARED_PREF, recipeName);
//        editor.putString(RECIPE_INGREDIENTS_DEFAULT_SHARED_PREF, jsonIngredients);
//        editor.apply();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences
                .edit()
                .putString(RECIPE_NAME_DEFAULT_SHARED_PREF, recipeName)
                .putString(RECIPE_INGREDIENTS_DEFAULT_SHARED_PREF, jsonIngredients)
                .apply();
    }

    /*
    * handles fragment communication
    * */
    @Override
    public void onRecipeSelected(int position) {
        // start RecipeDetailHost Activity and pass bundle to host actvity to pass down to the fragment

        // init recipe object to get recipe info to pass in a Bundle
        Recipe recipe = recipesFragment.getRecipes().get(position);

        Recipe recipeDetail = new Recipe(
                recipe.getIngredients(),
                recipe.getSteps()
        );

        // for Widget
        setRecipeWidgetIngredientsList(recipe.getIngredients(), recipe.getName());

        /* Update the widget with the selected recipe */
        RecipesWidgetIntentService.startActionUpdateWidget(this);

        // launch intent and pass in bundle to RecipeDetailHostActivity
        // this bundle needs to have both of the arrays for a recipe
        Intent intent = new Intent(MainActivity.this, RecipeDetailHostActivity.class);


        intent.putExtra("recipeDetails", recipeDetail);
        startActivity(intent);
    }
}
