package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class MainSpeciesData(
    @SerializedName("edible") var jestivo: Boolean,
    @SerializedName("specifications") var specifikacije: SpecificationsData,
    @SerializedName("growth") var growthInfo: GrowthData
)

