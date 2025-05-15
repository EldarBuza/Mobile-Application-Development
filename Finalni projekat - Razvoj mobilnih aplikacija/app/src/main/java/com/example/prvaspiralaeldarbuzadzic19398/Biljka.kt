package com.example.prvaspiralaeldarbuzadzic19398
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


//Vidi da li se trebaju praviti Type Converteri ili nesto slicno u GSON za List<T> atribute!
@Entity
data class Biljka(
    @ColumnInfo(name = "naziv")var naziv: String,
    @ColumnInfo(name = "family")var porodica: String,
    @ColumnInfo(name = "medicinskoUpozorenje")var medicinskoUpozorenje: String,
    @ColumnInfo(name = "medicinskeKoristi")var medicinskeKoristi: List<MedicinskaKorist>,
    @ColumnInfo(name = "profilOkusa")var profilOkusa: ProfilOkusaBiljke?,
    @ColumnInfo(name = "jela")var jela: List<String>,
    @ColumnInfo(name = "klimatskiTipovi")var klimatskiTipovi: List<KlimatskiTip>,
    @ColumnInfo(name = "zemljisniTipovi")var zemljisniTipovi: List<Zemljiste>,
    @ColumnInfo(name = "onlineChecked")var onlineChecked: Boolean = false,
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    ){
    fun copy(): Biljka {
        return Biljka(
            naziv,
            porodica,
            medicinskoUpozorenje,
            medicinskeKoristi.toList(),
            profilOkusa,
            jela.toList(),
            klimatskiTipovi.toList(),
            zemljisniTipovi.toList(),
            onlineChecked
        )
    }
}
