package com.hfad.betterlift.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R

import com.hfad.betterlift.databinding.FragmentExercisesListItemBinding
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.repository.ExerciseRepo
import com.hfad.betterlift.const.FragmentType
import com.hfad.betterlift.ui.exercise.ExerciseFragmentDirections

class ExerciseFragmentListAdapter (
    private val fragment: Int
    ): RecyclerView.Adapter<ExerciseFragmentListAdapter.ExercisesViewHolder>()
{
    private var item_selected_position = -1
    private val TAG = "ExercisesFragAdapter"
    private var selectedListItems = mutableListOf<Exercise>()


    inner class ExercisesViewHolder(private val binding: FragmentExercisesListItemBinding) :
        RecyclerView.ViewHolder(binding.root){

        init {
            if (fragment == FragmentType.ADD_EXERCISES) {
                itemView.setOnClickListener { setSelection(adapterPosition)}
            } else {
                itemView.setOnClickListener {
                    val exercise = ExerciseRepo.exerciseList[adapterPosition]
                    val action = ExerciseFragmentDirections.actionNavExerciseToNavExerciseDetail(exercise = exercise, exerciseLabel = exercise.exerciseName.toString())
                    itemView.findNavController().navigate(action)
                }
            }
        }
        fun bind(exercise: Exercise) {
            Log.d(TAG, "Inside bind()")
            Log.d(TAG, "exerciseNameTextView.text = ${binding.exerciseNameTextView.text}")
            binding.exerciseNameTextView.text = exercise.exerciseName
            binding.muscleGroupNameTextView.text = exercise.muscleGroupName
            if (item_selected_position == -1) {
                binding.exerciseImageView.setImageResource(exercise.imageReferenceId)
            }else{
                changeImageOnSelectedItem(exercise.imageReferenceId, exercise)
            }
        }

        private fun changeImageOnSelectedItem(imageResId: Int, exercise: Exercise) {
            val drawableImage: Int

            if (exercise.isSelected == false) {
                drawableImage = R.drawable.icons8_done_50
                exercise.isSelected = true
                selectedListItems.add(exercise)
                Log.d(TAG, "Tag: default to selected")
            }else {
                drawableImage = imageResId
                exercise.isSelected = false
                selectedListItems.remove(exercise)
                Log.d(TAG, "Tag: selected to default")
            }
            binding.exerciseImageView.setImageResource(drawableImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = FragmentExercisesListItemBinding.inflate(layoutInflater, parent, false)
        Log.d(TAG, "New viewholder created")
        return ExercisesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.bind(ExerciseRepo.exerciseList[position])
    }

    override fun getItemCount() = ExerciseRepo.exerciseList.size

    private fun setSelection(adapterPosition: Int) {
        if (adapterPosition == RecyclerView.NO_POSITION) return
        Log.d(TAG, "itemView clicked")
        item_selected_position = adapterPosition
        notifyItemChanged(adapterPosition)
    }

    fun getSelectedItems(): MutableList<Exercise> {
        for (exercise in selectedListItems){
            exercise.isSelected = false
        }
        return selectedListItems
    }
}