package com.example.prvaspiralaeldarbuzadzic19398

//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.widget.ImageView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Matchers.allOf
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PrikazSlikeTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(Manifest.permission.CAMERA)

    private lateinit var mockImageBitmap : Bitmap
    private fun createMockImageBitmap(): Bitmap {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        return BitmapFactory.decodeResource(context.resources, R.drawable.plant2)
    }

    @Test
    fun testSlike(){
        onView(withId(R.id.novaBiljkaBtn)).perform(click())
        Thread.sleep(500)
        mockImageBitmap=createMockImageBitmap()
        Intents.init()
        val resultIntent = Intent()
        resultIntent.putExtra("data", mockImageBitmap)
        val result = Activity.RESULT_OK
        Intents.intending(allOf(
            hasAction(MediaStore.ACTION_IMAGE_CAPTURE)
        )).respondWith(
            Instrumentation.ActivityResult(result, resultIntent)
        )
        onView(withId(R.id.uslikajBiljkuBtn)).perform(click())
        onView(withId(R.id.slikaIV)).perform(click()).check { view, _ ->
            val imageView = view as ImageView
            val displayedBitmap = (imageView.drawable as BitmapDrawable).bitmap
            assertEquals(mockImageBitmap.width, displayedBitmap.width)
            assertEquals(mockImageBitmap.height, displayedBitmap.height)

            for (x in 0 until mockImageBitmap.width) {
                for (y in 0 until mockImageBitmap.height) {
                    assertEquals(
                        "Pixel mismatch at x=$x, y=$y",
                        mockImageBitmap.getPixel(x, y),
                        displayedBitmap.getPixel(x, y)
                    )
                }
            }
        }
        Intents.release()
    }


}