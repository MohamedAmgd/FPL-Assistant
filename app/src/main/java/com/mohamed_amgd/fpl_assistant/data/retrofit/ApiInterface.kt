package com.mohamed_amgd.fpl_assistant.data.retrofit

import com.mohamed_amgd.fpl_assistant.data.models.Player
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("player")
    suspend fun getPlayers(
        @Query("key") Key: String?,
        @Query("min_value") minValue: String?,
        @Query("max_value") maxValue: String?,
        @Query("sort_dir") sortDir: String?,
    ): Response<List<Player>>
}