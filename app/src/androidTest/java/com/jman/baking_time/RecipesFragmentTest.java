package com.jman.baking_time;

import com.jman.baking_time.models.Ingredient;
import com.jman.baking_time.models.Recipe;
import com.jman.baking_time.models.Step;
import com.jman.baking_time.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class RecipesFragmentTest {

//    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule
//            = new ActivityTestRule<>(MainActivity.class);
    @Rule
    public IntentsTestRule<MainActivity> mainActivityTestRule = new IntentsTestRule<>(MainActivity.class);


    private IdlingResource mIdlingResource;

    @Before
    public void initFragment(){

        //init mIdlingResource
        mIdlingResource = mainActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void checkRecipeDetails() {

        // check Nutella Pie shows in the view
        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText("Nutella Pie")).check(matches(isDisplayed()));

        // check Brownies shows in the view
        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText("Brownies")).check(matches(isDisplayed()));

        // check Yellow Cake shows in the view
        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText("Yellow Cake")).check(matches(isDisplayed()));

        // check Cheesecake shows in the view
        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }

    @Test
    public void navigateToDetailActivityOnClick() {

        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.recipeIngredients_RecyclerView))
                .perform(RecyclerViewActions.scrollToPosition(0));

        // check
        onView(withText("2 CUP Graham Cracker crumbs")).check(matches(isDisplayed()));
    }

    @Test
    public void validateIntentSentToPackage() {
        Recipe recipeDetail = new Recipe(
                new ArrayList<Ingredient>(), new ArrayList<Step>()
        );
        onView(withId(R.id.recipeList_RecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(toPackage("com.jman.baking_time.ui.RecipeDetailHostActivity"));
        intended(hasExtra("recipeDetails", recipeDetail));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
