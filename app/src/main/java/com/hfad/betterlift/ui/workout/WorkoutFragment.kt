package com.hfad.betterlift.ui.workout

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
import com.hfad.betterlift.adapter.WorkoutViewAdapter
import com.hfad.betterlift.databinding.FragmentWorkoutBinding

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
    ): View? {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        binding.workoutTemplatesList.adapter = WorkoutViewAdapter()
        binding.workoutTemplatesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.workout_detail_app_bar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.workoutNewTemplateFragment -> {
                        Log.d(TAG, "Fab got clicked in onViewCreated")
                        /**
                         * Look into below, not sure if nagivation here is set up correctly
                         */
                        val navController = findNavController()
                        return menuItem.onNavDestinationSelected(navController)
                    }
                }
                return true
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