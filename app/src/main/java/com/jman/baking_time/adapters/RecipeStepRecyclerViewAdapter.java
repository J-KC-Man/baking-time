package com.jman.baking_time.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Step;
import com.jman.baking_time.ui.RecipeDetailFragment;
import com.squareup.picasso.Picasso;

/**
 * Created by Justin on 05/07/2018.
 *
 * RV for an individual recipe step
 */

public class RecipeStepRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Step step;

   // private RecipeDetailFragment mFragment;
    private Context mContext;

    private Bundle bundle;


    public RecipeStepRecyclerViewAdapter(Step step, Context context, Bundle bundle) {
        this.step = step;
        this.mContext = context;
        // to make sure the right instance of fragment is passed with the right callback
       // this.mFragment = fragment;
        this.bundle = bundle;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // define the context
        View recipeStepView = inflater.inflate(R.layout.recipe_step_list_item, parent, false);
        // pass in context to viewholder
        viewHolder = new RecipeStepRecyclerViewAdapter.RecipeStepViewHolder(recipeStepView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        RecipeStepRecyclerViewAdapter.RecipeStepViewHolder recipeStepVH = (RecipeStepRecyclerViewAdapter.RecipeStepViewHolder) holder;

        recipeStepVH.bindViews();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class RecipeStepViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView recipeStepDescription;
        Button nextButton;
        Button previousButton;

        /*Exoplayer */
        private SimpleExoPlayer mExoPlayer;
        private SimpleExoPlayerView mPlayerView;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);

            // Initialize the player view.
            mPlayerView = itemView.findViewById(R.id.playerView);

            //imageView = itemView.findViewById(R.id.imageView);
            recipeStepDescription = itemView.findViewById(R.id.recipe_step_description);
            nextButton = itemView.findViewById(R.id.button_next);
            previousButton = itemView.findViewById(R.id.button_prev);

            initializePlayer(Uri.parse(bundle.getString("thumbnailUrl")));
        }

        public void bindViews() {
            recipeStepDescription.setText(bundle.getString("description"));

//            Picasso.with(mContext).
//                    load(bundle.getString("thumbnailUrl")).
//                    into(mPlayerView);

        }

        /**
         * Initialize ExoPlayer.
         * @param mediaUri The URI of the sample to play.
         */
        private void initializePlayer(Uri mediaUri) {
            if (mExoPlayer == null) {
                // Create an instance of the ExoPlayer.
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
                mPlayerView.setPlayer(mExoPlayer);
                // Prepare the MediaSource.
                String userAgent = Util.getUserAgent(mContext, "Baking Time");
                MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                        mContext, userAgent), new DefaultExtractorsFactory(), null, null);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);
            }
        }

        /*Release ExoPlayer*/
        private void releasePlayer() {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }


    }
}
