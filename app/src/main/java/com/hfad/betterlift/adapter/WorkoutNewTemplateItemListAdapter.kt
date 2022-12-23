package com.hfad.betterlift.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.databinding.FragmentWorkoutNewTemplateListItemBinding
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.domain.Workout

class WorkoutNewTemplateItemListAdapter(
    private val workout: Workout
) : RecyclerView.Adapter<WorkoutNewTemplateItemListAdapter.WorkoutNewTemplateItemViewHolder>() {

    private val TAG = "WorkoutNewTItemAdapter"

    private lateinit var exercise: Exercise

    inner class WorkoutNewTemplateItemViewHolder(private val binding: FragmentWorkoutNewTemplateListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var workoutRowItemListAdapter: WorkoutRowItemListAdapter

        init {
            binding.addSetButton.setOnClickListener {
                workoutRowItemListAdapter.addNewWorkoutRowItem()
            }
        }
        fun bind(exercise: Exercise) {
            binding.exerciseNameTextView.text = exercise.exerciseName
            workoutRowItemListAdapter = WorkoutRowItemListAdapter(exercise)
            binding.workoutRowList.adapter = workoutRowItemListAdapter
            binding.workoutRowList.layoutManager = LinearLayoutManager(itemView.context, RecyclerView.VERTICAL, false)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutNewTemplateItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentWorkoutNewTemplateListItemBinding.inflate(layoutInflater, parent, false)
        Log.d(TAG, "onCreateViewHolder")
        return WorkoutNewTemplateItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutNewTemplateItemViewHolder, position: Int) {
        exercise = workout.exerciseList[position]
        holder.bind(exercise)
    }


    override fun getItemCount() =  workout.exerciseList.size

    fun getWorkout() = workout
}