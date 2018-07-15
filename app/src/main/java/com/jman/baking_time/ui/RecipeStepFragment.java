package com.jman.baking_time.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jman.baking_time.R;
import com.jman.baking_time.adapters.RecipeStepRecyclerViewAdapter;
import com.jman.baking_time.adapters.RecipesRecyclerViewAdapter;
import com.jman.baking_time.interfaces.CallbackInvoker;
import com.jman.baking_time.interfaces.IRecipeStepCallbackInvoker;
import com.jman.baking_time.interfaces.OnRecipeClickListener;
import com.jman.baking_time.models.Step;

/**
 * Created by Justin on 19/06/2018.
 */

public class RecipeStepFragment extends Fragment {


    private RecyclerView recipeStepRecyclerView;

    private RecipeStepRecyclerViewAdapter mAdapter;

    private Step step;

    private Bundle bundle;

//    /*
//   * Make sure container activity has implemented the callback
//   * The context is the host activity that has implemented the OnRecipeClickListener
//   * interface
//   * */
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try {
//            mCallback = (OnRecipeClickListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() + " must implement OnRecipeClickListener");
//        }
//    }

    /*
    * Mandatory constructor for instantiating the fragment
    * */
    public RecipeStepFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        bundle = getArguments();

        getActivity().setTitle(bundle.getString("shortDescription"));

        // bind recyclerView with XML recyclerView declaration
        recipeStepRecyclerView = rootView.findViewById(R.id.recipeStep_RecyclerView);

        // attach recyclerView and adapter
        recipeStepRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create an adapter to display the data
        mAdapter = new RecipeStepRecyclerViewAdapter(step, getContext(), bundle);

        // divider line at bottom of the recipe view
//        recipeStepRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        // Link the adapter to the RecyclerView
        recipeStepRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


}
