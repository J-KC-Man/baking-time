package com.jman.baking_time.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.models.Recipe;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

import static com.jman.baking_time.data.JsonData.json;

public class MainActivity extends AppCompatActivity implements OnRecipeClickListener {

    private  RecipesFragment recipesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    * handles fragment communication
    * */
    @Override
    public void onRecipeSelected(int position) {
        // start REcipeDetailHost Activity and pass bundle to host actvity to pass down to the fragment

        // init recipe object to get recipe info to pass in a Bundle
        Recipe recipe = recipesFragment.getRecipes().get(position);

        Recipe recipeDetail = new Recipe(
                recipe.getIngredients(),
                recipe.getSteps()
        );

        // launch intent and pass in bundle to RecipeDetailHostActivity
        // this bundle needs to have both of the arrays for a recipe
        Intent intent = new Intent(MainActivity.this, RecipeDetailHostActivity.class);


        intent.putExtra("recipeDetails", recipeDetail);
        startActivity(intent);
    }
}
