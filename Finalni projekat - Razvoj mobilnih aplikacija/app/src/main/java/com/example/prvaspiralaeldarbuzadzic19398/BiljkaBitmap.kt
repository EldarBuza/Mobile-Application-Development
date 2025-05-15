package com.example.prvaspiralaeldarbuzadzic19398
import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Biljka::class,
        parentColumns = ["id"],
        childColumns = ["idBiljke"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class BiljkaBitmap(
    @PrimaryKey val idBiljke: Long,
    @ColumnInfo(name = "bitmap") val bitmap: Bitmap
)