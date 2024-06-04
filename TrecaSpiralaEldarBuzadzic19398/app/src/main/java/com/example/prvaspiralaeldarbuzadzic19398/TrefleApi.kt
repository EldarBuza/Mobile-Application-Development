package com.example.prvaspiralaeldarbuzadzic19398

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrefleApi {

    @GET("plants/search")
    suspend fun searchPlants(
        @Query("q") query: String,
        @Query("token") apiKey: String = "ghjeu8Rq3A4kpmMhJCEv0k4SDYAmDg_CkkoyG1R4O24",
        ): Response<SearchResponse>

    @GET("plants/{id}")
    suspend fun getPlantViaId(
        @Path("id") id: Long,
        @Query("token") apiKey: String = "ghjeu8Rq3A4kpmMhJCEv0k4SDYAmDg_CkkoyG1R4O24"
    ): Response<SearchResponseViaId>
/*
    @GET("plants")
    suspend fun searchPlantsByColor(
        @Query("filter[flower_color]") flowerColor: String,
        @Query("token") apiKey: String = "ghjeu8Rq3A4kpmMhJCEv0k4SDYAmDg_CkkoyG1R4O24"
    ): Response<SearchResponseList>*/
@GET("plants/search")
suspend fun searchPlantsByColor(
    @Query("filter[flower_color]") flowerColor: String,
    @Query("q") query: String,
    @Query("token") apiKey: String = "ghjeu8Rq3A4kpmMhJCEv0k4SDYAmDg_CkkoyG1R4O24"
): Response<SearchResponseList>
}