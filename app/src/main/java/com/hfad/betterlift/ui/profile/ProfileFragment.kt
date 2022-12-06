package com.hfad.betterlift.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.navigation.NavigationBarView
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private val TAG = "ProfileFragment"
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "CreatedProfileFragment")
        _binding = FragmentProfileBinding.inflate(inflater,container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        NavigationBarView.OnItemSelectedListener {
            it.onNavDestinationSelected(view.findNavController())
        }
    }

    /**
     * Frees the binding object when the FragmentType is destroyed.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}