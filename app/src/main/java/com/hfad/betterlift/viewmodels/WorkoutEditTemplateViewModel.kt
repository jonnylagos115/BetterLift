package com.hfad.betterlift.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "WorkoutEditVM"
class WorkoutEditTemplateViewModel internal constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutEditTemplateUiState())
    val uiState: StateFlow<WorkoutEditTemplateUiState> = _uiState.asStateFlow()

    private val latestSelectedExerciseListState = MutableStateFlow<List<WorkoutEditTemplateItemUiState>>(
        listOf()
    )

    init {
        subscribeLatestSelectedExerciseState()
    }

    private fun subscribeLatestSelectedExerciseState() {
        viewModelScope.launch {
            latestSelectedExerciseListState.collect { latestList ->
                Log.d(TAG, "consuming latest selected exercise list state")
                _uiState.update { currentState ->
                    currentState.workoutEditTemplateItems.addAll(latestList)
                    currentState.isPrevSelectedIdListEmpty = false
                    currentState.copy(workoutEditTemplateItems = currentState.workoutEditTemplateItems)
                }
            }
        }
    }

    /**
     * Remember: exerciseIdList: MutableList<Int> is passed by reference from ExerciseViewModel, any changes made to it's contents (i.e. clearing out all elements) will affect how
     * we bind the values to the workout template view items. Think of an other way to clear out the selection id list if use case where add exercises buttons is clicked again to add on to the
     * already selected workout template view items, not clear it out.
     */
    fun receiveSelectedExerciseIdList(exerciseIdList: MutableList<Int>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                latestSelectedExerciseListState.value = repository.retrieveSelectedExerciseItems(exerciseIdList).asWorkoutEditTemplateItemUiState()
            }
            Log.d(TAG, "selectedIdList received")
        }
    }

    private fun List<Exercise>.asWorkoutEditTemplateItemUiState() : List<WorkoutEditTemplateItemUiState> {
        Log.d(TAG, this.toString())
        return map {
            WorkoutEditTemplateItemUiState(exerciseName = it.exerciseName)
        }
    }
}

data class WorkoutEditTemplateUiState(
    var templateTitle: String = "",
    var templateNotes: String = "",
    val workoutEditTemplateItems: MutableList<WorkoutEditTemplateItemUiState> = mutableListOf(),
    var isPrevSelectedIdListEmpty: Boolean = true
)

data class WorkoutEditTemplateItemUiState(
    val exerciseName: String,
    val workoutEditSetItems: MutableList<WorkoutEditSetItemUiState> = mutableListOf(WorkoutEditSetItemUiState())
)

data class WorkoutEditSetItemUiState(
    val setId: Int = 1,
    var editSetWeight: Int = 0,
    var editSetReps: Int = 0
)

class WorkoutEditTemplateViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = WorkoutEditTemplateViewModel(repository) as T
}