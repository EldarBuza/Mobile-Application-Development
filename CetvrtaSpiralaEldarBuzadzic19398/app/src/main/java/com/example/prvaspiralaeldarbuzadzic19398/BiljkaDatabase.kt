package com.example.prvaspiralaeldarbuzadzic19398
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(Biljka::class, BiljkaBitmap::class), version = 1)
@TypeConverters(Converters::class, BitmapConverters::class)
abstract class BiljkaDatabase : RoomDatabase() {
    abstract fun biljkaDao(): BiljkaDAO

    companion object {
        @Volatile
        private var INSTANCE: BiljkaDatabase? = null

            fun getInstance(context: Context): BiljkaDatabase {
                if (INSTANCE == null) {
                    synchronized(BiljkaDatabase::class) {
                        INSTANCE = buildRoomDB(context)
                    }
                }
                return INSTANCE!!
            }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BiljkaDatabase::class.java,
                "biljke-db"
            ).build()
    }
}