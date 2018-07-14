package com.jman.baking_time.interfaces;

import com.jman.baking_time.models.Step;

/**
 * Created by Justin on 10/07/2018.
 */

public interface OnRecipeStepClickListener {

    void onRecipeStepSelected(int position, Step step);
}
