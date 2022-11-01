package com.hfad.betterlift.data

import com.hfad.betterlift.R
import com.hfad.betterlift.models.Exercise

object ExerciseRepo {
    val exercise = mutableListOf(
        Exercise("Legs", "Leg Press", R.drawable.legpressing),
        Exercise("Chest", "Bench Press (Barbell)", R.drawable.chestpressing)
    )
}