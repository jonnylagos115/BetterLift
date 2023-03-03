package com.hfad.betterlift.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.hfad.betterlift.database.AppDatabase
import com.hfad.betterlift.network.NetworkService
import com.hfad.betterlift.repository.Repository
import com.hfad.betterlift.viewmodels.ExerciseDetailViewModelFactory
import com.hfad.betterlift.viewmodels.ExerciseViewModelFactory
import com.hfad.betterlift.viewmodels.WorkoutEditTemplateViewModelFactory
import com.hfad.betterlift.viewmodels.WorkoutViewModelFactory


interface ViewModelFactoryProvider {
    fun provideExerciseViewModelFactory(context: Context): ExerciseViewModelFactory
    fun provideExerciseDetailViewModelFactory(context: Context): ExerciseDetailViewModelFactory
    fun provideWorkoutViewModelFactory(context: Context): WorkoutViewModelFactory
    fun provideWorkoutEditTemplateViewModelFactory(context: Context): WorkoutEditTemplateViewModelFactory
}

val Injector: ViewModelFactoryProvider
    get() = currentInjector

private object DefaultViewModelProvider: ViewModelFactoryProvider {
    private fun getRepository(context: Context): Repository {
        return Repository.getInstance(
            exerciseDao(context),
            workoutDao(context),
            networkRequestRecordsDao(context),
            exerciseService()
        )
    }

    private fun exerciseService() = NetworkService()

    private fun exerciseDao(context: Context) =
        AppDatabase.getInstance(context.applicationContext).exerciseDao()

    private fun workoutDao(context: Context) =
        AppDatabase.getInstance(context.applicationContext).workoutDao()

    private fun networkRequestRecordsDao(context: Context) =
        AppDatabase.getInstance(context.applicationContext).networkRequestRecordsDao()

    override fun provideExerciseViewModelFactory(context: Context): ExerciseViewModelFactory {
        val repository = getRepository(context)
        return ExerciseViewModelFactory(repository)
    }

    override fun provideExerciseDetailViewModelFactory(context: Context): ExerciseDetailViewModelFactory {
        val repository = getRepository(context)
        return ExerciseDetailViewModelFactory(repository)
    }

    override fun provideWorkoutViewModelFactory(context: Context): WorkoutViewModelFactory {
        val repository = getRepository(context)
        return WorkoutViewModelFactory(repository)
    }

    override fun provideWorkoutEditTemplateViewModelFactory(context: Context): WorkoutEditTemplateViewModelFactory {
        val repository = getRepository(context)
        return WorkoutEditTemplateViewModelFactory(repository)
    }
}

private object Lock

@Volatile private var currentInjector: ViewModelFactoryProvider =
    DefaultViewModelProvider

@VisibleForTesting
private fun setInjectorForTesting(injector: ViewModelFactoryProvider?) {
    synchronized(Lock) {
        currentInjector = injector ?: DefaultViewModelProvider
    }
}