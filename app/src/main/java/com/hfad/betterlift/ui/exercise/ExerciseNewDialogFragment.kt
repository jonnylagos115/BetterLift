package com.hfad.betterlift.ui.exercise

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.FragmentNewExerciseBinding
import com.hfad.betterlift.models.Exercise
import com.hfad.betterlift.data.ExerciseRepo

class ExerciseNewDialogFragment : DialogFragment() {

    private lateinit var onClickListener: OnClickListener

    interface OnClickListener {
        fun onDialogPositiveClick()
        fun onDialogNegativeClick()
    }

    companion object {
        fun create(onClickListener : OnClickListener
         ): ExerciseNewDialogFragment
        {
            return ExerciseNewDialogFragment().apply {
                this.onClickListener = onClickListener
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?
    ): Dialog
    {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val layoutInflater = LayoutInflater.from(requireContext())
            val binding = FragmentNewExerciseBinding.inflate(layoutInflater)

            val muscleGroupTypes = resources.getStringArray(R.array.muscle_groups)
            val arrayAdapter = ArrayAdapter(requireContext(),
                R.layout.dropdown_item_muscle_group_type, muscleGroupTypes)
            binding.selectedItemTextView.setAdapter(arrayAdapter)
            binding.muscleGroupDropDownMenu

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.save_button
                ) {_, _ ->
                    val exerciseText = binding.exerciseEditText.text.toString()
                    val selectedMuscleGroup = binding.selectedItemTextView.text.toString()
                    val exercise = Exercise(selectedMuscleGroup, exerciseText, 0)
                    if (exerciseText != "") {
                        ExerciseRepo.exercise.add(exercise)
                    }
                    this.onClickListener.onDialogPositiveClick()
                }
                .setNegativeButton(
                    R.string.cancel_button
                ) { _, _ ->
                    //Do something.
                    dismiss()
                }
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}