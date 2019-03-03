package com.jman.baking_time.ui;

import android.os.Parcelable;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
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

    private static final String TAG = RecipeDetailHostActivity.class.getSimpleName();

    private final String RECIPE_DETAIL_FRAGMENT_TAG = "RecipeDetailFragmentTag";
    private final String RECIPE_STEP_FRAGMENT_TAG = "RecipeStepFragmentTag";

    FragmentManager fragmentManager;

    private boolean mTwoPane;

    private Recipe recipe;
    private List<Ingredient> ingredients;
    private List<Step> steps;

    RecipeDetailFragment recipeDetailFragment;
    RecipeStepFragment recipeStepFragment;

    private String currentFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_host);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recipe = getIntent().getExtras().getParcelable("recipeDetails");
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();

        Bundle arguments = new Bundle();

        // set these arguments in the bundle regardless of screen orientation
        arguments.putParcelableArrayList("ingredients", (ArrayList<? extends Parcelable>) ingredients);
        arguments.putParcelableArrayList("steps", (ArrayList<? extends Parcelable>) steps);

        // if in landscape
        if(findViewById(R.id.activity_recipe_detail_landscape) != null) {
            mTwoPane = true;

            if(savedInstanceState == null) { // if nothing has been saved
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
        }
        else { // if not in landscape
            mTwoPane = false;
        }

        // create new instance of fragment and display using fragment manager
        recipeDetailFragment = new RecipeDetailFragment();

        recipeDetailFragment.setArguments(arguments);

        // use a FragmentManager and transaction to add fragment to the screen
        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) { // first time loading up
            currentFragmentTag = RECIPE_DETAIL_FRAGMENT_TAG;
            loadFragment(recipeDetailFragment, RECIPE_DETAIL_FRAGMENT_TAG);
        } else {
            currentFragmentTag = savedInstanceState.getString("currentFragment");

            if(currentFragmentTag == RECIPE_DETAIL_FRAGMENT_TAG) {
                // look up the instance that already exists by tag
                recipeDetailFragment = (RecipeDetailFragment)
                        fragmentManager.findFragmentByTag(RECIPE_DETAIL_FRAGMENT_TAG);

                loadFragment(recipeDetailFragment, currentFragmentTag);
            }
            else if(currentFragmentTag == RECIPE_STEP_FRAGMENT_TAG) {
                recipeStepFragment = (RecipeStepFragment)
                        fragmentManager.findFragmentByTag(RECIPE_STEP_FRAGMENT_TAG);

                loadFragment(recipeStepFragment, currentFragmentTag);
            }
        }

    }


    private boolean loadFragment(Fragment fragment, String tag) {

        // Fragment transaction
        // dont add to backstack as it enables easier back navigation
        if(fragment != null && !fragment.isInLayout()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.recipeDetail_container, fragment, tag)
                    .commit();

            return true;
        }
        // do nothing
        return false;
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

        currentFragmentTag = RECIPE_STEP_FRAGMENT_TAG;

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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("currentFragment", currentFragmentTag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
