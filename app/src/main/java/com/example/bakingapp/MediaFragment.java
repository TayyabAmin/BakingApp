package com.example.bakingapp;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tayyab on 8/4/2017.
 */

public class MediaFragment extends Fragment implements View.OnClickListener {

    private SimpleExoPlayer mExoPlayer;
    private ArrayList<StepsData> mStepsList;
    private int stepPosition;
    private FragmentMediaBinding mediaBinding;
    private long playerState = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mediaBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media, container, false);

        mStepsList = getArguments().getParcelableArrayList("step_data");
        stepPosition = getArguments().getInt("position");

        if (!mStepsList.get(stepPosition).getThumbUrl().equals("")) {
            try {
                Picasso.with(getActivity())
                        .load(mStepsList.get(stepPosition).getThumbUrl())
                        .placeholder(R.drawable.appwidget)
                        .error(R.drawable.appwidget)
                        .into(mediaBinding.mediaImg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mediaBinding.mediaDescTxt.setText(mStepsList.get(stepPosition).getDetailDesc());

        if (savedInstanceState != null) {
            playerState = savedInstanceState.getLong("player");
        }
        initializePlayer(Uri.parse(mStepsList.get(stepPosition).getVideoUrl()));

        mediaBinding.previousIv.setOnClickListener(this);
        mediaBinding.nextIv.setOnClickListener(this);
        return mediaBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putLong("player", playerState);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("value", "value");
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mediaBinding.playerView.setPlayer(mExoPlayer);

            if (playerState != 0) {
                mExoPlayer.seekTo(playerState);
            }

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
    public void onPause() {
        super.onPause();
        playerState = mExoPlayer.getCurrentPosition();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
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
