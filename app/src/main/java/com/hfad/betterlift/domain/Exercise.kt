package com.hfad.betterlift.domain

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    val muscleGroupName: String? = null,
    val exerciseName: String? = null,
    val instructionList: String? = null
): Parcelable {
    @DrawableRes var imageReferenceId: Int = 0
    var isSelected: Boolean? = false
    val numberOfPounds: MutableList<Int> = mutableListOf()
    val numberOfReps: MutableList<Int> = mutableListOf()
    var numberOfSets: Int = 1
}
