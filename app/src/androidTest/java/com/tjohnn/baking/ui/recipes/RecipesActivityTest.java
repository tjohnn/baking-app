package com.tjohnn.baking.ui.recipes;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tjohnn.baking.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);


    @Before
    public void setUp() throws Exception {
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }


    @Test
    public void clickGridViewItem_OpensActivity() {

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the StepsActivity opens with steps recycler view displayed
        onView(withId(R.id.recipe_step_list)).check(matches(isDisplayed()));


    }

}