package com.jman.baking_time.ui;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jman.baking_time.R;
import com.jman.baking_time.adapters.RecipesRecyclerViewAdapter;
import com.jman.baking_time.interfaces.CallbackInvoker;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.remoteDataSource.WebService;
import com.jman.baking_time.remoteDataSource.WebServiceGenerator;
import com.jman.baking_time.testing.SimpleIdlingResource;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Justin on 12/06/2018.
 *
 */

public class RecipesFragment extends Fragment implements CallbackInvoker {

    /* RV */
    private RecyclerView recipesRecyclerView;

    private RecipesRecyclerViewAdapter mAdapter;

    // list of recipes
    private List<Recipe> recipes;

    private OnRecipeClickListener mCallback;

    private WebService apiClient;

    SimpleIdlingResource idlingResource;

    public List<Recipe> getRecipes() {
        return this.recipes;
    }

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

//        Type recipesListType = new TypeToken<List<Recipe>>(){}.getType();
//        recipes = new Gson().fromJson(json, recipesListType);


        // bind recyclerView with XML recyclerView declaration
        recipesRecyclerView = rootView.findViewById(R.id.recipeList_RecyclerView);

        // attach recyclerView and adapter
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // don't init idlingResource too early (before fragment is instantiated)
        // otherwise you get a null exception
        idlingResource = (SimpleIdlingResource)((MainActivity)getActivity()).getIdlingResource();

        // protect against NPE
        if (idlingResource != null) {
            /*
            * set idle to false
            * idle means:

                -No UI events in the current message queue
                -No more tasks in the default AsyncTask thread pool

                idle is false as there are tasks or events that are happening
                and any testing should be on halt until these processes finish.
            *
            * */
            idlingResource.setIdleState(false);
        }

        loadRecipesData();

        // Create an adapter for that cursor to display the data
       // mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext(), RecipesFragment.this);

        // divider line at bottom of the recipe view
        recipesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        return rootView;
    }

    public void loadRecipesData() {

        // assign retrofit client object that implements the WebService interface
        apiClient = WebServiceGenerator.createService();

        /* declare and assign a call object to make the request*/
        Call<List<Recipe>> networkRequest = apiClient.getRecipesJson();

        networkRequest.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if(response.isSuccessful()) {

                    recipes = response.body();
                    Log.d("response", response.body().toString());

                    if(mAdapter == null) {
                        // init adapter and attach to RecyclerView
                        mAdapter = new RecipesRecyclerViewAdapter(recipes, getContext(), RecipesFragment.this);

                        // Link the adapter to the RecyclerView
                        recipesRecyclerView.setAdapter(mAdapter);

                    } else {
                        // else update the UI
                        mAdapter.updateRecipesUI(recipes);

                    }
                }

                // for Espresso testing
                /*
                * idle set to true since:
                *   -No UI events in the current message queue
                    -No more tasks in the default AsyncTask thread pool, api call has finished
                * */
                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(getActivity(),
                        "network request failed",
                        Toast.LENGTH_LONG).show();
                t.getStackTrace();


            }
        });

    }
}
