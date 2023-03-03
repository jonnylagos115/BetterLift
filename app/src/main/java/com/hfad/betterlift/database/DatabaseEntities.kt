package com.hfad.betterlift.database

import androidx.room.*
import com.google.gson.Gson
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.domain.Workout


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabaseExercises represents a exercise entity in the database.
 */
@Entity
data class DatabaseExercise constructor(
    @PrimaryKey(autoGenerate = true) val exerciseId: Int = 0,
    val exerciseName: String,
    val muscleGroupName: String,
    val description: String,
    var imageResUrl: String? = null
)

@Entity
data class DatabaseWorkout constructor(
    @PrimaryKey(autoGenerate = true) val workoutId: Int = 0,
    val templateTitle: String,
    val workoutNotes: String,
    val selectedExercisesIdList: List<Int>
)

@Entity(tableName = "db_network_request_records")
data class DatabaseNetworkRequestRecords constructor(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val offset: Int
)

class ExerciseIdListTypeConverters {
    @TypeConverter
    fun fromListIntToString(ids: List<Int>): String = ids.toString()
    @TypeConverter
    fun toListIntFromString(stringList: String): List<Int> {
        val result = ArrayList<Int>()
        val split =stringList.replace("[","").replace("]","").replace(" ","").split(",")
        for (n in split) {
            result.add(n.toInt())
            /*try {
                result.add(n.toInt())
            } catch (e: Exception) {

            }*/
        }
        return result
    }
}

/**
 * Mappings for DatabaseExercises to domain entities
 */
fun List<DatabaseExercise>.asDomainModel(): List<Exercise> {
    return map {
        Exercise(
            exerciseId = it.exerciseId,
            exerciseName = it.exerciseName,
            muscleGroupName = it.muscleGroupName,
            description = it.description,
            imageResUrl = it.imageResUrl
        )
    }
}

fun DatabaseExercise.asDomainModel(): Exercise {
    return let {
        Exercise(
            exerciseId = it.exerciseId,
            exerciseName = it.exerciseName,
            muscleGroupName = it.muscleGroupName,
            description = it.description,
            imageResUrl = it.imageResUrl
        )
    }
}

@JvmName("asDatabaseModelExercise")
fun List<Exercise>.asDatabaseModel(): List<DatabaseExercise> {
    return map {
        DatabaseExercise(
            exerciseId = it.exerciseId,
            exerciseName = it.exerciseName,
            muscleGroupName = it.muscleGroupName,
            description = it.description,
            imageResUrl = it.imageResUrl)
    }
}

fun Exercise.asDatabaseModel(): DatabaseExercise {
    return let {
        DatabaseExercise(
            exerciseId = it.exerciseId,
            exerciseName = it.exerciseName,
            muscleGroupName = it.muscleGroupName,
            description = it.description,
            imageResUrl = it.imageResUrl)
    }
}

/**
 * Mappings for DatabaseWorkout to domain entities
 */
@JvmName("asDomainModelDatabaseWorkout")
fun List<DatabaseWorkout>.asDomainModel(): List<Workout> {
    return map {
        Workout(
            workoutId = it.workoutId,
            templateTitle = it.templateTitle,
            workoutNotes = it.workoutNotes,
            selectedExercisesIdList = it.selectedExercisesIdList
        )
    }
}

fun DatabaseWorkout.asDomainModel(): Workout {
    return let {
        Workout(
            workoutId = it.workoutId,
            templateTitle = it.templateTitle,
            workoutNotes = it.workoutNotes,
            selectedExercisesIdList = it.selectedExercisesIdList
        )
    }
}

fun List<Workout>.asDatabaseModel(): List<DatabaseWorkout> {
    return map {
        DatabaseWorkout(
            workoutId = it.workoutId,
            templateTitle = it.templateTitle,
            workoutNotes = it.workoutNotes,
            selectedExercisesIdList = it.selectedExercisesIdList)
    }
}

fun Workout.asDatabaseModel(): DatabaseWorkout {
    return let {
        DatabaseWorkout(
            workoutId = it.workoutId,
            templateTitle = it.templateTitle,
            workoutNotes = it.workoutNotes,
            selectedExercisesIdList = it.selectedExercisesIdList)
    }
}