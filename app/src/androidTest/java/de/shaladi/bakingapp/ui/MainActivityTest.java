package de.shaladi.bakingapp.ui;


import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.shaladi.bakingapp.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    //public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    public IntentsTestRule<MainActivity> mainActivityIntentsTestRule = new IntentsTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTestFirstItem() {
        onView(allOf(withId(R.id.recipe_name), withText("Nutella Pie"), isDisplayed()));
        onView(allOf(withId(R.id.recipe_name_label), withText("Reicpe name"), isDisplayed()));
        onView(allOf(withId(R.id.servings_label), withText("Servings"), isDisplayed()));
        onView(allOf(withId(R.id.servings), withText("8"), isDisplayed()));

        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click())
        );

        intended(allOf(
                hasComponent(RecipeStepsListActivity.class.getName()),
                hasExtra(MainActivity.RECIPE_ID_KEY, 1)
        ));

    }

}
