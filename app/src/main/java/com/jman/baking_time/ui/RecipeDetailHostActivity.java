package com.jman.baking_time.ui;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jman.baking_time.R;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.interfaces.OnRecipeStepClickListener;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.models.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 16/06/2018.
 *
 * Host Activity to hold recipe step fragments
 *
 */

public class RecipeDetailHostActivity extends AppCompatActivity implements OnRecipeStepClickListener {

    private RecipeStepFragment recipeStepFragment;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_host);

        // create new instance of fragment and display using fragment manager
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();



        // use a FragmentManager and transaction to add fragment to the screen
        fragmentManager = getSupportFragmentManager();

        // Fragment transaction
        fragmentManager.beginTransaction()
                .add(R.id.recipeDetail_container, recipeDetailFragment)
           //     .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRecipeStepSelected(int position, Step step, Recipe recipe) {
        // this replaces the fragment with the recipe step fragment
        // Fragment transaction

        // pass Step data from fragment to fragment
        recipeStepFragment = new RecipeStepFragment();

        Bundle arguments = new Bundle();

        arguments.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) recipe.getSteps());
        arguments.putString("recipeName", recipe.getName());
        arguments.putString("stepId", step.getId());
        arguments.putString("videoUrl", step.getVideoURL());
        arguments.putString("thumbnailUrl", step.getThumbnailURL());
        arguments.putString("description", step.getDescription());
        arguments.putString("shortDescription", step.getShortDescription());

        recipeStepFragment.setArguments(arguments);


        fragmentManager.beginTransaction()
                .replace(R.id.recipeDetail_container, recipeStepFragment)
                .addToBackStack(null)
                .commit();
    }
}
