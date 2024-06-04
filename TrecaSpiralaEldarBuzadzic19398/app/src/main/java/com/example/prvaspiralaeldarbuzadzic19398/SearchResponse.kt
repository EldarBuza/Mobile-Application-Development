package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data") val data: List<BiljkaData>
)
