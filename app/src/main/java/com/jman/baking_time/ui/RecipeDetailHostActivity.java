package com.jman.baking_time.ui;

import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jman.baking_time.R;

import com.jman.baking_time.interfaces.OnRecipeStepClickListener;
import com.jman.baking_time.models.Ingredient;
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

    FragmentManager fragmentManager;

    private boolean mTwoPane;

    private Recipe recipe;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_host);

        recipe = getIntent().getExtras().getParcelable("recipeDetails");
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();

        Bundle arguments = new Bundle();

        // set these arguments in the bundle regardless of screen orientation
        arguments.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredients);
        arguments.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);

        if(findViewById(R.id.activity_recipe_detail_landscape) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) {
                // create the recipe step fragment
                fragmentManager = getSupportFragmentManager();

                RecipeStepFragment recipeStepFragmentLand = new RecipeStepFragment();

                arguments.putString("stepId", steps.get(0).getId());
                arguments.putString("videoUrl", steps.get(0).getVideoURL());
                arguments.putString("thumbnailUrl", steps.get(0).getThumbnailURL());
                arguments.putString("description", steps.get(0).getDescription());
                arguments.putString("shortDescription", steps.get(0).getShortDescription());

                // set ingredients and steps to be passed to the recipe step frag
                recipeStepFragmentLand.setArguments(arguments);

                fragmentManager.beginTransaction()
                        .add(R.id.recipeStep_container, recipeStepFragmentLand)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

        // create new instance of fragment and display using fragment manager
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

        recipeDetailFragment.setArguments(arguments);

        // use a FragmentManager and transaction to add fragment to the screen
        fragmentManager = getSupportFragmentManager();

        // Fragment transaction
        // dont add to backstack as it enables easier up navigation
        fragmentManager.beginTransaction()
                .replace(R.id.recipeDetail_container, recipeDetailFragment)
                .commit();
    }

    @Override
    public void onRecipeStepSelected(int position, Step step, List<Step> steps) {
        // this replaces the fragment with the recipe step fragment
        // Fragment transaction

        // pass Step data from fragment to fragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();

        Bundle arguments = new Bundle();

        // list of steps is needed for navigation between steps
        arguments.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);

        arguments.putString("stepId", step.getId());
        arguments.putString("videoUrl", step.getVideoURL());
        arguments.putString("thumbnailUrl", step.getThumbnailURL());
        arguments.putString("description", step.getDescription());
        arguments.putString("shortDescription", step.getShortDescription());

        recipeStepFragment.setArguments(arguments);

        // if in landscape. put step details in step container
        if(mTwoPane == true) {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipeStep_container, recipeStepFragment)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.recipeDetail_container, recipeStepFragment)
                    .addToBackStack(null)
                    .commit();
        }



    }
}
