package com.adam.camerawithsaveapi24;

import android.app.Dialog;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoggedInTests {
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new
            ActivityTestRule<>(MainActivity.class);


    @Before
    public void setupLoginBeforeTest() {

        FirebaseAuth mAuth;
        FirebaseUser user = null;
        try {
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
        } catch (Exception e) {
        }

        if (user == null) {
            final String email = "testacc@mail.com";
            final String pass = "test1234";

            onView(withId(R.id.navigation_account_settings)).perform(click());

            onView(withId(R.id.email_sign_up_et)).perform(typeText(email), closeSoftKeyboard());

            //find password edit text and type password
            onView(withId(R.id.password_sign_up_et)).perform(typeText(pass), closeSoftKeyboard());

            onView(withId(R.id.email_sign_in_button)).perform(click());
        }
    }




    @Test
    public void clickMoreInfoButton_opensMoreInfoScreen() {
        onView(withId(R.id.navigation_more_info)).perform(click());
        onView(withId(R.id.info_date)).check(matches(allOf(isDescendantOfA(withId(R.id.more_info_cl)), isDisplayed())));
    }

    @Test
    public void clickAddMoreButtonTypeSearchClickSearch_opensAddMoreDialogTypesAndSearches(){
        onView(withId(R.id.navigation_add_more)).perform(click());

        onView(withId(R.id.add_more_typed)).perform(click());
        onView(withId(R.id.search_dialog_et)).perform(typeText("Tea"),closeSoftKeyboard());
        onView(withText("Search")).perform(click());
        onView(withId(R.id.food_select_button_one)).check(matches(allOf(isDescendantOfA(withId(R.id.pda_cl)),isDisplayed())));
    }

    @Test
    public void addItemToDatabase_typedSearchButton1Option1(){
        onView(withId(R.id.navigation_add_more)).perform(click());
        onView(withId(R.id.add_more_typed)).perform(click());
        onView(withId(R.id.search_dialog_et)).perform(typeText("Tea"),closeSoftKeyboard());
        onView(withText("Search")).perform(click());
        onView(withId(R.id.food_select_button_one)).perform(click());
        onView(withId(R.id.c1_linearLayout1)).perform(click());
        onView(withId(R.id.conf_tick)).perform(click());
        onView(withId(R.id.upload_to_firebase_button)).perform(click());
    }


}
