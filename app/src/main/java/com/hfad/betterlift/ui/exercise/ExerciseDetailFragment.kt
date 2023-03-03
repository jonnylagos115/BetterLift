package com.hfad.betterlift.ui.exercise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.FragmentExerciseDetailBinding
import com.hfad.betterlift.domain.Exercise
import com.hfad.betterlift.utils.Injector
import com.hfad.betterlift.viewmodels.ExerciseDetailViewModel
import com.hfad.betterlift.viewmodels.ExerciseViewModel
import kotlinx.coroutines.launch

class ExerciseDetailFragment : Fragment() {

    private val TAG = "ExerciseDetailFragment"
    private var _binding: FragmentExerciseDetailBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val navArgs: ExerciseDetailFragmentArgs by navArgs()
    private val exerciseViewModel: ExerciseViewModel by activityViewModels {
        Injector.provideExerciseViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView() called")
        _binding = FragmentExerciseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val exerciseId = navArgs.exerciseId
        collectUiState(exerciseId)
    }

    private fun collectUiState(exerciseId: Int){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }
    }

    private fun bind(exercise: Exercise) {
        binding.apply {
            bindImage(exerciseImage, exercise.imageResUrl)
            var incre = 0
            exerciseInstructionsText.text = exercise.description.split(". ").joinToString(".\n") {
                incre++
                "$incre. $it"
            }
        }
    }

    private fun bindImage(imgView: ImageView, imgUrl: String?){
        if (imgUrl != null){
            imgUrl.let {
                val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
                imgView.load(imgUri) {
                    placeholder(R.drawable.loading_animation)
                }
            }
        } else{
            imgView.setImageResource(R.drawable.chestpressing)
        }
    }

    private fun parseInstructionList() {

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