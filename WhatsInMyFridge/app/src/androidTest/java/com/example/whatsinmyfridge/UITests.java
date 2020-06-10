package com.example.whatsinmyfridge;

import android.content.Context;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.equalTo;

/**
 * These are just simple UI Tests to check whether the ingredient add/drop and navigation to the recipes 
 * is actually working
 */
public class UITests {
    @Rule
    public ActivityTestRule<LoggedInActivity> homeRule = new ActivityTestRule<>(LoggedInActivity.class);

    // Because we are following a PROCESS which starts by inserting the Ingredients and finishes with the
    // navigation into the search activity, I have put it all into a big test, instead of separating it into
    // small ones. For me the atomic unit of functionality is represented by the process as a whole.
    @Test
    public void homeUITest() throws Exception {
        // Typing Sauce as ingredient
        onView(withId(R.id.auto_complete_input)).perform(typeText("Sauce")).check(matches(isDisplayed()));
        // Adding the ingredient
        onData(equalTo("Sauce")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.button_add))
                .perform(click()).check(matches(isDisplayed()));
        // Waiting (symbolizes real life user interaction)
        SystemClock.sleep(1000);

        // Repeating above steps for Noodles
        onView(withId(R.id.auto_complete_input)).perform(typeText("Noodles")).check(matches(isDisplayed()));
        onData(equalTo("Noodles")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.button_add))
                .perform(click()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // Then doing the same for Eggs
        onView(withId(R.id.auto_complete_input)).perform(typeText("Eggs")).check(matches(isDisplayed()));
        onData(equalTo("Eggs")).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(withId(R.id.button_add))
                .perform(click()).check(matches(isDisplayed()));
        SystemClock.sleep(1000);

        // As the keyboard is blocking the screen, we have to press back once
        Espresso.pressBack();

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);
        onView(withId(R.id.recyclerViewHomeFragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, CustomViewAction.clickChildViewWithId(R.id.imageDelete)));

        // Then we are displaying more information about the first ingredient in the list
        SystemClock.sleep(1000);
        onView(withId(R.id.recyclerViewHomeFragment))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, CustomViewAction.clickChildViewWithId(R.id.ingredient_image)));

        // Now the tricky part begins
        SystemClock.sleep(1000);
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        // Because it is not possible to exit the dialog with a press of the back button, I had to
        // get the screen resolution to manually tell the app to click it away
        WindowManager wm = (WindowManager) homeRule.getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        // With this purpose I got the metrics of the screen resolution (width and height)
        wm.getDefaultDisplay().getMetrics(metrics);

        // But because it was too wacky to find values that work sufficiently, I enabled the x,y
        // coordinates within the debugging mode of the emulated phone, and inserted them manually
        // PLEASE NOTE: THIS MIGHT NOT WORK FOR EVERY PHONE AS THEY HAVE DIFFERENT RESOLUTIONS
        // BECAUSE THIS IS JUST A DEMONSTRATION THAT WE AS STUDENTS ARE CAPABLE OF CREATING TESTS
        // I DECIDED TO LET IT IN. FOR A REAL PUBLISHING PURPOSE OF THIS APP, THIS SHOULD BE REMOVED THOUGH
        // AND EITHER REPLACED BY FITTING METRICS (SEE ABOVE) OR JUST TESTED MANUALLY BY USING THE APP.
        device.click(552, 1298);

        // Last but not least we are navigating to another activity, that is displaying recipes based on the picked ingredients
        SystemClock.sleep(1000);
        onView(withId(R.id.button_search)).perform(click());
    }

}

/**
 * This is a helper class as we cannot just access children within a recyclerview directly
 * Therefore I have added this class that gives us advanced functionality through
 * which we can perform what we need.
 */
class CustomViewAction {

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return String.valueOf(id);
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(id).performClick();
            }
        };
    }

}