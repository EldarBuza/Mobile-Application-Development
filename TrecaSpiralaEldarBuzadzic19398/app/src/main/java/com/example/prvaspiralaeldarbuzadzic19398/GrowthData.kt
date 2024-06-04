package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class GrowthData(
    @SerializedName("soil_texture") var teksturaZemljista: String?,
    @SerializedName("light") var svjetlost: String?,
    @SerializedName("atmospheric_humidity") var vlaznost: String?
)
