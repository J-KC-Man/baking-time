package com.jman.baking_time.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jman.baking_time.R;
/**
 * Created by Justin on 16/06/2018.
 *
 * Host Activity to hold recipe step fragments
 *
 */

public class RecipeDetailHostActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_host);

        // create new instance of fragment and display using fragment manager
        RecipesFragment recipesFragment = new RecipesFragment();

        // use a FragmentManager and transaction to add fragment to the screen
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Fragment transaction
        fragmentManager.beginTransaction()
                .add(R.id.recipeDetail_container, recipesFragment)
                .commit();
    }
}
