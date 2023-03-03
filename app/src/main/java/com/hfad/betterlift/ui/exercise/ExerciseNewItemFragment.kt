package com.hfad.betterlift.ui.exercise

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hfad.betterlift.databinding.FragmentExerciseNewItemBinding
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.ExerciseViewModel

class ExerciseNewItemFragment : Fragment() {

    private var _binding: FragmentExerciseNewItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExerciseViewModel by activityViewModels {
        Injector.provideExerciseViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExerciseNewItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         binding.saveAction.setOnClickListener {
            addNewItem()
        }
    }

    /*private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.itemExerciseName.text.toString(),
            binding.selectedItemTextView.text.toString())
    }*/

    private fun addNewItem() {
        viewModel.addNewItem(
                binding.itemExerciseName.text.toString(),
                binding.selectedItemTextView.text.toString()
            )
        val action = ExerciseNewItemFragmentDirections.actionExerciseNewItemFragmentToNavExercise()
        findNavController().navigate(action)
    }

    /**
     * Called before fragment is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }
}