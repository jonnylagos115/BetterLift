package com.hfad.betterlift.repository

import android.util.Log
import androidx.room.util.copy
import com.hfad.betterlift.database.*
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.domain.Workout
import com.hfad.betterlift.domain.sortByAlphabeticalOrder
import com.hfad.betterlift.network.NetworkService
import com.hfad.betterlift.network.asDatabaseModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

// Classes in the data and business layer expose
// either suspend functions or Flows

private const val OFFSET_REQUEST_MAX = 20
private const val LIMIT_SET = 20
private const val TAG = "ExerciseRepo"

class Repository private constructor(
    private val exerciseDao: ExerciseDao,
    private val workoutDao: WorkoutDao,
    private val networkRequestRecordsDao: DBNetworkRequestRecords,
    private val exerciseService: NetworkService,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    /**
     * Returns true if we should make a network request.
     * Queries for last entry row by, if id is invalid it will return null, which
     * indicates that database is empty, returning true so that to populate database with get() network call
     * for all exercises
     */

    private val repositoryScopeIO = CoroutineScope(Dispatchers.IO)

    val exerciseStream: Flow<List<Exercise>>
        get() = exerciseDao.loadAllExercises()
            .map {
                it.asDomainModel()
            }.filterNotNull()

    val workoutStream: Flow<List<Workout>>
        get() = workoutDao.getAllWorkouts()
            .map {
                it.asDomainModel()
            }

    init {
        fetchAllNetworkExercises()
    }

    //fun exercisePagingSource() = ExercisePagingSource(this)

    suspend fun refreshLatestExerciseStream() {

    }

    suspend fun insertExerciseDB(exercise: Exercise) {
        exerciseDao.insert(exercise.asDatabaseModel())
    }

    suspend fun insertWorkoutDB(workout: Workout) {
        workoutDao.insert(workout.asDatabaseModel())
    }

    suspend fun insertNetworkRequestRecordDB(offset: Int) {
        networkRequestRecordsDao.insert(DatabaseNetworkRequestRecords(offset = offset))
    }

    suspend fun update(exercise: Exercise) {
        exerciseDao.update(exercise.asDatabaseModel())
    }

    suspend fun deleteAllExerciseItems() {
        exerciseDao.deleteAll()
    }

    suspend fun deleteWorkoutItem(workout: Workout) {
        workoutDao.delete(workout.asDatabaseModel())
    }

    suspend fun deleteAllWorkoutItems() {
        workoutDao.deleteAll()
    }

    suspend fun deleteExerciseItem(exercise: Exercise) {
        exerciseDao.delete(exercise.asDatabaseModel())
    }

    suspend fun deleteNetworkRequestRecordDB(requestRecords: DatabaseNetworkRequestRecords) {
        networkRequestRecordsDao.delete(requestRecords)
    }

    suspend fun deleteAllNetworkRequestRecordDB(){
        networkRequestRecordsDao.deleteAll()
    }

    fun retrieveExerciseItem(id: Int) = exerciseDao.loadExerciseById(id).map { it.asDomainModel() }

    suspend fun retrieveSelectedExerciseItems(ids: List<Int>) = exerciseDao.loadExercisesById(ids).map { it.asDomainModel() }

    private suspend fun isNetworkRequestRecordDBEmpty() = networkRequestRecordsDao.isEmpty()


    private fun fetchAllNetworkExercises(){
        repositoryScopeIO.launch{
            if (isNetworkRequestRecordDBEmpty()) {
                try {
                    Log.d(TAG, "Network get request made")
                    val fetchedExerciseInfoBase = exerciseService.fetchExerciseInfoBase()
                    val fetchedExerciseImageBase = exerciseService.fetchExerciseImageBase()
                    val exerciseImages =
                        exerciseService.fetchAllExerciseImages(fetchedExerciseImageBase.count).exerciseImages
                    val container = exerciseService.fetchAllExercises(fetchedExerciseInfoBase.count)
                    exerciseImages.forEach { image ->
                        container.networkExerciseList.first { it.id == image.id }.imageResUrl =
                            image.imageResUrl
                    }
                    exerciseDao.insertAll(container.asDatabaseModel())
                    insertNetworkRequestRecordDB(0)
                } catch (networkError: IOException) {
                    Log.d(TAG, "Network get request failed")
                }
            }
        }
    }

    /*suspend fun fetchAllNetworkExerciseImages(){
       withContext(Dispatchers.IO){
           val fetchedExerciseImageBase = exerciseService.fetchExerciseImageBase()
           //insertNetworkRequestRecordDB()
       }
   }
   suspend fun tryUpdateRecentExerciseCache() {
       if (shouldUpdateExerciseCache())
           fetchExercises()
   }*/

    companion object {
        @Volatile private var instance: Repository? = null

        fun getInstance(exerciseDao: ExerciseDao, workoutDao: WorkoutDao, networkRequestRecordsDao: DBNetworkRequestRecords, exerciseService: NetworkService) =
            instance ?: synchronized(this) {
                instance ?: Repository(exerciseDao, workoutDao, networkRequestRecordsDao, exerciseService).also { instance = it }
            }
    }
}

/*
In-memory cache is handled in the repo layer by using either:

LiveData - val exercises: LiveData<List<Exercise>> =  Transformations.map(itemDao.getExercises().asLiveData()) {
        it.asDomainModel()
    }

 */