package com.hfad.betterlift.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.databinding.WorkoutRowListItemBinding
import com.hfad.betterlift.domain.Exercise

class WorkoutRowItemListAdapter(
    private var exercise: Exercise
    ) : RecyclerView.Adapter<WorkoutRowItemListAdapter.WorkoutRowListViewHolder>() {

    private val TAG = "WorkoutRListAdapter"

    inner class WorkoutRowListViewHolder(private val binding: WorkoutRowListItemBinding)
        : RecyclerView.ViewHolder(binding.root){

        init {
            binding.weightEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(weight: Editable?) {
                    exercise.numberOfPounds.add(binding.weightEditText.text.toString().toInt())
                }
            })

            binding.repsEditText.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    exercise.numberOfReps.add(binding.weightEditText.text.toString().toInt())
                }

            })
        }
            fun bind(exercise: Exercise){
                binding.setTextView.text = exercise.numberOfSets.toString()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutRowListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WorkoutRowListItemBinding.inflate(layoutInflater, parent, false)
        return WorkoutRowListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutRowListViewHolder, position: Int) {
        holder.bind(exercise)
    }

    override fun getItemCount(): Int = exercise.numberOfSets

    fun addNewWorkoutRowItem() {
        exercise.numberOfSets++
        notifyItemInserted(itemCount-1)
    }
}