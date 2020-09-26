package com.fif.fpay.android.fsend.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.fif.fpay.android.fsend.R
import kotlinx.android.synthetic.main.shipment_succes_fragment.*

class ShipmentSuccesFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shipment_succes_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cmdFinalizarSucces.setOnClickListener {
            findNavController().navigate(R.id.action_shipmentSuccesFragment_to_shipmentListFragment)
        }
    }

}