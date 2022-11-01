package com.hfad.betterlift.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.adapter.WorkoutViewAdapter
import com.hfad.betterlift.databinding.FragmentWorkoutNewTemplateBinding

class WorkoutNewTemplateFragment : Fragment() {
    private val TAG = "WorkoutNewTemplateFragment"
    private var _binding: FragmentWorkoutNewTemplateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentWorkoutNewTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.newWorkoutTemplatesList.adapter = WorkoutViewAdapter()
        binding.newWorkoutTemplatesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        /**
         * Add action bar icon item that allows user to create and save the current state of template
         */
       binding.addExerciseButton.setOnClickListener {
            val action = WorkoutNewTemplateFragmentDirections.actionWorkoutNewTemplateFragmentToExerciseAddFragment()
            view.findNavController().navigate(action)
           }
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}