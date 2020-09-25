package com.fif.fpay.android.fsend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    private val navController by lazy { findNavController(R.id.nav_graph_shipment) } //1
    private lateinit var viewModel: ShipmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}