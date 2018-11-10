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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepDetailFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "ARG_RECIPE_ID";
    public static final String ARG_STEP_INDEX = "ARG_STEP_INDEX";
    public static final String ARG_VIDEO_PLAY_TIME = "ARG_VIDEO_PLAY_TIME";

    private long mItemIndex;
    private long mItemId;

    private List<Step> mItems;
    private RecipeStepDetailViewModel mViewModel;

    @BindView(R.id.simple_player_view)
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;


    @BindView(R.id.step_image)
    ImageView stepImageView;


    @BindView(R.id.tv_description)
    TextView descriptionView;
    private long currentPlayTime;


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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_RECIPE_ID, mItemId);
        outState.putLong(ARG_STEP_INDEX, mItemIndex);
        if(simpleExoPlayer != null) outState.putLong(ARG_VIDEO_PLAY_TIME, simpleExoPlayer.getDuration());
        super.onSaveInstanceState(outState);
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
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(currentPlayTime);
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
        if(simpleExoPlayer != null) simpleExoPlayer.release();
    }

    @Override
    public void onStop() {
        clearVideoResource();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
