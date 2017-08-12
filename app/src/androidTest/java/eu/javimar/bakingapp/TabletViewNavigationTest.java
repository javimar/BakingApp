package eu.javimar.bakingapp;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
public class TabletViewNavigationTest
{

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void tabletViewTest()
    {
        // Clicks on a RecyclerView recipe item
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_recipes_main), isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        // Clicks on a RecyclerView step item and shows detail fragment
        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.rv_recipe_steps),
                        withParent(withId(R.id.recipe_detail_fragment)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(2, click()));

        // Click on the play button of Exoplayer
        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.exo_play),
                        withContentDescription("Image for the play button in the video player"),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        // check it shows
        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.rv_recipe_steps),
                                childAtPosition(
                                        withId(R.id.recipe_detail_fragment),
                                        1)),
                        0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position)
    {
        return new TypeSafeMatcher<View>()
        {
            @Override
            public void describeTo(Description description)
            {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view)
            {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
