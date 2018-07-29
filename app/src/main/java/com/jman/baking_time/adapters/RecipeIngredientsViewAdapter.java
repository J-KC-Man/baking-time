package com.jman.baking_time.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jman.baking_time.R;
import com.jman.baking_time.models.Ingredient;

import java.util.List;

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
        private TextView quantity;


        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ingredient = itemView.findViewById(R.id.ingredient_textView);
            quantity = itemView.findViewById(R.id.quantity_textView);

        }

        public void bindView(int position) {
            ingredient.setText(ingredients.get(position).getIngredient());

            String measure = ingredients.get(position).getMeasure();

            switch (measure) {
                case "TBLSP":
                    quantity.setText(determineQuantity(position, TBLSP));
                    break;
                case "CUP":
                    quantity.setText(determineQuantity(position, CUP));
                    break;
                case "TSP":
                    quantity.setText(determineQuantity(position, TBLSP));
                    break;
                case "UNIT":
                    quantity.setText(
                            ingredients.get(position).getQuantity());
                    break;
                case "OZ":
                    quantity.setText(
                            ingredients.get(position).getQuantity() + "" + OZ);
                    break;
                case "K":
                    quantity.setText(
                            ingredients.get(position).getQuantity() + "" + K);
                    break;
                case "G":
                    quantity.setText(
                            ingredients.get(position).getQuantity() + "" + K);
                    break;

            }
        }

        public String determineQuantity(int position, String originalQuantity) {
            double quantity = Double.parseDouble(ingredients.get(position).getQuantity());

            if(quantity > 1) {
                return ingredients.get(position).getQuantity() + " " + originalQuantity + "s";
            } else {
                return ingredients.get(position).getQuantity() + " " + originalQuantity;
            }
        }

    } // end of viewholder class
}
