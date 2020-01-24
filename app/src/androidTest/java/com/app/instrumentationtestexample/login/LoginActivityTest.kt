package com.app.instrumentationtestexample.login

import android.app.Activity
import android.app.Instrumentation
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.instrumentationtestexample.R
import com.app.instrumentationtestexample.Utils
import com.app.instrumentationtestexample.main.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class LoginActivityTest {

    @get:Rule
    val rule = IntentsTestRule(LoginActivity::class.java, false, true)

    private val edtEmail = withId(R.id.edt_email)
    private val edtPassword = withId(R.id.edt_password)
    private val btnLogin = withId(R.id.btn_login)
    private val progressBar = withId(R.id.progress_bar)

    @Test
    fun whenActivityIsLaunched_shouldDisplayInitialState() {
        onView(edtEmail).check(matches(isDisplayed()))
        onView(edtPassword).check(matches(isDisplayed()))
        onView(edtPassword).check(matches(isDisplayed()))
        onView(progressBar).check(matches(not(isDisplayed())))
    }

    @Test
    fun whenEmailIsEmpty_andClickLoginButton_shouldSetEditTextErrorAndRequestFocus() {
        onView(btnLogin).perform(click())
        onView(edtEmail).check(matches(hasErrorText()))
        onView(edtEmail).check(matches(hasErrorText()))
        onView(edtEmail).check(matches(hasFocus()))
    }

    @Test
    fun whenPasswordIsEmpty_andClickLoginButton_shouldSetPasswordErrorAndRequestFocus() {
        onView(edtEmail).perform(typeText("user@email.com"), closeSoftKeyboard())
        onView(btnLogin).perform(click())
        onView(edtPassword).check(matches(hasErrorText()))
        onView(edtPassword).check(matches(hasFocus()))
    }

    @Test
    fun whenEmailAndPasswordIsNotEmpty_andClickLoginButton_shouldDisableLoginButton() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(Utils.replaceProgressBarDrawable())
        onView(edtEmail).perform(typeText("user@email.com"), closeSoftKeyboard())
        onView(edtPassword).perform(typeText("admin123"), closeSoftKeyboard())

        // don't open main activity
        val matcher = hasComponent(MainActivity::class.java.name)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(result)

        onView(btnLogin).perform(click()).check(matches(not(isEnabled())))
        intended(matcher)
    }

    @Test
    fun whenEmailAndPasswordIsNotEmpty_andClickLoginButton_shouldDisplayProgressBar() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(Utils.replaceProgressBarDrawable())
        onView(edtEmail).perform(typeText("user@email.com"), closeSoftKeyboard())
        onView(edtPassword).perform(typeText("admin123"), closeSoftKeyboard())

        // don't open main activity
        val matcher = hasComponent(MainActivity::class.java.name)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(result)

        onView(btnLogin).perform(click())
        onView(progressBar).check(matches(isDisplayed()))
        intended(matcher)
    }

    @Test
    fun whenBothFieldsAreFilled_andClickLoginButton_shouldOpenMainActivity() {
        onView(isAssignableFrom(ProgressBar::class.java)).perform(Utils.replaceProgressBarDrawable())
        onView(edtEmail).perform(typeText("user@email.com"), closeSoftKeyboard())
        onView(edtPassword).perform(typeText("admin123"), closeSoftKeyboard())

        // don't open main activity
        val matcher = hasComponent(MainActivity::class.java.name)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(result)

        onView(btnLogin).perform(click())
        intended(matcher)
    }

    private fun hasErrorText(): BoundedMatcher<View, EditText> {
        return object : BoundedMatcher<View, EditText>(EditText::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("has error text:")
            }

            override fun matchesSafely(editText: EditText?): Boolean {
                return editText?.error != null
            }
        }
    }
}