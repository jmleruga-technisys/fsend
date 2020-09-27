package com.fif.fpay.android.fsend.activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.fragments.BaseActivity
import com.fif.fpay.android.fsend.ui.CustomProgressBar
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BaseActivity {
    private lateinit var viewModel: ShipmentViewModel
    private lateinit var progressBar: CustomProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

       // val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.mapFragment,
            R.id.shipmentListFragment,
            R.id.configurationsFragment
        ))
       // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        progressBar = findViewById<CustomProgressBar>(R.id.customProgressBar)

    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        //nav_host_fragment.alpha = 1F
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        //nav_host_fragment.alpha = 0.51F
    }
}