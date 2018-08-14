package com.jman.baking_time.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jman.baking_time.R;
import com.jman.baking_time.models.Ingredient;
import com.jman.baking_time.models.Step;
import com.jman.baking_time.ui.RecipeDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 16/06/2018.
 *
 */

public class RecipeDetailRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = RecipeDetailRecyclerViewAdapter.class.getSimpleName();

    private final int INGREDIENTS = 0, STEP = 1;

    private List<Ingredient> ingredients;

    private List<Step> steps;

    private List<Object> ingredientsAndSteps = new ArrayList<>();

    private Context mContext;

    RecyclerView.ViewHolder viewHolder;

    private RecipeDetailFragment recipeDetailFragment;

    public RecipeDetailRecyclerViewAdapter(List<Ingredient> ingredients, List<Step> steps, Context context, RecipeDetailFragment fragment) {
        this.ingredients = ingredients;
        this.steps = steps;
        this.mContext = context;
        this.recipeDetailFragment = fragment;

        ingredientsAndSteps.addAll(ingredients);
        ingredientsAndSteps.addAll(steps);


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case INGREDIENTS:
                View ingredientsView = inflater.inflate(R.layout.recipe_ingredients_list_item, parent, false);
                viewHolder = new IngredientsViewHolder(ingredientsView);
                break;
            case STEP:
                View stepsView = inflater.inflate(R.layout.recipe_description_list_item, parent, false);
                viewHolder = new DescriptionViewHolder(stepsView);
                Log.d(TAG, "returned STEP");
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREDIENTS:
                IngredientsViewHolder ingredientsViewHolder = (IngredientsViewHolder) holder;
                ingredientsViewHolder.bindView(position);
                Log.d(TAG, "item count " + getItemCount());
                Log.d(TAG, "onBindViewHolder for INGREDIENTS Position: " + position);
                break;
            case STEP:
                DescriptionViewHolder descriptionViewHolder = (DescriptionViewHolder) holder;
                descriptionViewHolder.bindViews(position - ingredients.size());
                Log.d(TAG, "onBindViewHolder for Steps Position: " + position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ingredientsAndSteps.size();
    }

    @Override
    public int getItemViewType(int position) {
        // top of the recyclerview list of viewholders
//        if (position > 0 && position < ingredients.size()) {
//            Log.d(TAG, "returned INGREDIENTS at position " + position);
//            Log.d(TAG, "size of ingredients list " + ingredients.size());
//           // Log.d(TAG, "size of ingredients and steps list " + (ingredients.size() + steps.size()));
//            return INGREDIENTS;
//        }
//        else if (position >= ingredients.size() && position < steps.size() + ingredients.size()) { //if between 9 and the number of steps inflate review views after ingredients
//            Log.d(TAG, "returned STEP at position " + position);
//            return STEP;
//        }
//        else {
//            Log.d(TAG, "returned STEP at position " + position);
//            return STEP;
//        }

        if(ingredientsAndSteps.get(position) instanceof Ingredient) {
            Log.d(TAG, "returned INGREDIENTS at position " + position);
            return INGREDIENTS;
        }
        else if(ingredientsAndSteps.get(position) instanceof Step) {
            Log.d(TAG, "returned STEP at position " + position);
            return STEP;
        }

            return -1;

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

    class DescriptionViewHolder extends RecyclerView.ViewHolder {


        private TextView stepTitle;
        private TextView stepDescription;
        private Button viewStepButton;

        public DescriptionViewHolder(View itemView) {
            super(itemView);

            stepTitle = itemView.findViewById(R.id.recipe_description_title_textView);
            stepDescription = itemView.findViewById(R.id.recipe_description_textView);
            viewStepButton = itemView.findViewById(R.id.view_step_button);
        }

        public void bindViews(int position) {
            stepTitle.setText("Step " + steps.get(position).getId());
            stepDescription.setText(steps.get(position).getShortDescription());

            viewStepButton.setText("View step " + steps.get(position).getId());

            viewStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        Log.d(TAG, "the STEP object is at position " + position);
                        recipeDetailFragment.invokeRecipeStepCallback(position, steps.get(position-ingredients.size()));
                    }
                }
            });
        }
    }

} // end of class
