package eu.javimar.bakingapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import eu.javimar.bakingapp.model.Recipe;
import eu.javimar.bakingapp.model.Step;
import eu.javimar.bakingapp.view.RecipeDetailViewFragment;
import eu.javimar.bakingapp.view.RecipeStepDetailViewFragment;

import static eu.javimar.bakingapp.MainActivity.RECIPE_ITEM_PARCEABLE;


public class RecipeDetailViewActivity extends AppCompatActivity
        implements RecipeDetailViewFragment.OnItemStepSelectedListener
{
    private static String LOG_TAG = RecipeDetailViewActivity.class.getName();

    RecipeDetailViewFragment mRecipeDetailViewFragment;

    public static String STEP_ITEM_PARCEABLE_TAG = "step_item_parceable";
    public static String RECIPE_NAME_TAG = "recipe_name";
    public static String STEP_ARRAY_TAG = "step_array";

    private Recipe mRecipe;

    // Detect if we are on a phone or a tablet
    public static boolean sDualFragments = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // display static fragment since it is the same for phones and tablets
        setContentView(R.layout.recipe_detail_fragment);

        // retrieve recipe from MainActivity
        mRecipe = getIntent().getParcelableExtra(RECIPE_ITEM_PARCEABLE);

        // set title with name of recipe
        setTitle(mRecipe.getmRecipeName());

        // Take the info from the intent and deliver it to the fragment so it can update
        mRecipeDetailViewFragment = (RecipeDetailViewFragment)
                getFragmentManager().findFragmentById(R.id.recipe_detail_view_fragment);
        mRecipeDetailViewFragment.displayRecipeDetail(mRecipe);
        // Set the listener to receive Step clicked
        mRecipeDetailViewFragment.setItemStepListener(this);

        // Check if fragment step detail view is active and visible
        View stepDetailsFrame = findViewById(R.id.recipe_step_detail_view_fragment);
        sDualFragments = stepDetailsFrame != null && stepDetailsFrame.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onItemStepSelected(Step step)
    {
Log.e(LOG_TAG, "JAVIER step= " + step.getmStepId());

        if(step != null)
        {
            if (!sDualFragments) // phone view
            {
                Intent intent = new Intent(this, RecipeStepDetailViewActivity.class);
                // pass the step arraylist, the step, and the recipe name for the title
                intent.putExtra(STEP_ITEM_PARCEABLE_TAG, step);
                intent.putParcelableArrayListExtra
                        (STEP_ARRAY_TAG,
                                (ArrayList<? extends Parcelable>) mRecipe.getmStepsList());
                intent.putExtra(RECIPE_NAME_TAG, mRecipe.getmRecipeName());
                startActivity(intent);
            }
            else // tablet view
            {
                RecipeStepDetailViewFragment mFragmentStepDetail =
                        (RecipeStepDetailViewFragment)getSupportFragmentManager()
                        .findFragmentById(R.id.recipe_step_detail_view_fragment);
                if (mFragmentStepDetail == null) // first time on click is null
                {
                    // add fragment programmatically to the framelayout
                    mFragmentStepDetail = new RecipeStepDetailViewFragment();
                    mFragmentStepDetail.displayStepDetailView(step);

                    getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.recipe_step_detail_view_fragment, mFragmentStepDetail)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                }
                mFragmentStepDetail.displayStepDetailView(step);
            }
        }
    }



}
