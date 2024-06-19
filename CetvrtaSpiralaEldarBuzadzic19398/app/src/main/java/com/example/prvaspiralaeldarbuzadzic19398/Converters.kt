package com.example.prvaspiralaeldarbuzadzic19398
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromListType(list: List<String>?): String? {
        return list?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toListType(data: String?): List<String>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromMedicinskaKoristList(list: List<MedicinskaKorist>?): String? {
        return list?.map { it.name }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toMedicinskaKoristList(data: String?): List<MedicinskaKorist>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(data, listType)
        return stringList.map { MedicinskaKorist.valueOf(it) }
    }

    @TypeConverter
    fun fromKlimatskiTipList(list: List<KlimatskiTip>?): String? {
        return list?.map { it.name }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toKlimatskiTipList(data: String?): List<KlimatskiTip>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(data, listType)
        return stringList.map { KlimatskiTip.valueOf(it) }
    }

    @TypeConverter
    fun fromZemljisteList(list: List<Zemljiste>?): String? {
        return list?.map { it.name }?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toZemljisteList(data: String?): List<Zemljiste>? {
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<String>>() {}.type
        val stringList: List<String> = gson.fromJson(data, listType)
        return stringList.map { Zemljiste.valueOf(it) }
    }
}