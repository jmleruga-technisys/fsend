package com.fif.fpay.android.fsend.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.adapter.ShipmentsAdapter
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import kotlinx.android.synthetic.main.shipment_list_fragment.*

class ShipmentListFragment : Fragment() {
    private val viewModel: ShipmentViewModel by navGraphViewModels(R.id.nav_graph_shipment)

    var selectedShipment: Shipment? = null

    companion object {
        fun newInstance() =
            ShipmentListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shipment_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(viewModel.shipments != null && viewModel.shipments!!.isNotEmpty()){
            shipmentsGroup.visibility = View.VISIBLE
            shipmentsIndicatorsGroup.visibility = View.VISIBLE
            noShipmentGroup.visibility = View.GONE
            checkSentShipments()
            shipmentRecyclerView.adapter = ShipmentsAdapter(viewModel.shipments!!, context) { selected ->
                selectedShipment = selected
                //Voy al detalle findNavController().navigate(R.id.action)
            }
        }else{
            noShipmentGroup.visibility = View.VISIBLE
            shipmentsIndicatorsGroup.visibility = View.GONE
            shipmentsGroup.visibility = View.GONE
        }
    }

    fun checkSentShipments(){
        var counter = 0
        viewModel.shipments?.let {
            it.forEach { shipment ->
                if(shipment.state == "DELIVERED")
                    counter++
            }
        }
        sentShipments.text = context?.getString(R.string.sent_shipments, counter, viewModel.shipments!!.size)
    }

}