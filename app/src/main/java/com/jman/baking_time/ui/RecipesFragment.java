package com.jman.baking_time.ui;

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

public class RecipesFragment extends Fragment {

    /* RV */
    private RecyclerView recipesRecyclerView;

    private RecipesRecyclerViewAdapter mAdapter;

    // list of recipes
    private List<Recipe> recipes;

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
        mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext());

        // divider line at bottom of the recipe view
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Link the adapter to the RecyclerView
        recipesRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
