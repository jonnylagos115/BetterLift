package com.hfad.betterlift.ui.exercise

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.AlphabetHeaderItemBinding
import com.hfad.betterlift.databinding.FragmentExercisesListItemBinding
import com.hfad.betterlift.viewmodels.ExerciseItemUiAction
import com.hfad.betterlift.viewmodels.ExerciseItemUiState
import com.hfad.betterlift.viewmodels.ItemUiAction

private const val TAG = "ExerciseViewHolder"
class ExerciseViewHolder private constructor(private val binding: FragmentExercisesListItemBinding) :
    RecyclerView.ViewHolder(binding.root){

    fun bind(item: ExerciseItemUiState, itemUiAction: ItemUiAction) {
        binding.apply {
            exerciseNameTextView.text = item.exerciseName
            muscleGroupNameTextView.text = item.muscleGroupName
            exerciseImageView.setImageResource(R.drawable.ic_android)
            deleteItemButton.setOnClickListener {
                item.deleteExerciseItem.invoke()
            }
            itemView.setOnClickListener {
                when (itemUiAction) {
                    ItemUiAction.NAVIGATE -> item.onClickItemUiAction(item.navigateAction ?: ExerciseItemUiAction.Navigate(item).also { item.navigateAction = it })
                    ItemUiAction.SELECTION -> item.onClickItemUiAction(item.selectionAction ?: ExerciseItemUiAction.Selection(item.exerciseId).also { item.selectionAction = it })
                }
                Log.d(TAG, "Out of the when condtional but still inside setOnClickListener{}")
                if (item.selectionAction != null) {
                    Log.d(TAG, "inside")
                    when (item.selectionAction!!.isSelectedItem) {
                        true -> {
                            exerciseImageView.setImageResource(R.drawable.icons8_done_50)
                            item.selectionAction!!.isSelectedItem = false
                            Log.d(TAG, "selected")
                        }
                        false -> {
                            exerciseImageView.setImageResource(R.drawable.ic_android)
                            item.selectionAction!!.isSelectedItem = true
                            Log.d(TAG, "unselected")
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun from(parent: ViewGroup): ExerciseViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = FragmentExercisesListItemBinding.inflate(layoutInflater, parent, false)
            return ExerciseViewHolder(binding)
        }
    }
}

class AlphabetHeaderViewHolder(private val binding: AlphabetHeaderItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Alphabet){
        binding.apply {
            headerText.text = item.character.toString()
        }
    }

    companion object {
        fun from(parent: ViewGroup): AlphabetHeaderViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AlphabetHeaderItemBinding.inflate(layoutInflater, parent, false)
            return AlphabetHeaderViewHolder(binding)
        }
    }
}

data class Alphabet(
    val id: Int = 0,
    val character: Char = ' '
)

fun initializeAlphabetSequence(): List<Alphabet> {
    val alphabetList = mutableListOf<Alphabet>()
    var id = -1
    for (char in 'A'..'Z'){
        alphabetList += Alphabet(id, char)
        id--
    }
    return alphabetList
}