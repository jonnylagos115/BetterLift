package com.hfad.betterlift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        val view = binding.root
        return view
    }

    /**
     * Frees the binding object when the Fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}