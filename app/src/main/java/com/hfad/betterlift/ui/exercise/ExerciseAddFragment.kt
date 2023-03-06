package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.launch

class ExerciseAddFragment : Fragment() {

    private val TAG = "ExerciseAddFragment"
    private var _binding: FragmentExerciseAddBinding? = null
    private var _adapter:  ExerciseListAdapter? = null

    private val binding get() = _binding!!
    private val adapter get() = _adapter!!

    private val exerciseViewModel: ExerciseViewModel by activityViewModels {
        Injector.provideExerciseViewModelFactory(requireContext())
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
            exercisesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        }

        // Refresh view state for list each time fragment is navigated to as destination
        exerciseViewModel.toggleSubmitLatestListStatus(true)
        subscribeToUiState()
    }

    private fun subscribeToUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                exerciseViewModel.uiState.collect { state ->
                    if (state.submitLatestList) {
                        adapter.addHeaderAndSubmitList(state.latestExerciseItemList)
                        exerciseViewModel.toggleSubmitLatestListStatus(false)
                    }
                    if (state.navigateToWorkoutEdit) {
                        val action = ExerciseAddFragmentDirections.actionExerciseAddFragmentToWorkoutEditTemplateFragment(state.latestSelectedExerciseIdList.toIntArray())
                        findNavController().navigate(action)
                        exerciseViewModel.resetNavigateStatus()
                    }
                }
            }
        }
        setupFABSave()
    }

    private fun setupFABSave() {
        binding.FABSaveButton.setOnClickListener {
            exerciseViewModel.bindSelectedIdListToUiState()
        }
    }

    private fun View.setVisibility(visible: Boolean) {
        visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
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