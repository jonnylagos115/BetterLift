package com.hfad.betterlift.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hfad.betterlift.domain.Workout
import com.hfad.betterlift.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "WorkoutVM"

class WorkoutViewModel internal constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState.asStateFlow()

    private val workoutItems: StateFlow<List<Workout>> = repository.workoutStream.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = listOf()
    )

    init {
        loadWorkoutListData()
    }

    private fun loadWorkoutListData(){
        viewModelScope.launch {
            Log.d(TAG, "loadWorkoutData() called")
            workoutItems.collect { workouts ->
                _uiState.update {
                    it.copy(workoutItems = workouts.map { currentItem ->
                        WorkoutItemUiState(
                            workoutId = currentItem.workoutId,
                            templateTitle = currentItem.templateTitle,
                            workoutNotes = currentItem.workoutNotes,
                            selectedExercisesIdList = currentItem.selectedExercisesIdList
                        )
                    })
                }

            }
        }
    }

    fun insertNewWorkoutTemplate(workout: Workout){
        viewModelScope.launch {
            repository.insertWorkoutDB(workout)
        }
    }

}

data class WorkoutUiState(
    val workoutItems: List<WorkoutItemUiState> = listOf()
)

data class WorkoutItemUiState(
    val workoutId: Int,
    val templateTitle: String,
    val workoutNotes: String,
    val selectedExercisesIdList: List<Int>
)

class WorkoutViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = WorkoutViewModel(repository) as T
}