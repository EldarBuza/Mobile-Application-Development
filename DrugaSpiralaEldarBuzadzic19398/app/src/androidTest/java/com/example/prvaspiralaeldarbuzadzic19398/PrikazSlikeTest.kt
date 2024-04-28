package com.example.prvaspiralaeldarbuzadzic19398

//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrikazSlikeTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(NovaBiljkaActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    private lateinit var mockImageBitmap : Bitmap
    private fun createMockImageBitmap(): Bitmap {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return BitmapFactory.decodeResource(context.resources, R.drawable.plant)
    }

    @Test
    fun testUslikajBiljkuBtn() {
        /*      TEST PROLAZI TEK KADA KORISNIK USLIKA SLIKU     */
        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())
        onView(withId(R.id.slikaIV)).check(matches(withBitmap()))
    }

    private fun withBitmap(): Matcher<in View> {
        return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText("with bitmap drawable")
            }
            override fun matchesSafely(imageView: ImageView?): Boolean {
                val drawable = imageView?.drawable
                return (drawable is BitmapDrawable && (drawable.bitmap != null))
            }
        }
    }

}