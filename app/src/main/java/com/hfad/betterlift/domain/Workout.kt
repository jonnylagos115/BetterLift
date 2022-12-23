package com.hfad.betterlift.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hfad.betterlift.domain.Exercise
import kotlinx.parcelize.Parcelize

@Parcelize
data class Workout (
    var exerciseList: MutableList<Exercise> = mutableListOf<Exercise>(),
) : Parcelable {
    var templateTitle: String? = null
    var lastPerformed: String? = null
    var workoutNotes: String? = null
}