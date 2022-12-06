package com.hfad.betterlift.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNav: BottomNavigationView

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNav = binding.bottomNavigationBar
        bottomNav.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_profile, R.id.nav_workout, R.id.nav_exercise))
        navController.addOnDestinationChangedListener { _: NavController, destination: NavDestination, _: Bundle? ->
            bottomNav.isVisible = appBarConfiguration.topLevelDestinations.contains(destination.id)
            Log.d(TAG, "inside destchanged")
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */

    override fun onSupportNavigateUp(): Boolean {
        Log.d(TAG, "navigateUp pressed")
        return navController.navigateUp(appBarConfiguration)
    }

    /*
    Call these methods from the Fragment you want to show/hide BottomNavigationView

        override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
        }

        override fun onDetach() {
        (activity as MainActivity).showBottomNavigation()
        super.onDetach()
        }
     */

    fun showBottomNavigation()
    {
        bottomNav.visibility = View.VISIBLE
    }

    fun hideBottomNavigation()
    {
        bottomNav.visibility = View.GONE
    }

}