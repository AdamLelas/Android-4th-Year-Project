package com.adam.camerawithsaveapi24;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoggedOutTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new
            ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickLoginButton_opensLoginScreen() {
        //finds and clicks the button to go to login screen
        onView(withId(R.id.no_user_btn)).perform(click());

        //check that a view id from login screen is on new screen
        onView(withId(R.id.email_sign_up_et))
                .check(matches(allOf(isDescendantOfA(withId(R.id.sign_up_cl)), isDisplayed())));
    }

    @Test
    public void clickAccountButton_opensLoginScreen() {
        //finds and clicks the button to go to login screen
        onView(withId(R.id.navigation_account_settings)).perform(click());

        //check that a view id from login screen is on new screen
        onView(withId(R.id.email_sign_up_et))
                .check(matches(allOf(isDescendantOfA(withId(R.id.sign_up_cl)), isDisplayed())));
    }

}
