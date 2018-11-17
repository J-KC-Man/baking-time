package com.jman.baking_time.adapters;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jman.baking_time.R;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.ui.MainActivity;
import com.jman.baking_time.ui.RecipeDetailHostActivity;
import com.jman.baking_time.ui.RecipesFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 12/06/2018.
 */

public class RecipesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipesRecyclerViewAdapter.class.getSimpleName();

    // list of recipes
    private List<Recipe> recipes;

    private Context mContext;

    private RecipesFragment mFragment;

    public RecipesRecyclerViewAdapter(List<Recipe> recipes, Context mContext, RecipesFragment fragment) {
        this.mFragment = fragment;
        this.recipes = recipes;
        this.mContext = mContext; // important so the adapter knows which activity it is attached to
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View recipesView = inflater.inflate(R.layout.recipe_list_item, parent, false);
        viewHolder = new RecipeViewHolder(recipesView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipeViewHolder recipesVH = (RecipeViewHolder) holder;
        recipesVH.bindRecipeView(position);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public void updateRecipesUI(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView name;
        private TextView servings;
        private ImageView backgroundImage;

        public RecipeViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.recipe_title);
            servings = itemView.findViewById(R.id.servingsTitle);

            // Init image view and bind it
            backgroundImage = itemView.findViewById(R.id.recipe_imageview);

            itemView.setOnClickListener(RecipeViewHolder.this);

        }

        public void bindRecipeView(int position) {
            // bind views
            name.setText(recipes.get(position).getName());
            servings.setText("Servings: " + recipes.get(position).getServings());

            File f = new File("baking-time\\app\\src\\main\\res\\drawable\\nutella.jpg");
            String nutellaURL = "https://hips.hearstapps.com/del.h-cdn.co/assets/16/32/1470773544-delish-nutella-cool-whip-pie-1.jpg";

            Picasso picasso = Picasso.get();

            picasso.setLoggingEnabled(true);

            picasso.load(R.drawable.nutella)
                   // .fit()
                    .resize(600, 200)
                    .centerCrop()
                    .into(backgroundImage);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it

                // trigger click listener callback implemented in fragment class
                mFragment.invokeCallback(position);
            }
        }
    } // end of viewholder class
} // end of adapter class
