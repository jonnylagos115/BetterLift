package com.hfad.betterlift.ui.workout

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.WorkoutNewTemplateItemListAdapter
import com.hfad.betterlift.repository.WorkoutRepo
import com.hfad.betterlift.databinding.FragmentWorkoutNewTemplateBinding
import com.hfad.betterlift.domain.Workout

class WorkoutNewTemplateFragment : Fragment() {
    private val TAG = "WorkoutNewTemplate"
    private var _binding: FragmentWorkoutNewTemplateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var title: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkoutNewTemplateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        val navController = findNavController()
        lateinit var workout: Workout

        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Workout>("selected_exercises")?.observe(
            viewLifecycleOwner) { result ->
            workout = result
            binding.newWorkoutTemplatesList.apply {
                adapter =  WorkoutNewTemplateItemListAdapter(workout)
                layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            }
        }
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
                        workout.templateTitle = getTemplateTitle()
                        WorkoutRepo.workoutList.add(workout)
                        navController.popBackStack()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.titleNewTemplateEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                title = binding.titleNewTemplateEditText.text.toString()
            }

        })
        binding.addExerciseButton.setOnClickListener {
            val action = WorkoutNewTemplateFragmentDirections.actionNavWorkoutNewTemplateFragmentToNavExerciseAddFragment()
            findNavController().navigate(action)
        }
    }

    private fun getTemplateTitle() = title

    /**
     * Frees the binding object when the FragmentType is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}