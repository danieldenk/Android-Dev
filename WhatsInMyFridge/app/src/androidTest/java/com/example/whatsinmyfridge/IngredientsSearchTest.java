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

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * These are just simple UI Tests to check whether the ingredients passed lead to the corresponding
 * recipes that would be expected to be included (HomeFragment & SearchResults)
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class IngredientsSearchTest {

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
    public void ingredientsSearchTest() {
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

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView = onView(
                allOf(withId(R.id.auto_complete_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView.perform(replaceText("Flour"), closeSoftKeyboard());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.button_add), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView2 = onView(
                allOf(withId(R.id.auto_complete_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView2.perform(replaceText("Eggs"), closeSoftKeyboard());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.button_add), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView3 = onView(
                allOf(withId(R.id.auto_complete_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView3.perform(replaceText("Milk"), closeSoftKeyboard());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.button_add), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton4.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView4 = onView(
                allOf(withId(R.id.auto_complete_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView4.perform(replaceText("Meat"), closeSoftKeyboard());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.button_add), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton5.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatAutoCompleteTextView6 = onView(
                allOf(withId(R.id.auto_complete_input),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatAutoCompleteTextView6.perform(replaceText("Bread"), closeSoftKeyboard());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(R.id.button_add), withText("ADD"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                2),
                        isDisplayed()));
        appCompatButton6.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.button_search), withText("SEARCH RECIPE"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatButton7.perform(click());

        // Then we are pressing delete for the third ingredient added (Eggs)
        SystemClock.sleep(1000);

        onData(allOf(is(instanceOf(String.class)), containsString("Pancake")));
        onData(allOf(is(instanceOf(String.class)), containsString("Schnitzel")));
        onData(allOf(is(instanceOf(String.class)), containsString("Corndog")));
    }
}
