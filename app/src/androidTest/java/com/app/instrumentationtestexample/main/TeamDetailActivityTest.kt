package com.app.instrumentationtestexample.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.app.instrumentationtestexample.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeamDetailActivityTest {

    @get:Rule
    val activityRule = ActivityTestRule(TeamDetailActivity::class.java)

    @Test
    fun whenActivityIsLaunched_shouldDisplayInitialState() {
        // home action
        onView(withContentDescription(R.string.abc_action_bar_up_description))
            .check(matches(isDisplayed()))
    }
}