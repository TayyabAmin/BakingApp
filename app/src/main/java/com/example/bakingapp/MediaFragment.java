package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bakingapp.databinding.FragmentMediaBinding;
import com.example.model.StepsData;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

/**
 * Created by Tayyab on 8/4/2017.
 */

public class MediaFragment extends Fragment implements View.OnClickListener {

    private SimpleExoPlayer mExoPlayer;
//    private SimpleExoPlayerView mPlayerView;
//    private TextView mediaDesc;
    private ArrayList<StepsData> mStepsList;
//    private ImageView previousIcon, nextIcon;
    private int stepPosition;
    private FragmentMediaBinding mediaBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mediaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false);

//        StepsData stepsData = new StepsData();
        mStepsList = getArguments().getParcelableArrayList("step_data");

        stepPosition = getArguments().getInt("position");

//        previousIcon = (ImageView) rootView.findViewById(R.id.previous_iv);
//        nextIcon = (ImageView) rootView.findViewById(R.id.next_iv);
//        mediaDesc = (TextView) rootView.findViewById(R.id.media_desc_txt);
        mediaBinding.mediaDescTxt.setText(mStepsList.get(stepPosition).getDetailDesc());

        // Initialize the player view.
//        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.playerView);

        initializePlayer(Uri.parse(mStepsList.get(stepPosition).getVideoUrl()));

        mediaBinding.previousIv.setOnClickListener(this);
        mediaBinding.nextIv.setOnClickListener(this);
        return mediaBinding.getRoot();
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mediaBinding.playerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_iv:
                if (stepPosition < mStepsList.size() - 1) {
                    stepPosition++;
                    mediaBinding.mediaDescTxt.setText(mStepsList.get(stepPosition).getDetailDesc());

                    releasePlayer();
                    initializePlayer(Uri.parse(mStepsList.get(stepPosition).getVideoUrl()));
                }
                break;
            case R.id.previous_iv:
                if (stepPosition > 0) {
                    stepPosition--;
                    mediaBinding.mediaDescTxt.setText(mStepsList.get(stepPosition).getDetailDesc());
                    releasePlayer();
                    initializePlayer(Uri.parse(mStepsList.get(stepPosition).getVideoUrl()));
                }
                break;
        }
    }
}
