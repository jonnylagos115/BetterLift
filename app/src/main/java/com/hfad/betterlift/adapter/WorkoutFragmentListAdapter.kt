package com.hfad.betterlift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.data.WorkoutRepo
import com.hfad.betterlift.databinding.FragmentWorkoutTemplateListItemBinding
import com.hfad.betterlift.models.Workout

class WorkoutFragmentListAdapter()
    : RecyclerView.Adapter<WorkoutFragmentListAdapter.WorkoutViewHolder>()
{
    inner class WorkoutViewHolder(private val binding: FragmentWorkoutTemplateListItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(workout: Workout) {
            binding.titleTemplateTextView.text = workout.templateTitle
            binding.lastPerformedTextView.text = "2 days ago"
            binding.workoutTemplateInfoTextView.text = parseInfoTextView(workout)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentWorkoutTemplateListItemBinding.inflate(layoutInflater, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.bind(WorkoutRepo.workoutList[position])
        /**
         * holder.itemView.setOnClickListener {
         *      val workout = WorkoutRepo.workout[position]
         *
         *      val action = {go to nav_graph to set up navigation sequence to start workout screen}
         */
    }
    override fun getItemCount() = WorkoutRepo.workoutList.size

   private fun parseInfoTextView(workout: Workout): String {
        var strInfo = StringBuilder().apply {
            for (exercise in workout.exerciseList) {
                append(exercise.numberOfSets)
                append(" x ")
                append(exercise.exerciseName)
                append("\n")
            }
        }.toString()
        return strInfo
    }
}