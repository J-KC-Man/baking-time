package com.jman.baking_time.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Ingredient;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin on 21/07/2018.
 */

public class RecipeIngredientsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = RecipeIngredientsViewAdapter.class.getSimpleName();

    private List<Ingredient> ingredients;

    private Context mContext;

    public RecipeIngredientsViewAdapter(List<Ingredient> ingredients, Context mContext) {
        this.ingredients = ingredients;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View ingredientsView = inflater.inflate(R.layout.recipe_ingredients_list_item, parent, false);
        viewHolder = new RecipeIngredientsViewAdapter.IngredientsViewHolder(ingredientsView);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipeIngredientsViewAdapter.IngredientsViewHolder ingredientsViewHolder = (RecipeIngredientsViewAdapter.IngredientsViewHolder) holder;
        ingredientsViewHolder.bindView(position);
        Log.d(TAG, "item count " + getItemCount());
        Log.d(TAG, "onBindViewHolder for INGREDIENTS Position: " + position);
    }

    @Override
    public int getItemCount() {
        if(ingredients == null) {
            return 0;
        }
        return ingredients.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private static final String TBLSP = "Tablespoon";
        private static final String CUP = "Cup";
        private static final String TSP = "Teaspoon";
        private static final String K = "Kilogram";
        private static final String G = "Grams";
        private static final String OZ = "Oz";

        private TextView ingredient;



        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient_textView);


        }

        public void bindView(int position) {

            ingredient.setText(
                    ingredients.get(position).getQuantity() + " "
                            + ingredients.get(position).getMeasure() + " "
                            + ingredients.get(position).getIngredient());
        }

    } // end of viewholder class
}
