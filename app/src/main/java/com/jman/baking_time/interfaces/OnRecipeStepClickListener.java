package com.jman.baking_time.interfaces;

import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.models.Step;

import java.util.List;

/**
 * Created by Justin on 10/07/2018.
 */

public interface OnRecipeStepClickListener {

    /* Pass in position and step info when step button is clicked */
    void onRecipeStepSelected(int position, Step step, List<Step> steps);
   // void onRecipeStepSelected(int position, Step step, Recipe recipe);
}
