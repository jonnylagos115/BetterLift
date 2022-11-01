package com.hfad.betterlift.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hfad.betterlift.R
import com.hfad.betterlift.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration
            .Builder(
                R.id.profileFragment,
                R.id.workoutFragment,
                R.id.exercisesFragment
            )
            .build()
        setupActionBarWithNavController(navController, appBarConfiguration)

        handleBottomNavigation(navController)
    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()  || super.onSupportNavigateUp()
    }

    private fun handleBottomNavigation(navController: NavController){
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
        bottomNav?.setupWithNavController(navController)
    }
}