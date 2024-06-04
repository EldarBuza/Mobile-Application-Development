package com.example.prvaspiralaeldarbuzadzic19398

import com.google.gson.annotations.SerializedName

data class SearchResponseViaId(
    @SerializedName("data") val data: BiljkaFullData
)

data class SearchResponseList(
    @SerializedName("data") val data: List<BiljkaData>,
    @SerializedName("meta") val meta: MetaData
)
