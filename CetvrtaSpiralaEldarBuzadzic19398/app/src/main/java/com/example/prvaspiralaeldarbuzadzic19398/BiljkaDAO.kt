package com.example.prvaspiralaeldarbuzadzic19398
import android.graphics.Bitmap
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface BiljkaDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBiljka(biljka: Biljka): Long

    suspend fun saveBiljka(biljka: Biljka): Boolean {
        val id = insertBiljka(biljka)
        return id != -1L
    }

    @Insert
    suspend fun saveAllBiljkas(vararg bilje: Biljka)

    @Transaction
    suspend fun fixOfflineBiljka(): Int {
        var biljkeToFix = ArrayList(getOfflineBiljkas())
        var updatedCount = 0

        for(i in biljkeToFix.indices){
            val original = biljkeToFix[i].copy()
            println(original.porodica)
            var biljka = biljkeToFix[i]
            if (!biljka.onlineChecked){
                var novaBiljka = TrefleDAO().fixData(biljka)
                System.out.println("Provjereno!")
                println(novaBiljka.porodica + " " + biljka.porodica)
                biljka = novaBiljka
            }
            if (biljka.porodica != original.porodica || biljka.medicinskoUpozorenje != original.medicinskoUpozorenje || biljka.klimatskiTipovi != original.klimatskiTipovi || biljka.zemljisniTipovi != original.zemljisniTipovi || biljka.jela != original.jela){
                biljka.onlineChecked = true
                updateBiljka(biljka)
                updatedCount++
            }
        }
        return updatedCount
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImageThroughBitmap(biljkaBitmap: BiljkaBitmap): Long

    suspend fun addImage(idBiljke: Long, bitmap: Bitmap): Boolean {
        val biljkaBitmap = BiljkaBitmap(idBiljke, bitmap)
        val id = addImageThroughBitmap(biljkaBitmap)
        return id != -1L
    }

    @Query("SELECT * FROM Biljka")
    suspend fun getAllBiljkas(): List<Biljka>

    @Query("DELETE FROM Biljka")
    suspend fun clearBiljkas()

    @Query("DELETE FROM BiljkaBitmap")
    suspend fun clearBiljkaBitmaps()

    @Transaction
    suspend fun clearData() {
        clearBiljkas()
        clearBiljkaBitmaps()
    }

    @Update
    suspend fun updateBiljka(biljka: Biljka): Int

    @Query("SELECT * FROM Biljka WHERE onlineChecked = 0")
    suspend fun getOfflineBiljkas(): List<Biljka>

    @Query("SELECT * FROM BiljkaBitmap WHERE idBiljke = :id")
    suspend fun provjeriPostojiLi(id: Long): List<BiljkaBitmap>

}
