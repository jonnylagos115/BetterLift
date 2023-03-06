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

    private val _uiState = MutableStateFlow(WorkoutEditUiState())
    val uiState: StateFlow<WorkoutEditUiState> = _uiState.asStateFlow()

    private val workoutEditExerciseList = MutableSharedFlow<List<WorkoutEditItemUiState>>()

    init {
        getLatestWorkoutEditListState()
    }

    private fun getLatestWorkoutEditListState() {
        viewModelScope.launch {
            workoutEditExerciseList.collect { list ->
                _uiState.update { state ->
                    state.workoutEditTemplateItems
                    state.copy(workoutEditTemplateItems = list)
                }
            }
        }
    }

    /**
     * Remember: exerciseIdList: MutableList<Int> is passed by reference from ExerciseViewModel, any changes made to it's contents (i.e. clearing out all elements) will affect how
     * we bind the values to the workout template view items. Think of an other way to clear out the selection id list if use case where add exercises buttons is clicked again to add on to the
     * already selected workout template view items, not clear it out.
     */
    fun receiveSelectedExerciseIdList(exerciseIdList: List<Int>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                val list = repository.retrieveSelectedExerciseItems(exerciseIdList).asWorkoutEditTemplateItemUiState()
                workoutEditExerciseList.emit(list)
            }
            Log.d(TAG, "selectedIdList received")
        }
    }

    private fun List<Exercise>.asWorkoutEditTemplateItemUiState() : List<WorkoutEditItemUiState> {
        Log.d(TAG, this.toString())
        return map {
            WorkoutEditItemUiState(exerciseName = it.exerciseName)
        }
    }
}

data class WorkoutEditUiState(
    var templateTitle: String = "",
    var templateNotes: String = "",
    val workoutEditTemplateItems: List<WorkoutEditItemUiState> = listOf(),
    var isPrevSelectedIdListEmpty: Boolean = true
)

data class WorkoutEditItemUiState(
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