package com.hfad.betterlift.models

data class Workout (
    var templateTitle: String,
    var lastPerformed: String,
    var workoutExerciseList: MutableList<String> = mutableListOf<String>()) {
    var numberOfSets: Int = 0
    var numberOfPounds: Int = 0
    var numberOfReps: Int = 0
}