package com.adam.camerawithsaveapi24;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Adam on 10/04/2018.
 */
@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    @Rule
    public ActivityTestRule<SignUpActivity> signUpActivityActivityTestRule = new
            ActivityTestRule<>(SignUpActivity.class);


    @Test
    public void clickSignUpAfterFillingInFormCorrectly_showMainActivity() {
        Random r = new Random();
        int res = r.nextInt(10000-1)+ 0 ;
        String email = "test"+res+"@gmail.com";
        String password = "1234Test";

        //find email edit text and type email
        onView(withId(R.id.email_sign_up_et)).perform(typeText(email), closeSoftKeyboard());

        //find password edit text and type password
        onView(withId(R.id.password_sign_up_et)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.edl_username_tv))
                .check(matches(allOf(isDescendantOfA(withId(R.id.user_details_cl)), isDisplayed())));

    }

    @Test
    public void clickSignUpAfterFillingInFormIncorrectlyEmail_showMainActivity() {
        String email = "testgmail.com";
        String password = "1234Test";

        //find email edit text and type email
        onView(withId(R.id.email_sign_up_et)).perform(typeText(email), closeSoftKeyboard());

        //find password edit text and type password
        onView(withId(R.id.password_sign_up_et)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.email_sign_up_et))
                .check(matches(allOf(isDescendantOfA(withId(R.id.sign_up_cl)), isDisplayed())));

    }

    @Test
    public void clickSignUpAfterFillingInFormIncorrectlyPassword_showUserDetails() {
        String email = "testg@gmail.com";
        String password = "12";

        //find email edit text and type email
        onView(withId(R.id.email_sign_up_et)).perform(typeText(email), closeSoftKeyboard());

        //find password edit text and type password
        onView(withId(R.id.password_sign_up_et)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.sign_up_btn)).perform(click());

        onView(withId(R.id.email_sign_up_et))
                .check(matches(allOf(isDescendantOfA(withId(R.id.sign_up_cl)), isDisplayed())));

    }



}