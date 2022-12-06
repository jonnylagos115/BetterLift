package com.hfad.betterlift.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    var muscleGroupName: String,
    var exerciseName: String,
    @DrawableRes val imageReferenceId: Int
): Parcelable {
    var instructionList: String? = null
    var isSelected: Boolean? = false
    val numberOfPounds: MutableList<Int> = mutableListOf()
    val numberOfReps: MutableList<Int> = mutableListOf()
    var numberOfSets: Int = 1
}
