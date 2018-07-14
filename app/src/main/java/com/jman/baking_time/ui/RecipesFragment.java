package com.jman.baking_time.ui;

import android.app.Activity;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

    private OnRecipeClickListener mCallback;

//    public OnRecipeClickListener getmCallback() {
//        return mCallback;
//    }

    /*
    * Tell host activity to call its onRecipeSelected(position) implementation
    * */
    @Override
    public void invokeCallback(int position) {
        mCallback.onRecipeSelected(position);
    }

//    public interface OnRecipeClickListener {
//        void onRecipeSelected(int position); // position that user click in RecyclerView
//    }

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

        Type recipesListType = new TypeToken<List<Recipe>>(){}.getType();
        recipes = new Gson().fromJson(json, recipesListType);

        // bind recyclerView with XML recyclerView declaration
        recipesRecyclerView = rootView.findViewById(R.id.recipeList_RecyclerView);

        // attach recyclerView and adapter
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create an adapter for that cursor to display the data
        mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext(), RecipesFragment.this);

        // divider line at bottom of the recipe view
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Link the adapter to the RecyclerView
        recipesRecyclerView.setAdapter(mAdapter);


        return rootView;
    }
}
