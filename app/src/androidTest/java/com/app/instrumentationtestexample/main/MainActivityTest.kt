package com.app.instrumentationtestexample.main

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.app.instrumentationtestexample.R
import com.app.instrumentationtestexample.mock.Mocks
import com.app.instrumentationtestexample.service.NbaService
import com.app.instrumentationtestexample.service.RetrofitService
import net.vidageek.mirror.dsl.Mirror
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activityRule = IntentsTestRule(MainActivity::class.java, false, false)

    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
        setupServer()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun whenRequestResultIsOk_shouldDisplayListOfTeams() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(Mocks.success))
        activityRule.launchActivity(Intent())
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()))
    }

    @Test
    fun checkTeamItemView_isDisplayed() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(Mocks.success))
        activityRule.launchActivity(Intent())
        onView(allOf(withId(R.id.image_view), hasSibling(withText("Atlanta Hawks"))))
        onView(allOf(withId(R.id.text_view), withText("Atlanta Hawks")))
    }

    @Test
    fun whenClickOnItemList_shouldOpenDetailActivity_withExtra() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(Mocks.success))
        activityRule.launchActivity(Intent())

        val matcher = allOf(
            hasComponent(TeamDetailActivity::class.java.name),
            hasExtraWithKey(TeamDetailActivity.INTENT_TEAM_DETAIL_KEY)
        )

        val activityResult = Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        intending(matcher).respondWith(activityResult)

        onView(withId(R.id.recycler_view))
            .perform(actionOnItemAtPosition<MainActivity.Adapter.ViewHolder>(0, click()))

        intended(matcher)
    }

    private fun setupServer() {
        val url = server.url("/").toString()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val service = NbaService

        val retrofitService = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitService::class.java)

        Mirror().on(service).set().field("service").withValue(retrofitService)
    }
}