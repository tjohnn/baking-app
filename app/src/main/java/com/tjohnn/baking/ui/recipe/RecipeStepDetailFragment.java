package com.tjohnn.baking.ui.recipe;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.tjohnn.baking.App;
import com.tjohnn.baking.R;
import com.tjohnn.baking.data.dto.Step;
import com.tjohnn.baking.util.AppScheduler;
import com.tjohnn.baking.util.Provider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepDetailFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "ARG_RECIPE_ID";
    public static final String ARG_STEP_INDEX = "ARG_STEP_INDEX";
    public static final String ARG_VIDEO_PLAY_TIME = "ARG_VIDEO_PLAY_TIME";
    public static final String ARG_PLAY_WHEN_READY = "ARG_PLAY_WHEN_READY";

    private long mItemIndex;
    private long mItemId;

    private List<Step> mItems = new ArrayList<>();
    private RecipeStepDetailViewModel mViewModel;

    @BindView(R.id.simple_player_view)
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;


    @BindView(R.id.step_image)
    ImageView stepImageView;


    @BindView(R.id.tv_description)
    TextView descriptionView;
    private long currentPlayTime;
    private boolean playWhenReady = true;


    public RecipeStepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_INDEX)) {
            mItemIndex = getArguments().getLong(ARG_STEP_INDEX);

        }
        if (getArguments().containsKey(ARG_RECIPE_ID)) {
            mItemId = getArguments().getLong(ARG_RECIPE_ID);
        }

        if(savedInstanceState != null){
            mItemIndex = savedInstanceState.getLong(ARG_STEP_INDEX);
            currentPlayTime = savedInstanceState.getLong(ARG_VIDEO_PLAY_TIME);
            mItemId = savedInstanceState.getLong(ARG_RECIPE_ID);
            playWhenReady = savedInstanceState.getBoolean(ARG_PLAY_WHEN_READY);
        }

        mViewModel = ViewModelProviders.of(this,
                new RecipeStepDetailViewModel.ViewModelFactory(App.getInstance(),
                        Provider.getRecipeRepository(), mItemId, AppScheduler.getInstance()))
                .get(RecipeStepDetailViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        mViewModel.loadRecipeById();

        subscribeToViewModel();

        return rootView;
    }

    private void subscribeToViewModel() {
        if(mViewModel == null)  return;
        mViewModel.getRecipe().observe(this, d -> {
            if(d == null || d.steps == null) return;
            mItems = d.steps;
            bindData();
        });
        mViewModel.getToaster().observe(this, s -> {
            if(s == null || s.getContentIfNotUsed() == null) return;
            Toast.makeText(getActivity(), s.peekContent(), Toast.LENGTH_SHORT).show();
        });
    }

    private void bindData() {
        Step item = mItems.get((int)mItemIndex);
        descriptionView.setText(item.description);
        setupVideoPlayer(item);
        getActivity().setTitle(String.format(getString(R.string.step_index), mItemIndex + 1));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && mItems.size() > 0) {
            setupVideoPlayer(mItems.get((int)mItemIndex));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || simpleExoPlayer == null) && mItems.size() > 0) {
            setupVideoPlayer(mItems.get((int)mItemIndex));
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_RECIPE_ID, mItemId);
        outState.putLong(ARG_STEP_INDEX, mItemIndex);
        if(simpleExoPlayer != null) {
            outState.putBoolean(ARG_PLAY_WHEN_READY, simpleExoPlayer.getPlayWhenReady());
            outState.putLong(ARG_VIDEO_PLAY_TIME, simpleExoPlayer.getCurrentPosition());
            super.onSaveInstanceState(outState);
        }
        else{
            outState.putBoolean(ARG_PLAY_WHEN_READY, playWhenReady);
            outState.putLong(ARG_VIDEO_PLAY_TIME, currentPlayTime);
        }
    }

    private void setupVideoPlayer(Step item) {
        if(item.videoUrl == null || item.videoUrl.isEmpty()){
            if(item.thumbnailUrl != null && !item.thumbnailUrl.isEmpty())
                Picasso.with(getActivity())
                        .load(item.thumbnailUrl)
                        .error(R.drawable.no_image)
                        .into(stepImageView);
            else
                stepImageView.setImageResource(R.drawable.no_image);
            stepImageView.setVisibility(View.VISIBLE);

            return;
        }
        stepImageView.setVisibility(View.INVISIBLE);

        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity());
        playerView.setPlayer(simpleExoPlayer);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                Util.getUserAgent(getActivity(), getString(R.string.app_name)));
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .setExtractorsFactory(new DefaultExtractorsFactory())
                .createMediaSource(Uri.parse(item.videoUrl));
        simpleExoPlayer.prepare(videoSource);
        simpleExoPlayer.seekTo(currentPlayTime);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    @OnClick(R.id.btn_previous)
    void previousStep(){
        clearVideoResource();
        if(mItemIndex == 0){
            Toast.makeText(getActivity(), R.string.no_previous_video, Toast.LENGTH_SHORT).show();
            return;
        }
        mItemIndex--;
        currentPlayTime = 0;
        bindData();
    }

    @OnClick(R.id.btn_next)
    void nextStep(){
        clearVideoResource();
        if(mItemIndex == mItems.size() - 1){
            Toast.makeText(getActivity(), R.string.no_videos_to_play, Toast.LENGTH_SHORT).show();
            return;
        }
        currentPlayTime = 0;
        mItemIndex++;
        bindData();
    }

    private void clearVideoResource() {

        if(simpleExoPlayer != null) {
            currentPlayTime = simpleExoPlayer.getCurrentPosition();
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
        }
        simpleExoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            clearVideoResource();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            clearVideoResource();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearVideoResource();
    }
}
