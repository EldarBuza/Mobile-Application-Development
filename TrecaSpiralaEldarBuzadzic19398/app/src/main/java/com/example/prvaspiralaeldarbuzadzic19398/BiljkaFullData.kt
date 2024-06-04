package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class BiljkaFullData(
    @SerializedName("id") var id: Long,
    @SerializedName("common_name") var engleskiNaziv: String,
    @SerializedName("scientific_name") var latinskiNaziv: String,
    @SerializedName("main_species") var mainSpec: MainSpeciesData,
    @SerializedName("family") var porodicaData: PorodicaData
)

data class PorodicaData(
    @SerializedName("name") var porodica: String
)
