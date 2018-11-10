package com.tjohnn.baking.ui.recipe;

import android.content.Intent;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tjohnn.baking.Mocks;
import com.tjohnn.baking.R;
import com.tjohnn.baking.data.dto.Step;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class RecipeStepDetailActivityTest {

    // mock a list of steps that matches with the steps in the firs recipe
    private List<Step> steps = Mocks.mockStepsId1();

    @Rule
    public ActivityTestRule<RecipeStepDetailActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeStepDetailActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        Intent intent = new Intent();
        // send id of the first recipe as recipe_id
        intent.putExtra(RecipeStepDetailFragment.ARG_RECIPE_ID, 1L);
        intent.putExtra(RecipeStepDetailFragment.ARG_STEP_INDEX, 0L);
        mActivityTestRule.launchActivity(intent);
        IdlingRegistry.getInstance().register(mActivityTestRule.getActivity().getIdlingResource());
    }

    @Test
    public void confirmDescriptionPageHasDescriptionTextOfFirstStep() {
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()));
    }

    @Test
    public void navButtonClicked_shouldChangeDescriptionText() {
        onView(withId(R.id.btn_next)).perform(click());

        onView(withText(steps.get(1).description)).check(matches(isDisplayed()));

        onView(withId(R.id.btn_previous)).perform(click());

        onView(withText(steps.get(0).description)).check(matches(isDisplayed()));
    }
}