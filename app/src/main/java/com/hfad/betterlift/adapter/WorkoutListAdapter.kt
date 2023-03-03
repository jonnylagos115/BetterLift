package com.hfad.betterlift.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.databinding.WorkoutListItemBinding
import com.hfad.betterlift.viewmodels.WorkoutItemUiState

class WorkoutListAdapter() :
    ListAdapter<WorkoutItemUiState, WorkoutListAdapter.WorkoutViewHolder>(WorkoutListDiffCallback())
{
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
        /**
         * holder.itemView.setOnClickListener {
         *      val workout = WorkoutRepo.workout[position]
         *
         *      val action = {go to nav_graph to set up navigation sequence to start workout screen}
         */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder.from(parent)
    }
    class WorkoutViewHolder private constructor(private val binding: WorkoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(workout: WorkoutItemUiState) {
            binding.apply {
                titleTemplateTextView.text = workout.templateTitle
                lastPerformedTextView.text = "2 days ago"
                //workoutTemplateInfoTextView.text = parseInfoTextView(workout)
            }
        }

        companion object {
            fun from(parent: ViewGroup): WorkoutViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = WorkoutListItemBinding.inflate(layoutInflater, parent, false)
                return WorkoutViewHolder(binding)
            }
        }

       /* private fun parseInfoTextView(workout: WorkoutItemUiState): String {
            var strInfo = StringBuilder().apply {

                for (exercise in workout.exerciseList) {
                    append(exercise.numberOfSets)
                    append(" x ")
                    append(exercise.exerciseName)
                    append("\n")
                }
            }.toString()
            return strInfo
        }*/
    }
}

class WorkoutListDiffCallback : DiffUtil.ItemCallback<WorkoutItemUiState>() {
    override fun areItemsTheSame(oldItem: WorkoutItemUiState, newItem: WorkoutItemUiState): Boolean {
        return oldItem === newItem
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WorkoutItemUiState, newItem: WorkoutItemUiState): Boolean {
        return oldItem.workoutId == newItem.workoutId
    }
}