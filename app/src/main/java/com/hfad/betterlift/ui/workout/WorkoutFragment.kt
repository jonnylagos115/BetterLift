package com.hfad.betterlift.ui.workout

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.WorkoutFragmentListAdapter
import com.hfad.betterlift.databinding.FragmentWorkoutBinding
import com.hfad.betterlift.ui.MainActivity

class WorkoutFragment : Fragment() {

    private val TAG = "WorkoutFragment"
    private var _binding: FragmentWorkoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "CreatedWorkoutFragment")
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        binding.workoutTemplatesList.adapter = WorkoutFragmentListAdapter()
        binding.workoutTemplatesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        Log.d(TAG, "Inside view created")
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.workout_tab_app_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.action_workoutNewTemplateFragment -> {
                        val action = WorkoutFragmentDirections.actionNavWorkoutToNavWorkoutNewTemplateFragment()
                        findNavController().navigate(action)
                        return true
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


}