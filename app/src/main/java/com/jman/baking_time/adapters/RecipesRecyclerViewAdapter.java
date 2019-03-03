package com.jman.baking_time.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.ui.RecipesFragment;
import com.squareup.picasso.Picasso;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

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

            Picasso picasso = Picasso.get();

            picasso.setLoggingEnabled(true);

            String id = recipes.get(position).getId();
            int imageResource = 0;

            /*
            * Sources for images:
            * Nutella pie (R.drawable.nutella): https://hips.hearstapps.com/del.h-cdn.co/assets/16/32/1470773544-delish-nutella-cool-whip-pie-1.jpg

              Brownies (R.drawable.brownies): https://hips.hearstapps.com/del.h-cdn.co/assets/17/11/1489456319-delish-baileys-brownies-4.jpg

              Yellow cake (R.drawable.yellowcake): https://i1.wp.com/media.hungryforever.com/wp-content/uploads/2017/07/11112744/easy-yellow-cake-recipe.jpg?ssl=1?w=356&strip=all&quality=80

              Cheesecake (R.drawable.cheesecake): https://truffle-assets.imgix.net/dkkdgt98uadb_3Z5UdC2zKMwWUGC48aMEmS_Lemonade-Cheesecake_landscapeThumbnail_en-UK.png
            *
            * */

            switch (id) {
                case "1":
                    imageResource = R.drawable.nutella;
                    break;
                case "2":
                    imageResource = R.drawable.brownies;
                    break;
                case "3":
                    imageResource = R.drawable.yellowcake;
                    break;
                case "4":
                    imageResource = R.drawable.cheesecake;
                default:
                    break;
            }

            picasso.load(imageResource)
                    .fit() // resize() to reduce the image size to the dimensions of the ImageView
                    .centerInside() // scale dimensions that are equal to or less than bounds of the ImageView
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
