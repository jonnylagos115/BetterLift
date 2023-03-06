package com.hfad.betterlift.ui.workout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.WorkoutEditTemplateItemListAdapter
import com.hfad.betterlift.databinding.FragmentWorkoutEditTemplateBinding
import com.hfad.betterlift.domain.Workout
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.ExerciseViewModel
import com.hfad.betterlift.viewmodels.WorkoutEditTemplateViewModel
import com.hfad.betterlift.viewmodels.WorkoutViewModel
import kotlinx.coroutines.launch

class WorkoutEditTemplateFragment : Fragment() {
    private val TAG = "WorkoutNewTemplate"
    private var _binding: FragmentWorkoutEditTemplateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val navArgs: WorkoutEditTemplateFragmentArgs by navArgs()
    private lateinit var adapter: WorkoutEditTemplateItemListAdapter
    private val workoutEditViewModel: WorkoutEditTemplateViewModel by activityViewModels {
        Injector.provideWorkoutEditTemplateViewModelFactory(requireContext())
    }
    private val workoutSharedViewModel: WorkoutViewModel by activityViewModels {
        Injector.provideWorkoutViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutEditTemplateBinding.inflate(inflater, container, false)
        adapter = WorkoutEditTemplateItemListAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lateinit var title: String
        lateinit var workoutNotes: String
        val selectedIdList = navArgs.selectedIdList?.toList()

        if (selectedIdList != null)
            workoutEditViewModel.receiveSelectedExerciseIdList(selectedIdList)
        binding.apply {
            title = titleNewTemplateEditText.text.toString()
            workoutNotes = workoutNoteEditText.text.toString()
            newWorkoutTemplatesList.adapter = adapter
            newWorkoutTemplatesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        }
        subscribeToUiState()
        setupAddExerciseButton()
        setupMenuItemDropdownList()
    }

    private fun subscribeToUiState()  {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutEditViewModel.uiState.collect { state ->
                    adapter.submitList(state.workoutEditTemplateItems)
                }
            }
        }
    }

    private fun setupAddExerciseButton() {
        binding.addExerciseButton.setOnClickListener {
            val action = WorkoutEditTemplateFragmentDirections.actionNavWorkoutNewTemplateFragmentToExerciseAddFragment()
            findNavController().navigate(action)
        }
    }

    private fun setupMenuItemDropdownList() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.workout_tab_app_bar_menu, menu)
            }

            override fun onPrepareMenu(menu: Menu) {
                super.onPrepareMenu(menu)
                val createWorkoutItem = menu.findItem(R.id.action_workoutNewTemplateFragment)
                val saveWorkoutItem = menu.findItem(R.id.workoutFragment)
                createWorkoutItem.isVisible = false
                saveWorkoutItem.isVisible = true
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.workoutFragment -> {
                        findNavController().popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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