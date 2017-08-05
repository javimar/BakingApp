package eu.javimar.bakingapp;

import android.annotation.TargetApi;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.javimar.bakingapp.model.Step;
import eu.javimar.bakingapp.view.RecipeStepDetailViewFragment;

import static eu.javimar.bakingapp.RecipeDetailViewActivity.RECIPE_NAME_TAG;
import static eu.javimar.bakingapp.RecipeDetailViewActivity.STEP_ARRAY_TAG;
import static eu.javimar.bakingapp.RecipeDetailViewActivity.STEP_ITEM_PARCEABLE_TAG;


/**
 *  RecipeStepDetailViewActivity is never used on a tablet. It is a container to
 *  present RecipeStepDetailViewFragment, so it is only used on phones where the
 *  two fragments are displayed separately.
 */
public class RecipeStepDetailViewActivity extends AppCompatActivity
{
    private static String LOG_TAG = RecipeStepDetailViewActivity.class.getName();

    public static String STEP_ITEM_PARCEABLE_TAG_ONCLICK = "step_onclick";

    @BindView(R.id.view_pager)ViewPager mViewPager;
    PagerAdapter mPagerAdapter;

    ArrayList<Step> mStepList = new ArrayList<>();
    Step mStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_detail_fragment);

        ButterKnife.bind(this);

        // Get extras from Activity
        setTitle(getIntent().getStringExtra(RECIPE_NAME_TAG));
        mStepList = getIntent().getParcelableArrayListExtra(STEP_ARRAY_TAG);
        mStep = getIntent().getParcelableExtra(STEP_ITEM_PARCEABLE_TAG);

        // Send the step to the fragment only when clicking in the step list
        // ?????




        // Set up View Pager to swipe horizontally through the Steps
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
    }

    private class PagerAdapter extends FragmentStatePagerAdapter {
        PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = new RecipeStepDetailViewFragment();
            Bundle args = new Bundle();
            // Send the correct Step
            args.putParcelable(STEP_ITEM_PARCEABLE_TAG, mStepList.get(position));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return mStepList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.detail_step_pager_strip)
                    + (position);
        }
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE)
        {
            View view = findViewById(android.R.id.content);
            if (hasFocus)
            {
                view.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }


    /** Prevent up buttom to recreate caller activity, force back button behavior */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
