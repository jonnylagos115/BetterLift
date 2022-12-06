package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hfad.betterlift.adapter.ExerciseFragmentListAdapter
import com.hfad.betterlift.const.FragmentType
import com.hfad.betterlift.databinding.FragmentExerciseAddBinding
import com.hfad.betterlift.models.Workout

class ExerciseAddFragment : Fragment() {

    private val TAG = "ExerciseAddFragment"
    private var _binding: FragmentExerciseAddBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var exerciseFragmentListAdapter: ExerciseFragmentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Retrieve and inflate the layout for this fragment
        _binding = FragmentExerciseAddBinding.inflate(inflater, container, false)
        binding.FABSaveButton.setOnClickListener{
            val workout = Workout(exerciseFragmentListAdapter.getSelectedItems())
            findNavController().previousBackStackEntry?.savedStateHandle?.set("selected_exercises", workout)
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        exerciseFragmentListAdapter = ExerciseFragmentListAdapter(FragmentType.ADD_EXERCISES)
        binding.exercisesList.adapter = exerciseFragmentListAdapter
        binding.exercisesList.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)

        // Add menu items without using the FragmentType Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
    }

    /**
     * Frees the binding object when the FragmentType is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}