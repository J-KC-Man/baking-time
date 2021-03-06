package com.jman.baking_time.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import androidx.core.app.NotificationCompat.Style;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jman.baking_time.R;
import com.jman.baking_time.models.Step;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Justin on 19/06/2018.
 */

public class RecipeStepFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();
//    private RecyclerView recipeStepRecyclerView;
//
//    private RecipeStepRecyclerViewAdapter mAdapter;
//
//    private Step step;

    private List<Step> steps;
    int stepId;
    private String recipeName;

    private Bundle bundle;


    TextView recipeStepDescription;
    Button nextButton;
    Button previousButton;
    boolean nextButtonClicked;

    /*Exoplayer */
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private long playerPosition;
    private String videoUri;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;

    private boolean mIsTwoPane;

    public boolean checkIfLandscape() {
        if(getActivity().findViewById(R.id.activity_recipe_detail_landscape) != null) {
            mIsTwoPane = true;
        } else {
            mIsTwoPane = false;
        }
        return mIsTwoPane;
    }

    /*
    * Mandatory constructor for instantiating the fragment
    * */
    public RecipeStepFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.i("RecipeStepFragment", "onCreateView. Saved state? "+ (savedInstanceState != null));

        View rootView = inflater.inflate(R.layout.recipe_step_list_item, container, false);

        bundle = getArguments();
//        recipeName = bundle.getString("recipeName");

        steps = bundle.getParcelableArrayList("steps");
        stepId = Integer.parseInt(bundle.getString("stepId"));


        getActivity().setTitle(bundle.getString("shortDescription"));

        videoUri = bundle.getString("videoUrl");

        // Initialize the player view.
        mPlayerView = rootView.findViewById(R.id.playerView);


        recipeStepDescription = rootView.findViewById(R.id.recipe_step_description);
        nextButton = rootView.findViewById(R.id.button_next);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked = true;
                changeStep(nextButtonClicked);
            }
        });
        previousButton = rootView.findViewById(R.id.button_prev);


        // if in landscape make previous button disappear
        // when screen is first created
            if(checkIfLandscape() == true && stepId == 0) {
                previousButton.setVisibility(View.GONE);
            }

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButtonClicked = false;
                changeStep(nextButtonClicked);
            }
        });

        bindViews();

        playerPosition = C.TIME_UNSET; // unknown or unset time or duration
        if(savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong("player_position", C.TIME_UNSET);
        }

        return rootView;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

            outState.putLong("player_position", playerPosition);

    }

    public void bindViews() {
        recipeStepDescription.setText(bundle.getString("description"));

    }

    public void changeStep(boolean nextButtonClicked) {

        if(nextButtonClicked) { // if next button was clicked ie: nextButtonClicked=true

            // replace text with step at position of stepID + 1
            if(stepId < steps.size() - 1) {
                stepId++;

                // make next button disappear on last step
                if(stepId == steps.size() - 1 && checkIfLandscape() == true) {
                    nextButton.setVisibility(View.GONE);
                }

                // make previous button appear on subsequent steps
                if(stepId > 0 && checkIfLandscape() == true) {
                    previousButton.setVisibility(View.VISIBLE);
                }
                // may need to replace the fragment using the activity
                recipeStepDescription.setText(steps.get(stepId).getDescription());
                getActivity().setTitle(steps.get(stepId).getShortDescription());

                // reinit exoplayer
                reInitPlayer(steps.get(stepId).getVideoURL());


            } else {
                // go back to recipe details - tell host activity to replace fragment
                // by removing activity from backstack - this needs addToBackStack() to work
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                    getActivity().setTitle(recipeName);
                }
            }


        } else { // if Previous button clicked
            // replace text with step at position of stepID - 1

            if(stepId > 0) {
                stepId--;

                // make next button appear again for previous steps
                if(checkIfLandscape() == true && nextButton.getVisibility() == View.GONE) {
                    nextButton.setVisibility(View.VISIBLE);
                }

                // make 'previous' button disappear on first step
                if(stepId == 0 && checkIfLandscape() == true) {
                    previousButton.setVisibility(View.GONE);
                }

                recipeStepDescription.setText(steps.get(stepId).getDescription());
                getActivity().setTitle(steps.get(stepId).getShortDescription());

                // reinit exoplayer
                reInitPlayer(steps.get(stepId).getVideoURL());

            } else {
                // go back to recipe detail
                if (getFragmentManager().getBackStackEntryCount() > 0 && checkIfLandscape() == false) {
                    getFragmentManager().popBackStack();
                }
            }

        }
    }

    public void reInitPlayer(String url) {
        if (mExoPlayer != null) {
            releasePlayer();
        }

        initializeMediaSession();
        initializePlayer(Uri.parse(url));
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
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "Baking Time");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            // go to previous seekbar position
            if (playerPosition != C.TIME_UNSET){ mExoPlayer.seekTo(playerPosition); }
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false); // pause player
        }
    }

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }

    /*Release ExoPlayer*/
    private void releasePlayer() {

        // destroy notification when activity is destroyed
        //mNotificationManager.cancelAll();
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;

    }
    /*
     * NOTE: we initialize the player either in onStart or onResume according to API level
     * API level 24 introduced support for multiple windows to run side-by-side. So it's safe to initialize our player in onStart
     * more on Multi-Window Support here https://developer.android.com/guide/topics/ui/multi-window.html
     * Before API level 24, we wait as long as onResume (to grab system resources) before initializing player
     *
     * source for above comment: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 || videoUri != null) {
                initializeMediaSession();
                initializePlayer(Uri.parse(videoUri));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || videoUri != null) {
            initializeMediaSession();
            initializePlayer(Uri.parse(videoUri));
        }
    }


    /*
     * Before API level 24 we release player resources early
     * because there is no guarantee of onStop being called before the system terminates our app
     * remember onPause means the activity is partly obscured by something else (e.g. incoming call, or alert dialog)
     * so we do not want to be playing media while our activity is not in the foreground.
     *
     * source for above comment: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c
     */
    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT <= 23 || mExoPlayer != null) {
            //get playback position in milliseconds, needed to save the
            // seekbar position in onSavedInstanceState
            playerPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.setPlayWhenReady(false); // pause player
            releasePlayer();
        }
    }

    /*
     API level 24+ we release the player resources when the activity is no longer visible (onStop)
     NOTE: On API 24+, onPause is still visible!!! So we do not not want to release the player resources
     this is made possible by the new Android Multi-Window Support https://developer.android.com/guide/topics/ui/multi-window.html
     We stop playing media on API 24+ only when our activity is no longer visible aka onStop

     source for above comment: https://gist.github.com/codeshifu/c26bb8a5f27f94d73b3a4888a509927c
     */
    @Override
    public void onStop() {
        super.onStop();

        if(Util.SDK_INT <= 23 || mExoPlayer != null) {
            releasePlayer();
        }

    }



    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Shows Media Style notification, with an action that depends on the current MediaSession
     * PlaybackState.
     * @param state The PlaybackState of the MediaSession.
     */
    @SuppressWarnings("deprecation")
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());

        int icon;
        String play_pause;
        if(state.getState() == PlaybackStateCompat.STATE_PLAYING){
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new androidx.core.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getContext(), 0, new Intent(getContext(), RecipeStepFragment.class), 0);

        builder.setContentTitle("test")
                .setContentText("This is a test")
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.ic_music_note)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(restartAction)
                .addAction(playPauseAction);
//                .setStyle(new NotificationCompat.MediaStyle()
//                        .setMediaSession(mMediaSession.getSessionToken())
//                        .setShowActionsInCompactView(0,1));


        mNotificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
