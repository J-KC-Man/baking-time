package com.jman.baking_time.ui;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.adapters.RecipesRecyclerViewAdapter;
import com.jman.baking_time.interfaces.CallbackInvoker;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.repository.RecipeRepository;
import com.jman.baking_time.viewmodels.RecipesViewModel;
import com.jman.baking_time.viewmodels.RecipesViewModelFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.jman.baking_time.data.JsonData.json;

/**
 * Created by Justin on 12/06/2018.
 *
 * This view will have a recyclerView in it
 */

public class RecipesFragment extends Fragment implements CallbackInvoker {

    /* RV */
    private RecyclerView recipesRecyclerView;

    private RecipesRecyclerViewAdapter mAdapter;



    // list of recipes
    private List<Recipe> recipes;
//
//    public List<Recipe> getRecipes() {
//        return this.recipes;
//    }

    private OnRecipeClickListener mCallback;

    /* Viewmodel*/
    private RecipesViewModel mRecipesViewModel;

   // @Inject
    RecipesViewModelFactory factory;

    RecipeRepository recipeRepository;

    /*
    * Tell host activity to call its onRecipeSelected(position) implementation
    * */
    @Override
    public void invokeCallback(int position) {
        mCallback.onRecipeSelected(position);
    }

    /*
    * Make sure container activity has implemented the callback
    * The context is the host activity that has implemented the OnRecipeClickListener
    * interface
    * */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener");
        }
    }

    /*
        * Mandatory constructor for instantiating the fragment
        * */
    public RecipesFragment() {
    }

    /*
    * Inflates the fragment layout and sets any image resources
    * */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipes_list, container, false);

//        Type recipesListType = new TypeToken<List<Recipe>>(){}.getType();
//        recipes = new Gson().fromJson(json, recipesListType);

        recipeRepository = new RecipeRepository();

        factory = new RecipesViewModelFactory(recipeRepository);

        // bind recyclerView with XML recyclerView declaration
        recipesRecyclerView = rootView.findViewById(R.id.recipeList_RecyclerView);

        // attach recyclerView and adapter
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create an adapter to display the data
       // mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext(), RecipesFragment.this);
        mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext(), RecipesFragment.this);

        // divider line at bottom of the recipe view
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Link the adapter to the RecyclerView
        recipesRecyclerView.setAdapter(mAdapter);


        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        createRecipesViewModel();

        mRecipesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mAdapter.updateRecipeListUI(recipes);
            }
        });
    }


    public void createRecipesViewModel() {

        // creates or retrieves existing viewmodel
        // and associates fragment with viewmodel with Activity Scope so it can be shared with other fragments

        mRecipesViewModel = ViewModelProviders.of(getActivity(),factory).get(RecipesViewModel.class);
    }
}
