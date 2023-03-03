package com.hfad.betterlift.domain

import com.hfad.betterlift.viewmodels.ExerciseItemUiState

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

data class Exercise(
    val exerciseId: Int = -1,
    val exerciseName: String,
    val muscleGroupName: String,
    val description: String = "",
    val imageResUrl: String? = null
) {
    var isSelected: Boolean? = false
    val numberOfPounds: MutableList<Int> = mutableListOf()
    val numberOfReps: MutableList<Int> = mutableListOf()
    var numberOfSets: Int = 1
}

/**
 * Sorts list of exercises in alphabetical order by name
 */
fun List<Exercise>.sortByAlphabeticalOrder(): List<Exercise> {
    return sortedBy {
        it.exerciseName
    }
}
