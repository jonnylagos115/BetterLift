package com.hfad.betterlift.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.databinding.WorkoutEditSetListItemBinding
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.viewmodels.WorkoutEditSetItemUiState

private const val TAG = "WorkoutRListAdapter"

class WorkoutEditSetItemListAdapter() :
    ListAdapter<WorkoutEditSetItemUiState, WorkoutEditSetItemListAdapter.WorkoutEditSetItemViewHolder>(WorkoutEditSetItemDiffCallback()) {

    override fun onBindViewHolder(holder: WorkoutEditSetItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutEditSetItemViewHolder {
        return WorkoutEditSetItemViewHolder.from(parent)
    }

    class WorkoutEditSetItemViewHolder private constructor(private val binding: WorkoutEditSetListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(item: WorkoutEditSetItemUiState) {
            binding.apply {
                setTextView.text = item.setId.toString()
                if (weightEditText.text.toString().isNotEmpty() && repsEditText.text.toString().isNotEmpty()){
                    item.editSetWeight = weightEditText.text.toString().toInt()
                    item.editSetReps = repsEditText.text.toString().toInt()
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutEditSetItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WorkoutEditSetListItemBinding.inflate(layoutInflater, parent, false)
                return WorkoutEditSetItemViewHolder(binding)
            }
        }
    }
}

class WorkoutEditSetItemDiffCallback: DiffUtil.ItemCallback<WorkoutEditSetItemUiState>() {
    override fun areItemsTheSame(oldItem: WorkoutEditSetItemUiState, newItem: WorkoutEditSetItemUiState): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: WorkoutEditSetItemUiState, newItem: WorkoutEditSetItemUiState): Boolean {
        return oldItem.setId == newItem.setId
    }
}