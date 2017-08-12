package eu.javimar.bakingapp;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest
{
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Clicks on a RecyclerView recipe item and checks that it opens RecipeDetailViewActivity
     */
    @Test
    public void clickRecyclerViewRecipe_OpensRecipeDetailViewActivity()
    {
        // Reference to the recycler view item and click it
        onView(withId(R.id.rv_recipes_main))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Checks that the RecipeDetailViewActivity opens correctly
        onView(withId(R.id.recipe_detail_fragment)).check(matches(isDisplayed()));
    }


}
