package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.R
import com.hfad.betterlift.adapter.ExerciseFragmentListAdapter
import com.hfad.betterlift.const.FragmentType
import com.hfad.betterlift.databinding.FragmentExerciseBinding

class ExerciseFragment : Fragment(), ExerciseNewDialogFragment.OnClickListener {

    private val TAG = "ExerciseFragment"
    private var _binding: FragmentExerciseBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()

        binding.exercisesList.adapter = ExerciseFragmentListAdapter(FragmentType.EXERCISES)
        binding.exercisesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        // Add menu items without using the FragmentType Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.exercise_detail_action_create_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when(menuItem.itemId){
                    R.id.action_create -> {
                        Log.d(TAG, "Fab got clicked in onViewCreated")
                        showDialog()
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

    private fun showDialog() {
        val dialog = ExerciseNewDialogFragment.create(this)
        dialog.show(parentFragmentManager, "ExerciseDialogFragment")
    }

    override fun onDialogPositiveClick() {
        Log.d(TAG, " onDialogPositiveClick save clicked")
        //Nothing to do, is handled by caller
    }

    override fun onDialogNegativeClick() {
        Log.d(TAG, " onDialogNegativeClick cancel clicked")
    }
}