package com.hfad.betterlift.network.responses

import com.google.gson.annotations.SerializedName
import com.hfad.betterlift.network.model.ExerciseNetworkEntity

class ExerciseSearchResponse (

    @SerializedName("exercises")
    var exercises: List<ExerciseNetworkEntity>
)