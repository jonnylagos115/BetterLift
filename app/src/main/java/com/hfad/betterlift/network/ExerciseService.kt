package com.hfad.betterlift.network

import com.hfad.betterlift.network.model.ExerciseNetworkEntity
import com.hfad.betterlift.network.responses.ExerciseSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ExerciseService {

    @GET("exercises")
    suspend fun getAll(
        @Header("X-Api-Key") token: String
    ) : List<ExerciseNetworkEntity>

    @GET("exercises")
    suspend fun getByMuscleType(
        @Header("X-Api-Key") token: String,
        @Query("muscle") muscle: String
    ): List<ExerciseNetworkEntity>
}