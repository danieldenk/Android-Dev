package com.example.whatsinmyfridge;


import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * These are just simple UI Tests to check whether the favorite adding / recipe display functionality
 * is actually working (SearchFragment)
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    @Test
    public void searchFragmentTest() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.login_guest), withText("Login as Guest"),
                        childAtPosition(
                                allOf(withId(R.id.loginLayout),
                                        childAtPosition(
                                                withId(android.R.id.content),
                                                0)),
                                8),
                        isDisplayed()));
        appCompatButton.perform(click());
        // Waiting (symbolizes real life user interaction)
        SystemClock.sleep(1000);
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.search), withContentDescription("Search"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_bottom),
                                        0),
                                2),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());
        // Waiting (symbolizes real life user interaction)
        SystemClock.sleep(1000);
        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.auto_complete_input_search),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        appCompatAutoCompleteTextView.perform(scrollTo(), replaceText("Boiled Egg"), closeSoftKeyboard());
        // Waiting (symbolizes real life user interaction)
        SystemClock.sleep(1000);

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.search_heart), withContentDescription("Favorite Image"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                7)));
        appCompatImageView.perform(scrollTo(), click());

        // Waiting (symbolizes real life user interaction)
        SystemClock.sleep(1000);
        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.fav), withContentDescription("Favorites"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.navigation_bottom),
                                        0),
                                1),
                        isDisplayed()));
        bottomNavigationItemView2.perform(click());

        onView(withId(R.id.fav))
                .perform(click());

        // Now we need to do a manual check if the item has been removed/added to favorites
        // It cannot be checked here automatically because we do not know the state it had before
    }
}
