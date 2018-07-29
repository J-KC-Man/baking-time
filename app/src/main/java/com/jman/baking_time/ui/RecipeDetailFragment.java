package com.jman.baking_time.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jman.baking_time.R;
import com.jman.baking_time.adapters.RecipeDetailRecyclerViewAdapter;
import com.jman.baking_time.adapters.RecipeIngredientsViewAdapter;
import com.jman.baking_time.adapters.RecipeStepRecyclerViewAdapter;
import com.jman.baking_time.adapters.RecipeStepsViewAdapter;
import com.jman.baking_time.interfaces.IRecipeStepCallbackInvoker;
import com.jman.baking_time.interfaces.OnRecipeStepClickListener;
import com.jman.baking_time.models.Ingredient;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.models.Step;

import java.util.List;


/**
 * Created by Justin on 16/06/2018.
 *
 * This fragment shows the recipe ingredients and clickable steps
 */

public class RecipeDetailFragment extends Fragment implements IRecipeStepCallbackInvoker {

    // set up RV and Adapter
    private RecyclerView recipeDetailRecyclerView;
    private RecyclerView recipeIngredientsRecyclerView;
    private RecyclerView recipeStepRecyclerView;


    private RecipeIngredientsViewAdapter recipeIngredientsViewAdapter;
    private RecipeStepsViewAdapter recipeStepsViewAdapter;

    private RecipeDetailRecyclerViewAdapter mAdapter;

    private Recipe recipe;

    private List<Ingredient> ingredients;
    private List<Step> steps;

    private OnRecipeStepClickListener mCallback;

    /*
 * Make sure container activity has implemented the callback
 * The context is the host activity that has implemented the OnRecipeClickListener
 * interface
 * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener");
        }
    }

    /*
    * Mandatory constructor for instantiating the fragment
    * */
    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        recipe = getActivity().getIntent().getExtras().getParcelable("recipeDetails");
        ingredients = recipe.getIngredients();
        steps = recipe.getSteps();

//        recipeDetailRecyclerView = rootView.findViewById(R.id.recipeDetail_RecyclerView);
//        recipeDetailRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        // Create an adapter for that cursor to display the data
//        mAdapter = new RecipeDetailRecyclerViewAdapter(ingedients, steps, getContext(), RecipeDetailFragment.this);
//
//        // divider line at bottom of the recipe view
//        recipeDetailRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
//
//        // Link the adapter to the RecyclerView
//        recipeDetailRecyclerView.setAdapter(mAdapter);

        // set up ingredients RV
        // Create an adapter for that cursor to display the data
        // Link the adapter to the RecyclerView
        recipeIngredientsRecyclerView = rootView.findViewById(R.id.recipeIngredients_RecyclerView);
        recipeIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeIngredientsRecyclerView.setHasFixedSize(true);

        recipeIngredientsViewAdapter = new RecipeIngredientsViewAdapter(ingredients, getContext());
        recipeIngredientsRecyclerView.setAdapter(recipeIngredientsViewAdapter);

        // set up steps RV
        recipeStepRecyclerView = rootView.findViewById(R.id.recipeSteps_RecyclerView);
        recipeStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeStepRecyclerView.setHasFixedSize(true);

        recipeStepsViewAdapter = new RecipeStepsViewAdapter(steps, getContext(), RecipeDetailFragment.this);
        recipeStepRecyclerView.setAdapter(recipeStepsViewAdapter);

        return rootView;
    }

    @Override
    public void invokeRecipeStepCallback(int position, Step step) {
        mCallback.onRecipeStepSelected(position, step);
    }
}
