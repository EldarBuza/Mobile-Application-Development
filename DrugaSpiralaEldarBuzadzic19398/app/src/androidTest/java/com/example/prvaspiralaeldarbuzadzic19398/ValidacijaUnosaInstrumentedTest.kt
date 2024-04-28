package com.example.prvaspiralaeldarbuzadzic19398

import android.os.IBinder
import android.view.WindowManager
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Root
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matchers.anything
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidacijaUnosaInstrumentedTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(
        NovaBiljkaActivity::class.java
    )

    @Test
    fun testValidacijaUnosaNaziv() {
        onView(withId(R.id.nazivET)).perform(typeText("a"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("bbb"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("cccc"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.nazivET)).check(matches(hasErrorText("Dužina teksta mora biti u segmentu [2,20]")))
    }

    @Test
    fun testValidacijaUnosaPorodica() {
        onView(withId(R.id.nazivET)).perform(typeText("aaa"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("bbbbbbbbbbbbbbbbbbbbb"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("cccc"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.porodicaET)).check(matches(hasErrorText("Dužina teksta mora biti u segmentu [2,20]")))
    }

    @Test
    fun testValidacijaUnosaMedicinskoUpozorenje() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Predugo medicinsko upozorenje"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onView(withId(R.id.medicinskoUpozorenjeET)).check(matches(hasErrorText("Dužina teksta mora biti u segmentu [2,20]")))
    }

    @Test
    fun testValidacijaUnosaJelo() {
        onView(withId(R.id.jeloET)).perform(typeText("Definitivno predugo jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        onView(withId(R.id.jeloET)).check(matches(hasErrorText("Dužina teksta mora biti u segmentu [2,20]")))
    }

    @Test
    fun testValidacijaMedicinskeKoristi() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Dovoljno"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        // Provjeravamo da li je Toast poruka prikazana ako nije odabrana nijedna stavka iz liste Medicinskih koristi
        onView(withText("Odaberite barem jednu medicinsku korist")).
        inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    @Test
    fun testValidacijaKlimatskogTipa() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Dovoljno"), closeSoftKeyboard())
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(0).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        // Provjeravamo da li je Toast poruka prikazana ako nije odabrana nijedna stavka iz liste klimatskih tipova
        onView(withText("Odaberite barem jedan klimatski tip")).
        inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    @Test
    fun testValidacijaZemljisnogTipa() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Dovoljno"), closeSoftKeyboard())
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(0).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        // Provjeravamo da li je Toast poruka prikazana ako nije odabrana nijedna stavka iz liste Zemljisnih tipova
        onView(withText("Odaberite barem jednu zemljišni tip")).
        inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    @Test
    fun testValidacijaProfilaOkusa() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Dovoljno"), closeSoftKeyboard())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(0).perform(click())
        onView(withId(R.id.jeloET)).perform(typeText("Neko novo jelo"), closeSoftKeyboard())
        onView(withId(R.id.dodajJeloBtn)).perform(click())
        // Provjeravamo da li je Toast poruka prikazana ako nije nijedna stavka odabrana
        onView(withText("Odaberite profil okusa")).
        inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    @Test
    fun testValidacijaPrazneListeJela() {
        onView(withId(R.id.nazivET)).perform(typeText("Ime biljke"), closeSoftKeyboard())
        onView(withId(R.id.porodicaET)).perform(typeText("Neka porodica"), closeSoftKeyboard())
        onView(withId(R.id.medicinskoUpozorenjeET)).perform(typeText("Dovoljno"), closeSoftKeyboard())
        onData(anything()).inAdapterView(withId(R.id.medicinskaKoristLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.klimatskiTipLV)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.zemljisniTipLV)).atPosition(0).perform(click())
        onView(withId(R.id.dodajBiljkuBtn)).perform(click())
        // Provjeravamo da li je Toast poruka prikazana ako nije odabrana nijedna stavka iz liste Zemljisnih tipova
        onView(withText("Dodajte barem jedno jelo")).
        inRoot(ToastMatcher().apply {
            matches(isDisplayed())
        })
    }

    class ToastMatcher : TypeSafeMatcher<Root?>() {

        override fun matchesSafely(item: Root?): Boolean {
            val type: Int? = item?.windowLayoutParams?.get()?.type
            if (type == WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW) {
                val windowToken: IBinder = item.decorView.windowToken
                val appToken: IBinder = item.decorView.applicationWindowToken
                if (windowToken === appToken) { // means this window isn't contained by any other windows.
                    return true
                }
            }
            return false
        }

        override fun describeTo(description: Description?) {
            description?.appendText("is toast")
        }
    }



}