package com.example.prvaspiralaeldarbuzadzic19398

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.greaterThan
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private val countBiljka = "SELECT COUNT(*) AS broj_biljaka FROM Biljka"
    private val countBiljkaBitmaps = "SELECT COUNT(*) AS broj_bitmapa FROM BiljkaBitmap"

    private val describeBiljka = "pragma table_info('Biljka')"
    private val describeBiljkaBitmap = "pragma table_info('BiljkaBitmap')"

    private val kolone = mapOf(
        "BiljkaBitmap" to arrayListOf("idBiljke", "bitmap"),
        "Biljka" to arrayListOf(
            "id",
            "naziv",
            "family",
            "medicinskoUpozorenje",
            "jela",
            "klimatskiTipovi",
            "zemljisniTipovi",
            "medicinskeKoristi"
        )
    )

    companion object {
        lateinit var db: SupportSQLiteDatabase
        lateinit var context: Context
        lateinit var roomDb: BiljkaDatabase
        lateinit var biljkaDAO: BiljkaDAO

        @BeforeClass
        @JvmStatic
        fun createDB() = runBlocking {
            val scenarioRule = ActivityScenario.launch(MainActivity::class.java)
            context = ApplicationProvider.getApplicationContext()
            roomDb = Room.inMemoryDatabaseBuilder(context, BiljkaDatabase::class.java).build()
            biljkaDAO = roomDb.biljkaDao()
            biljkaDAO.getAllBiljkas()
            db = roomDb.openHelper.readableDatabase

        }
    }
    private fun executeCountAndCheck(query: String, column: String, value: Long) {
        var rezultat = BiljkeDB4test.db.query(query)
        rezultat.moveToFirst()
        var brojOdgovora = rezultat.getLong(0)
        MatcherAssert.assertThat(brojOdgovora, CoreMatchers.`is`(CoreMatchers.equalTo(value)))
    }

    private fun checkColumns(query: String, naziv: String) {
        var rezultat = BiljkeDB4test.db.query(query)
        val list = (1..rezultat.count).map {
            rezultat.moveToNext()
            rezultat.getString(1)
        }
        ViewMatchers.assertThat(list, CoreMatchers.hasItems(*kolone[naziv]!!.toArray()))
    }

    @get:Rule
        val intentsTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testDodavanjaBiljkeUBazu() = runBlocking{
        //Vršim cišćenje baze zato što je test lagano "prevariti" tako što jednostavno prije pokretanja testa dodam ovu biljku preko aplikacije!
        biljkaDAO.clearData()
        val biljka: Biljka = Biljka("(Campanula rapunculus)", "Campanulaceae", "Nemoj dirati",
            emptyList(), ProfilOkusaBiljke.AROMATICNO, emptyList(), emptyList(), emptyList() )
        biljkaDAO.saveBiljka(biljka)
        val sveBiljke = biljkaDAO.getAllBiljkas()
        val velicina: Int = sveBiljke.size
        assertThat(sveBiljke[velicina-1].naziv, `is`("(Campanula rapunculus)"))
    }

    @Test
    fun testCiscenjaBazeSaClearData() = runBlocking{
        //Dodajem barem jednu biljku u bazu jer bi se test mogao "prevariti" ukoliko jednostavno ostavim praznu bazu!
        val biljka: Biljka = Biljka("(Campanula rapunculus)", "Campanulaceae", "Nemoj dirati",
            emptyList(), ProfilOkusaBiljke.AROMATICNO, emptyList(), emptyList(), emptyList() )
        biljkaDAO.saveBiljka(biljka)
        val brojBiljki: Int = biljkaDAO.getAllBiljkas().size
        System.out.println(brojBiljki)
        assertThat(brojBiljki, greaterThan(0))
        biljkaDAO.clearData()
        val noviBrojBiljki = biljkaDAO.getAllBiljkas().size
        System.out.println(noviBrojBiljki)
        assertThat(noviBrojBiljki, equalTo(0))

    }

}