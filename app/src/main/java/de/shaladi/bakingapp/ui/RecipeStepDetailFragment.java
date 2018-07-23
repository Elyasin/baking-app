package de.shaladi.bakingapp.ui;

import android.app.Dialog;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Locale;

import de.shaladi.bakingapp.R;
import de.shaladi.bakingapp.model.IngredientParcelable;

public class RecipeStepDetailFragment extends Fragment {


    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();


    public static final String step_description_key = "step_description_key";
    public static final String video_url_key = "video_url_key";
    public static final String list_of_ingredients_key = "list_of_ingredients_key";
    public static final String step_number_key = "step_number_key";


    private final String PLAY_WHEN_READY_KEY = "play_when_ready_key";
    private final String CURRENT_WINDOW_KEY = "current_window_key";
    private final String PLAYBACK_POSITION_KEY = "playback_position_key";

    //private int mRecipeId;
    private int mStepNo;
    private String mStepDescription;
    private String mVideoUrl;
    private ArrayList<IngredientParcelable> mIngredients;

    private PlayerView mPlayerView;
    private SimpleExoPlayer mSimpleExoPlayer;

    private boolean mPlayWhenReady = true;
    private int mCurrentWindow = 0;
    private long mPlaybackPosition = 0;


    private Dialog mFullScreenDialog;
    private boolean mExoPlayerFullscreen = false;
    private ImageView mFullScreenIcon;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getActivity() != null;

        if (savedInstanceState == null) {
            Bundle bundle = getArguments();

            //get ingredients or step description and video/thumbnail url
            if (bundle != null &&
                    bundle.containsKey(step_description_key) &&
                    bundle.containsKey(list_of_ingredients_key) &&
                    bundle.containsKey(video_url_key) &&
                    bundle.containsKey(step_number_key)) {

                mStepDescription = bundle.getString(step_description_key);
                mVideoUrl = bundle.getString(video_url_key);
                mIngredients = bundle.getParcelableArrayList(list_of_ingredients_key);
                mStepNo = bundle.getInt(step_number_key);
            } else {
                Log.e(TAG, "Bundle is null or expected keys are not present");
                throw new IllegalArgumentException("Bundle is null or expected keys are not present");
            }

        } else {
            mStepDescription = savedInstanceState.getString(step_description_key);
            mVideoUrl = savedInstanceState.getString(video_url_key);
            mIngredients = savedInstanceState.getParcelableArrayList(list_of_ingredients_key);
            mStepNo = savedInstanceState.getInt(step_number_key);

            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
            mCurrentWindow = savedInstanceState.getInt(CURRENT_WINDOW_KEY);
            mPlaybackPosition = savedInstanceState.getLong(PLAYBACK_POSITION_KEY);
        }


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert getActivity() != null;

        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        //can be null on tablet landscape mode
        TextView recipeStepDetailTextView = rootView.findViewById(R.id.recipe_step_detail);
        View noVideoView = rootView.findViewById(R.id.no_video_view);
        mPlayerView = rootView.findViewById(R.id.player_view);

        if (recipeStepDetailTextView != null) {

            if (mStepNo == 0) {
                for (int i = 0; i < mIngredients.size(); i++) {
                    String ingredient = mIngredients.get(i).getIngredient();
                    String measure = mIngredients.get(i).getMeasure();
                    float quantity = mIngredients.get(i).getQuantity();
                    String text;
                    if (quantity == (long) quantity)
                        text = String.format(Locale.ENGLISH, "%d %s of %s", (long) quantity, measure, ingredient);
                    else
                        text = String.format(Locale.ENGLISH, "%s %s of %s", quantity, measure, ingredient);
                    recipeStepDetailTextView.append(text + "\n");
                }
            } else {
                recipeStepDetailTextView.setText(mStepDescription);
            }

        }

        if (TextUtils.isEmpty(mVideoUrl)) {
            noVideoView.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        } else {
            noVideoView.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE &&
                !(getResources().getConfiguration().screenWidthDp >= 600)) {
            hideSystemUi();
        }

        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        outState.putString(step_description_key, mStepDescription);
        outState.putString(video_url_key, mVideoUrl);
        outState.putParcelableArrayList(list_of_ingredients_key, mIngredients);
        outState.putInt(step_number_key, mStepNo);

        //No guarantee where in the lifecycle onSaveInstanceState is called
        //check onPause (release of player), onStop (release of player), onSaveInstanceState
        if (mSimpleExoPlayer != null) {
            outState.putBoolean(PLAY_WHEN_READY_KEY, mSimpleExoPlayer.getPlayWhenReady());
            outState.putInt(CURRENT_WINDOW_KEY, mSimpleExoPlayer.getCurrentWindowIndex());
            outState.putLong(PLAYBACK_POSITION_KEY, mSimpleExoPlayer.getCurrentPosition());
        } else {
            outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
            outState.putInt(CURRENT_WINDOW_KEY, mCurrentWindow);
            outState.putLong(PLAYBACK_POSITION_KEY, mPlaybackPosition);
        }
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (!TextUtils.isEmpty(mVideoUrl))
                initializePlayer(mVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mSimpleExoPlayer == null)) {
            if (!TextUtils.isEmpty(mVideoUrl))
                initializePlayer(mVideoUrl);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ||
                getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            initFullscreenDialog();
            initFullscreenButton();
            if (mExoPlayerFullscreen) {
                ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
                mFullScreenDialog.addContentView(
                        mPlayerView,
                        new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)
                );
                assert getActivity() != null;
                mFullScreenIcon.setImageDrawable(
                        ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_black_24dp)
                );
                mFullScreenDialog.show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }


    //player view init and release

    private void initializePlayer(String videoUrlString) {
        mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getActivity()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mSimpleExoPlayer);

        //if fragment not visible ensure no video starts playing
        if (!this.isVisible()) mPlayWhenReady = false;
        mSimpleExoPlayer.setPlayWhenReady(mPlayWhenReady);
        mSimpleExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

        Uri uri = Uri.parse(videoUrlString);
        MediaSource mediaSource = new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-baking-app")).
                createMediaSource(uri);

        mSimpleExoPlayer.prepare(mediaSource, false, false);
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
            mCurrentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
            mPlaybackPosition = mSimpleExoPlayer.getCurrentPosition();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }


    private void hideSystemUi() {
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    //Full screen dialog methods

    private void initFullscreenDialog() {

        assert getActivity() != null;
        mFullScreenDialog = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };
    }

    private void openFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        mFullScreenDialog.addContentView(mPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        assert getActivity() != null;
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_exit_black_24dp));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void closeFullscreenDialog() {
        ((ViewGroup) mPlayerView.getParent()).removeView(mPlayerView);
        assert getActivity() != null;
        ((FrameLayout) getActivity().findViewById(R.id.media_frame)).addView(mPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_fullscreen_black_24dp));
    }

    private void initFullscreenButton() {

        PlayerControlView controlView = mPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        FrameLayout fullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        fullScreenButton.setOnClickListener(v -> {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
    }


}
