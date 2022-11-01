package com.hfad.betterlift.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.FragmentExercisesListItemBinding
import com.hfad.betterlift.models.Exercise
import com.hfad.betterlift.data.ExerciseRepo

class WorkoutAddExercisesAdapter :
    RecyclerView.Adapter<WorkoutAddExercisesAdapter.WorkoutAddExercisesViewHolder>()
{
    var item_selected_position = -1
    private val TAG1 = "SetSelection()"
    private val TAG2 = "onBindViewHolder()"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutAddExercisesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentExercisesListItemBinding.inflate(layoutInflater, parent, false)
        return WorkoutAddExercisesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutAddExercisesViewHolder, position: Int) {
        holder.bind(ExerciseRepo.exercise[position])

        Log.d(TAG2, "position: $position")
       if (item_selected_position == position) {
           Log.d(TAG2, "item_selection_position == position: $item_selected_position")
           holder.changeImageOnSelectedItem(0, ExerciseRepo.exercise[position].imageReferenceId)
        }
    }

    override fun getItemCount(): Int = ExerciseRepo.exercise.size

    inner class WorkoutAddExercisesViewHolder(private val binding: FragmentExercisesListItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        init {
            itemView.setOnClickListener { setSelection(adapterPosition)}
        }
        fun bind(exercise: Exercise) {
            binding.exerciseNameTextView.text = exercise.exerciseName
            binding.muscleGroupNameTextView.text = exercise.muscleGroupName
            binding.exerciseImageView.setImageResource(exercise.imageReferenceId)
        }
        fun changeImageOnSelectedItem(i: Int, imageResId: Int) {
            val drawableImage =
                when (i){
                    0 -> R.drawable.icons8_done_50
                    else -> imageResId
                }
            binding.exerciseImageView.setImageResource(drawableImage)
        }
    }

    private fun setSelection(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return
        Log.d(TAG1,"before 1st notifyItemChanged() call")
        notifyItemChanged(adapterPosition)
    }
}