package com.jman.baking_time.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Step;
import com.jman.baking_time.ui.RecipeDetailFragment;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin on 15/07/2018.
 */

public class RecipeStepsViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = RecipeStepsViewAdapter.class.getSimpleName();

    private List<Step> steps;

    /*The application environment*/
    private Context mContext;

    private RecipeDetailFragment recipeDetailFragment;

    public RecipeStepsViewAdapter(List<Step> steps, Context context, RecipeDetailFragment recipeDetailFragment) {
        this.steps = steps;
        this.mContext = context;
        this.recipeDetailFragment = recipeDetailFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View stepsView = inflater.inflate(R.layout.recipe_description_list_item, parent, false);
        viewHolder = new RecipeStepsViewAdapter.DescriptionViewHolder(stepsView);
        Log.d(TAG, "returned STEP");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecipeStepsViewAdapter.DescriptionViewHolder descriptionViewHolder = (RecipeStepsViewAdapter.DescriptionViewHolder) holder;

        descriptionViewHolder.bindViews(position);
        Log.d(TAG, "onBindViewHolder for Steps Position: " + position);
    }

    @Override
    public int getItemCount() {

        if(steps == null) {
            return 0;
        }

        return steps.size();
    }

    class DescriptionViewHolder extends RecyclerView.ViewHolder {



        private Button viewStepButton;

        public DescriptionViewHolder(View itemView) {
            super(itemView);


            viewStepButton = itemView.findViewById(R.id.view_step_button);
        }

        public void bindViews(int position) {

            viewStepButton.setText("Step " + steps.get(position).getId() + ": " + steps.get(position).getShortDescription());

            viewStepButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition(); // gets item position
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        Log.d(TAG, "the STEP object is at position " + position);
                        recipeDetailFragment.invokeRecipeStepCallback(position, steps.get(position));
                    }
                }
            });
        }
    }
}
