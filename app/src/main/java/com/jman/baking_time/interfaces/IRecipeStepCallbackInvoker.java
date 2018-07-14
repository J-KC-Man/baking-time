package com.jman.baking_time.interfaces;

import com.jman.baking_time.models.Step;

/**
 * Created by Justin on 11/07/2018.
 */

public interface IRecipeStepCallbackInvoker {

    void invokeRecipeStepCallback(int position, Step step);
}
