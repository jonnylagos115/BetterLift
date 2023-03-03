package com.hfad.betterlift.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.ui.exercise.Alphabet
import com.hfad.betterlift.ui.exercise.AlphabetHeaderViewHolder
import com.hfad.betterlift.ui.exercise.ExerciseViewHolder
import com.hfad.betterlift.ui.exercise.initializeAlphabetSequence
import com.hfad.betterlift.viewmodels.ExerciseItemUiState
import com.hfad.betterlift.viewmodels.ItemUiAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1
private const val TAG = "ExercisesFragAdapter"

class ExerciseListAdapter(val itemUiAction: ItemUiAction) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(ExerciseListDiffCallback())
{
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSubmitList(list: List<ExerciseItemUiState>) {
        adapterScope.launch {
            val exerciseDataItems = list.map { DataItem.ExerciseItem(it) }
            val alphabetDataItems = initializeAlphabetSequence().map { DataItem.AlphabetHeaderItem(it) }
            val dataItems: MutableList<DataItem> = exerciseDataItems.toMutableList()

            Log.d(TAG, "inside addHeaderAndSubmit")
            alphabetDataItems.forEach { alphabetItem ->
                val firstExerciseAlphabet =
                    exerciseDataItems.firstOrNull { it.exerciseItemUiState.exerciseName.first() == alphabetItem.alphabet.character }
                if (firstExerciseAlphabet != null)
                    dataItems.add(dataItems.indexOf(firstExerciseAlphabet), alphabetItem)
            }
            withContext(Dispatchers.Main) {
                submitList(dataItems)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> AlphabetHeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ExerciseViewHolder.from(parent)
            else -> throw java.lang.ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlphabetHeaderViewHolder -> {
                val alphabetHeaderItem = getItem(position) as DataItem.AlphabetHeaderItem
                holder.bind(alphabetHeaderItem.alphabet)
            }
            is ExerciseViewHolder -> {
                val exerciseItem = getItem(position) as DataItem.ExerciseItem
                holder.bind(exerciseItem.exerciseItemUiState, itemUiAction)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.AlphabetHeaderItem -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ExerciseItem -> ITEM_VIEW_TYPE_ITEM
        }
    }
}

class ExerciseListDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem === newItem
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}

sealed class DataItem {
    data class ExerciseItem(val exerciseItemUiState: ExerciseItemUiState): DataItem() {
        override val id = exerciseItemUiState.exerciseId
    }

    data class AlphabetHeaderItem(val alphabet: Alphabet): DataItem() {
        override val id = alphabet.id
    }

    abstract val id: Int
}