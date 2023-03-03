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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.WorkoutEditTemplateItemListAdapter
import com.hfad.betterlift.databinding.FragmentWorkoutEditTemplateBinding
import com.hfad.betterlift.domain.Workout
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.WorkoutEditTemplateViewModel
import com.hfad.betterlift.viewmodels.WorkoutViewModel
import kotlinx.coroutines.launch

class WorkoutEditTemplateFragment : Fragment() {
    private val TAG = "WorkoutNewTemplate"
    private var _binding: FragmentWorkoutEditTemplateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: WorkoutEditTemplateItemListAdapter
    private val workoutEditTemplateSharedViewModel: WorkoutEditTemplateViewModel by activityViewModels {
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

    private fun subscribeUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                workoutEditTemplateSharedViewModel.uiState.collect { currentState ->
                    Log.d(TAG, "comsumed uiState")
                    adapter.submitList(currentState.workoutEditTemplateItems)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        val navController = findNavController()
        lateinit var title: String
        lateinit var workoutNotes: String

        binding.apply {
            title = titleNewTemplateEditText.text.toString()
            workoutNotes = workoutNoteEditText.text.toString()
            newWorkoutTemplatesList.adapter = adapter
            newWorkoutTemplatesList.layoutManager = LinearLayoutManager(view?.context, RecyclerView.VERTICAL, false)
            addExerciseButton.setOnClickListener {
                val action = WorkoutEditTemplateFragmentDirections.actionNavWorkoutNewTemplateFragmentToExerciseAddFragment()
                findNavController().navigate(action)
            }
        }
        subscribeUi()

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
                        navController.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Frees the binding object when the FragmentType is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Called when Fragments own lifecycle is destroyed
     */
    override fun onDestroy() {
        super.onDestroy()
    }
}