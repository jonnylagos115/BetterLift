package com.hfad.betterlift.network.model

import com.google.gson.annotations.SerializedName

class ExerciseNetworkEntity (

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("muscle")
    var muscle: String? = null,

    @SerializedName("instructions")
    var instructions: String? = null,
)