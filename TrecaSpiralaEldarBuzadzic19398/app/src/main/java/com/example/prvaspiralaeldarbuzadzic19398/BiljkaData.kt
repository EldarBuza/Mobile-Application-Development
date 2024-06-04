package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class BiljkaData(
    @SerializedName("id") val id: Long,
    @SerializedName("scientific_name") val latinskiNaziv: String,
    @SerializedName("common_name") val engleskiNaziv: String,
    @SerializedName("image_url") val slikaBiljkeURL: String
)

