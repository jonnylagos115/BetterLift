package com.hfad.betterlift.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout (
    var exerciseList: MutableList<Exercise> = mutableListOf<Exercise>(),
) : Parcelable {
    var templateTitle: String? = null
    var lastPerformed: String? = null
    var workoutNotes: String? = null
}