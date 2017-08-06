package eu.javimar.bakingapp.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.javimar.bakingapp.R;
import eu.javimar.bakingapp.model.Step;

import static eu.javimar.bakingapp.RecipeDetailViewActivity.STEP_ITEM_PARCEABLE_TAG;
import static eu.javimar.bakingapp.RecipeDetailViewActivity.sDualFragments;
import static eu.javimar.bakingapp.RecipeStepDetailViewActivity.STEP_ITEM_PARCEABLE_TAG_ONCLICK;
import static eu.javimar.bakingapp.utils.HelperUtils.isNetworkAvailable;


public class RecipeStepDetailViewFragment extends Fragment implements
        ExoPlayer.EventListener
{
    private static String TAG = RecipeStepDetailViewFragment.class.getName();

    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_detail_description) TextView mTvStep;
    @BindView(R.id.iv_no_video) ImageView mIvNoVideo;

    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat sMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private Step mStep;


    public RecipeStepDetailViewFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
        {
            // Restore last state if activity was destroyed on a creen rotation
            mStep = savedInstanceState.getParcelable(STEP_ITEM_PARCEABLE_TAG);
        }

        // This is only executed on handsets
        if(!sDualFragments)
        {
            Bundle args = getArguments();
            mStep = args.getParcelable(STEP_ITEM_PARCEABLE_TAG);
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.recipe_step_detail_view, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        // save step when rotate the screen
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_ITEM_PARCEABLE_TAG, mStep);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Set the correct Step information on the card
        mTvStep.setText(mStep.getmDescription());

        if(!isNetworkAvailable(getActivity()))
        {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            // one of the two, if not, url will be empty.
            String url = mStep.getmVideoUrl();
            if(url.isEmpty())
                url = mStep.getmThumbnailUrl();

            if(url == null || url.isEmpty())
            {
                mPlayerView.setVisibility(View.GONE);
                mIvNoVideo.setVisibility(View.VISIBLE);
            }
            else
            {
                // initialize the Media Session
                initializeMediaSession();
                // show player
                mPlayerView.setVisibility(View.VISIBLE);
                mIvNoVideo.setVisibility(View.GONE);
                // initialize the player
                initializePlayer(Uri.parse(url));
            }
        }

    }


    // Step info is going to be provided in here only in tablet view
    public void displayStepDetailView(Step step)
    {
        mStep = step;
    }


    private void initializeMediaSession()
    {
        // Create a MediaSessionCompat.
        sMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        sMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        sMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        sMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        sMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        sMediaSession.setActive(true);
    }


    private void initializePlayer(Uri mediaUri)
    {
        if (mExoPlayer == null)
        {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(),
                    trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Recipes");
            MediaSource mediaSource =
                    new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                            getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(false);
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


    /**
     * Method that is called when the ExoPlayer state changes. Used to update the MediaSession
     * PlayBackState to keep in sync, and post the media notification.
     * @param playWhenReady true if ExoPlayer is playing, false if it's paused.
     * @param playbackState int describing the state of ExoPlayer. Can be STATE_READY, STATE_IDLE,
     *                      STATE_BUFFERING, or STATE_ENDED.
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState)
    {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady)
        {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        else if((playbackState == ExoPlayer.STATE_READY))
        {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        sMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }


    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback
    {
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


    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver
    {
        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent)
        {
            MediaButtonReceiver.handleIntent(sMediaSession, intent);
        }
    }


    /**
     * Release the player when the fragment is destroyed.
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        releasePlayer();
        sMediaSession.setActive(false);
    }


    /**
     * Release ExoPlayer.
     */
    private void releasePlayer()
    {
        if (mExoPlayer != null)
        {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
