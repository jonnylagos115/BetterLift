package com.hfad.betterlift.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.repository.Repository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the Exercise repository and an up-to-date list of all items.
 *
 */
private const val ITEMS_PER_PAGE = 50
private const val TAG = "ExerciseVM"

class ExerciseViewModel internal constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExercisesUiState())
    val uiState: StateFlow<ExercisesUiState> = _uiState.asStateFlow()

    private val latestExerciseList: SharedFlow<List<ExerciseItemUiState>> = repository.exerciseStream.map {
        it.asExerciseItemUiState()
    }.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1
    )

    lateinit var currentExerciseList: List<ExerciseItemUiState>

    init {
    }

    //private lateinit var exerciseDetailInfo: (Int, String) -> (Int, String)

   /* val exerciseItems: Flow<PagingData<Exercise>> = Pager(
        config = PagingConfig(pageSize = ITEMS_PER_PAGE, enablePlaceholders = false),
        pagingSourceFactory = {exerciseRepo.exercisePagingSource() }
    )
        .flow
        .cachedIn(viewModelScope)*/

    init {
        Log.d(TAG, "ExerciseViewModel initializing")
        getLatestExerciseListState()
    }

    private fun onExerciseItemClicked(item: ExerciseItemUiState) {
        navigateToExerciseDetail(item)
    }

    private fun navigateToExerciseDetail(item: ExerciseItemUiState) {
        _uiState.update {
            it.copy(navigateToExerciseDetail = true, destinationItemDetail = item)
        }
    }

    fun resetNavigateStatus() {
        _uiState.update {
            it.copy(navigateToExerciseDetail = false, navigateToWorkoutEdit = false)
        }
    }

    private fun getLatestExerciseListState(){
        viewModelScope.launch {
            latestExerciseList.collect { list ->
                _uiState.update { state ->
                    state.copy(latestExerciseItemList = list, submitLatestList = true)
                }
            }
        }
    }

    fun toggleSubmitLatestListStatus(status: Boolean) {
        _uiState.update {
            it.copy(submitLatestList = status)
        }
    }
    private val latestSelectedExerciseIdList = mutableListOf<Int>()

    fun bindSelectedIdListToUiState() {
        _uiState.update { state ->
            state.copy(latestSelectedExerciseIdList = latestSelectedExerciseIdList, navigateToWorkoutEdit = true)
        }
    }

    fun getSelectedExerciseIdList() = latestSelectedExerciseIdList

    fun clearSelectedExerciseIdList() = latestSelectedExerciseIdList.clear()

    fun retrieveExerciseItem(id: Int): StateFlow<Exercise?> {
        return repository.retrieveExerciseItem(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )
    }

    private fun deleteExerciseItem(exerciseItem: Exercise) {
        viewModelScope.launch {
            repository.deleteExerciseItem(exerciseItem)
        }
    }

    private fun selectedExerciseItem(exerciseItem: Exercise) {

    }

    fun isEntryValid(exerciseName: String, muscleGroupName: String): Boolean {
        if (exerciseName.isBlank() || muscleGroupName.isBlank()) {
            return false
        }
        return true
    }

    private fun insertExerciseItem(exercise: Exercise) {
        viewModelScope.launch {
            repository.insertExerciseDB(exercise)
        }
    }

   private fun getNewItemEntry(
        exerciseName: String,
        muscleGroupName: String): Exercise {
        return Exercise(
            exerciseName = exerciseName,
            muscleGroupName = muscleGroupName
        )
    }

    fun addNewItem(
        exerciseName: String,
        muscleGroupName: String) {
        val newItem = getNewItemEntry(exerciseName, muscleGroupName)
        insertExerciseItem(newItem)
    }

    private fun deleteAllExerciseItems() {
        viewModelScope.launch {
            repository.deleteAllExerciseItems()
        }
    }

    fun deleteExerciseItems() {
        deleteAllExerciseItems()
    }

    fun deleteAllNetworkRequestRecordDB() {
        viewModelScope.launch {
            repository.deleteAllNetworkRequestRecordDB()
        }
    }

    private fun updateItem(exercise: Exercise) {
        viewModelScope.launch {
            repository.update(exercise)
        }
    }

    /**
     * Conversion for domain Exercise objects to ExerciseItemUiState objects, these fields
     * include lambda functions for item action behaviors to then be scoped in this view model
     * to handle any business logic
     */
    private fun List<Exercise>.asExerciseItemUiState() : List<ExerciseItemUiState> {
        Log.d(TAG, "Converting List<Exercise> to List<ExerciseItemUiState>")
        return map {
            ExerciseItemUiState(
                exerciseId = it.exerciseId,
                exerciseName = it.exerciseName,
                muscleGroupName = it.muscleGroupName,
                description = it.description,
                imageResUrl = it.imageResUrl,
                onClickItemUiAction = {action ->
                    when (action) {
                        is ExerciseItemUiAction.Navigate -> {
                            onExerciseItemClicked(action.exerciseItem)
                        }
                        is ExerciseItemUiAction.Selection -> {
                            Log.d(TAG,"Adding item to latestSelectedExerciseIdList")
                            when(action.isSelectedItem) {
                                true -> latestSelectedExerciseIdList.add(action.exerciseId)
                                false ->latestSelectedExerciseIdList.remove(action.exerciseId)
                            }
                        }
                    }
                },
                deleteExerciseItem = { deleteExerciseItem(it) }
            )
        }
    }
}

data class ExercisesUiState(
    val navigateToExerciseDetail: Boolean = false,
    val navigateToWorkoutEdit: Boolean = false,
    val destinationItemDetail: ExerciseItemUiState? = null,
    val submitLatestList: Boolean = false,
    val latestExerciseItemList: List<ExerciseItemUiState> = listOf(),
    val latestSelectedExerciseIdList: List<Int> = listOf()
)

data class ExerciseItemUiState(
    val exerciseId: Int,
    val exerciseName: String,
    val muscleGroupName: String,
    val description: String,
    val imageResUrl: String?,
    var navigateAction: ExerciseItemUiAction.Navigate? = null,
    var selectionAction: ExerciseItemUiAction.Selection? = null,
    val onClickItemUiAction: (ExerciseItemUiAction) -> Unit,
    val deleteExerciseItem: () -> Unit,
)

enum class ItemUiAction {
    NAVIGATE,
    SELECTION
}

/**
 * The different actions that may occur specifically for itemView.onClickListener in ExerciseListAdapter
 * Other onClick events that pertain to components within the itemView (i.e. buttons, etc) can be invoked
 * by lambda function field in ExerciseItemUiState()
 */

sealed class ExerciseItemUiAction {
    data class Navigate(val exerciseItem: ExerciseItemUiState): ExerciseItemUiAction()
    data class Selection(val exerciseId: Int, var isSelectedItem: Boolean = true): ExerciseItemUiAction()
}


class ExerciseViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = ExerciseViewModel(repository) as T
}