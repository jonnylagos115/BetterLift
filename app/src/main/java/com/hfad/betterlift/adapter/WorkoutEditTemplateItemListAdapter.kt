package com.hfad.betterlift.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.databinding.WorkoutEditTemplateListItemBinding
import com.hfad.betterlift.viewmodels.WorkoutEditSetItemUiState
import com.hfad.betterlift.viewmodels.WorkoutEditItemUiState

private const val TAG = "WorkoutNewTItemAdapter"

class WorkoutEditTemplateItemListAdapter() :
    ListAdapter<WorkoutEditItemUiState, WorkoutEditTemplateItemListAdapter.WorkoutEditTemplateItemViewHolder>(WorkoutEditTemplateItemDiffCallback())
{

    override fun onBindViewHolder(holder: WorkoutEditTemplateItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutEditTemplateItemViewHolder {
        return WorkoutEditTemplateItemViewHolder.from(parent)
    }

    class WorkoutEditTemplateItemViewHolder private constructor(private val binding: WorkoutEditTemplateListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val adapter: WorkoutEditSetItemListAdapter = WorkoutEditSetItemListAdapter()

        init {
            binding.workoutEditSetList.adapter = adapter
            binding.workoutEditSetList.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
        }

        fun bind(item: WorkoutEditItemUiState) {
            binding.apply {
                exerciseNameTextView.text = item.exerciseName
                adapter.submitList(item.workoutEditSetItems)
                addSetButton.setOnClickListener {
                    val addSetIndex = item.workoutEditSetItems.size
                    item.workoutEditSetItems.add(addSetIndex, WorkoutEditSetItemUiState(addSetIndex+1))
                    adapter.submitList(item.workoutEditSetItems)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutEditTemplateItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WorkoutEditTemplateListItemBinding.inflate(layoutInflater, parent, false)
                return WorkoutEditTemplateItemViewHolder(binding)
            }
        }
    }
}

class WorkoutEditTemplateItemDiffCallback : DiffUtil.ItemCallback<WorkoutEditItemUiState>() {
    override fun areItemsTheSame(oldItem: WorkoutEditItemUiState, newItem: WorkoutEditItemUiState): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: WorkoutEditItemUiState, newItem: WorkoutEditItemUiState): Boolean {
        return oldItem.exerciseName == newItem.exerciseName
    }
}