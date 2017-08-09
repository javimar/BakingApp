package eu.javimar.bakingapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import eu.javimar.bakingapp.model.Recipe;
import eu.javimar.bakingapp.model.Step;
import eu.javimar.bakingapp.view.RecipeDetailViewFragment;
import eu.javimar.bakingapp.view.RecipeStepDetailViewFragment;

import static eu.javimar.bakingapp.MainActivity.RECIPE_ITEM_PARCEABLE;

/**
 * This activity already has a Recipe selected, and will decide whether we are using
 * a handset or a tablet device, passing the necessary info to the activity (handset)
 * or creating a detail fragment for tablets, that will display a list of ingredients
 * and a list of steps to bake the recipe
 */
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

        // deliver Recipe to the left fragment so it can update
        mRecipeDetailViewFragment = (RecipeDetailViewFragment)
                getFragmentManager().findFragmentById(R.id.recipe_detail_fragment);
        mRecipeDetailViewFragment.displayRecipeDetail(mRecipe);
        // set the listener to receive the correct Step being clicked
        mRecipeDetailViewFragment.setItemStepListener(this);

        // check if fragment (on the right in tablets) step detail view is active and visible
        View stepDetailsFrame = findViewById(R.id.recipe_step_detail_fragment);
        sDualFragments = stepDetailsFrame != null &&
                stepDetailsFrame.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onItemStepSelected(Step step)
    {
        if(step != null)
        {
            if (!sDualFragments) // handset view
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
                RecipeStepDetailViewFragment fragmentStepDetail =
                        (RecipeStepDetailViewFragment)getSupportFragmentManager()
                                .findFragmentById(R.id.recipe_step_detail_fragment);
                if(fragmentStepDetail == null)
                {
                    // hide the empty step textview
                    findViewById(R.id.text_empty_detail).setVisibility(View.GONE);
                }
                // add fragment programmatically to the framelayout
                fragmentStepDetail =
                        new RecipeStepDetailViewFragment();
                // pass the correct step to visualize
                fragmentStepDetail.displayStepDetailView(step);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipe_step_detail_fragment, fragmentStepDetail)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        }
    }



}
