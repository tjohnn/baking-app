package com.tjohnn.baking.ui.recipe;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tjohnn.baking.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeStepsActivityTest {

    @Rule
    public IntentsTestRule<RecipeStepsActivity> mActivityRule = new IntentsTestRule<>(RecipeStepsActivity.class);

    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(mActivityRule.getActivity().getIdlingResource());
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void ingredientOptionClicked_shouldToggleIngredientWrapper() {
        onView(withId(R.id.ingredients)).perform(click());

        onView(withId(R.id.ingredients_container)).check(matches(isDisplayed()));

        onView(withId(R.id.ingredients)).perform(click());

        onView(withId(R.id.ingredients_container)).check(matches(not(isDisplayed())));
    }


    @Test
    public void stepItemClicked_shouldDisplayVideo() {
        onView(withId(R.id.recipe_step_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.simple_player_view)).check(matches(isDisplayed()));

    }



}