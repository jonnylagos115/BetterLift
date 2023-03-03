package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.adapter.ExerciseListAdapter
import com.hfad.betterlift.databinding.FragmentExerciseAddBinding
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseAddFragment : Fragment() {

    private val TAG = "ExerciseAddFragment"
    private var _binding: FragmentExerciseAddBinding? = null
    private var _adapter:  ExerciseListAdapter? = null

    private val binding get() = _binding!!
    private val adapter get() = _adapter!!

    private val exerciseViewModel: ExerciseViewModel by viewModels {
        Injector.provideExerciseViewModelFactory(requireContext())
    }
    private val workoutEditSharedViewModel: WorkoutEditTemplateViewModel by activityViewModels {
        Injector.provideWorkoutEditTemplateViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentExerciseAddBinding.inflate(inflater, container, false)
        _adapter = ExerciseListAdapter(ItemUiAction.SELECTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.apply {
            exercisesList.adapter = adapter
            exercisesList.layoutManager = LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
        }

        binding.bindState()

            // Add menu items without using the FragmentType Menu APIs
            // Note how we can tie the MenuProvider to the viewLifecycleOwner
            // and an optional Lifecycle.State (here, RESUMED) to indicate when
            // the menu should be visible
    }

    private fun FragmentExerciseAddBinding.bindState() {
        bindExercisesList()

        bindFABSave()
        // Consuming WorkoutEditTemplateViewModel state
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutEditSharedViewModel.uiState.collect { currentState ->
                    if (!currentState.isPrevSelectedIdListEmpty) {
                        currentState.isPrevSelectedIdListEmpty = false
                        exerciseViewModel.clearSelectedExerciseIdList()
                    }
                }
            }
        }
    }

    private fun FragmentExerciseAddBinding.bindExercisesList() {
        // Consuming ExerciseViewModel state
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseViewModel.uiState.collect { currentState ->
                    Log.d(TAG, "exerciseAddUiState consumed")
                    if (currentState.hasExerciseListUpdated) {
                        adapter.addHeaderAndSubmitList(exerciseViewModel.exerciseItems)
                    }
                }
            }
        }
    }

    private fun FragmentExerciseAddBinding.bindFABSave() {
        FABSaveButton.setOnClickListener {
            val selectedExerciseIdList = exerciseViewModel.getSelectedExerciseIdList()
            val action = ExerciseAddFragmentDirections.actionExerciseAddFragmentToWorkoutEditTemplateFragment()
            Log.d(TAG, selectedExerciseIdList.toString())
            workoutEditSharedViewModel.receiveSelectedExerciseIdList(selectedExerciseIdList)
            findNavController().navigate(action)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    /**
     * Frees the binding object when the FragmentType is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "onDestroy() called")
    }
}