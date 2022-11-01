package com.hfad.betterlift.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView

import com.hfad.betterlift.databinding.FragmentExercisesListItemBinding
import com.hfad.betterlift.models.Exercise
import com.hfad.betterlift.data.ExerciseRepo
import com.hfad.betterlift.ui.exercise.ExercisesFragmentDirections

class ExercisesFragmentAdapter :
    RecyclerView.Adapter<ExercisesFragmentAdapter.ExercisesViewHolder>()
{
    class ExercisesViewHolder(private val binding: FragmentExercisesListItemBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(exercise: Exercise) {
            binding.exerciseNameTextView.text = exercise.exerciseName
            binding.muscleGroupNameTextView.text = exercise.muscleGroupName
            binding.exerciseImageView.setImageResource(exercise.imageReferenceId)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentExercisesListItemBinding.inflate(layoutInflater, parent, false)
        return ExercisesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bind(ExerciseRepo.exercise[position])

        holder.itemView.setOnClickListener {
            val exercise = ExerciseRepo.exercise[position]

            val action = ExercisesFragmentDirections.actionExercisesFragmentToExerciseDetailFragment(exercise = exercise, exerciseLabel = exercise.exerciseName)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount() = ExerciseRepo.exercise.size
}