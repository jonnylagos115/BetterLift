package com.hfad.betterlift.domain

data class Workout (
    val workoutId: Int,
    val templateTitle: String = "",
    val workoutNotes: String = "",
    val selectedExercisesIdList: List<Int> = listOf()
) {
    var lastPerformed: String? = null
}
