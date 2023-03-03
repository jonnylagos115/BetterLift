package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.ExerciseListAdapter
import com.hfad.betterlift.databinding.FragmentExerciseBinding
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.ExerciseViewModel
import com.hfad.betterlift.viewmodels.ItemUiAction
import kotlinx.coroutines.launch

class ExerciseFragment : Fragment() {

    private val TAG = "ExerciseFragment"
    private var _binding: FragmentExerciseBinding? = null
    private var _adapter: ExerciseListAdapter? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
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
        Log.d(TAG, "onCreateView() called")
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        _adapter = ExerciseListAdapter(ItemUiAction.NAVIGATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated() called")
        // Attach an observer on the allItems list to update the UI automatically when the data
        // changes.

        binding.apply {
            exercisesList.adapter = adapter
            exercisesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
        }

        bindState()
        setupMenuItemDropdownList()
    }

    private fun bindState() {
        bindExercisesList()
    }

    private fun bindExercisesList() {
        // Consuming ExerciseViewModel state
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    exerciseViewModel.navigateToExerciseDetail.collect { exercise ->
                        val action = ExerciseFragmentDirections.actionNavExerciseToNavExerciseDetail(
                            exercise.exerciseId,
                            exercise.exerciseName)
                        findNavController().navigate(action)
                    }
                }

                launch {
                    exerciseViewModel.latestExerciseList.collect {
                        adapter.addHeaderAndSubmitList(it)
                    }
                }
            }
        }
    }

    // Add menu items without using the FragmentType Menu APIs
    // Note how we can tie the MenuProvider to the viewLifecycleOwner
    // and an optional Lifecycle.State (here, RESUMED) to indicate when
    // the menu should be visible
    private fun setupMenuItemDropdownList() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.exercise_detail_action_create_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.action_create -> {
                        val action = ExerciseFragmentDirections.actionNavExerciseToExerciseNewItemFragment()
                        findNavController().navigate(action)
                    }
                    R.id.action_delete_all -> {
                        exerciseViewModel.deleteExerciseItems()
                        exerciseViewModel.deleteAllNetworkRequestRecordDB()
                    }
                }
                return true
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
        _adapter = null
        Log.d(TAG, "onDestroy() called")
    }
}