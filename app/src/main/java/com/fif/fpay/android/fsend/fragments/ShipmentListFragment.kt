package com.fif.fpay.android.fsend.fragments


import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import androidx.viewpager2.widget.ViewPager2
import com.fif.fpay.android.fsend.CustomAlertDialog
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.adapter.ShipmentsAdapter
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.android.synthetic.main.select_shipment_item.*
import kotlinx.android.synthetic.main.shipment_list_fragment.*
import java.lang.Exception


class ShipmentListFragment : BaseFragment() {
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
        viewModel.getShipments {
            hideLoading()
            CustomAlertDialog(requireActivity())
                .setBasicProperties(
                    it.friendlyMessage,
                    R.string.accept_button,
                    DialogInterface.OnClickListener { _, _ ->
                        //Nothing
                    },
                    null,
                    null,
                    null,
                    null
                ).show()
            setShipmentList(viewModel.shipments)
            checkCurrent()
        }

        showLoading()

        return inflater.inflate(R.layout.shipment_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.gotShipments.observe(viewLifecycleOwner, Observer { result ->
            result.getContentIfNotHandled()?.let { shipments ->
                setShipmentList(shipments)
                checkCurrent()
            }
            hideLoading()
        })

        shipmentIndicator.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                //Do nothing
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                //Do Nothing
            }
        })

        checkCurrent()
    }


    fun checkSentShipments() {
        var counter = 0
        viewModel.shipments?.let {
            shipmentIndicator.removeAllTabs()
            it.forEach { shipment ->
                shipmentIndicator.addTab(shipmentIndicator.newTab())
                if (shipment.state == "DELIVERED") {
                    counter++
                    shipmentIndicator.getTabAt(shipmentIndicator.size - 1)!!.select()
                }
            }
        }
        sentShipments.text =
            context?.getString(R.string.sent_shipments, counter, viewModel.shipments!!.size)
    }

    fun checkCurrent() {
        viewModel.shipments?.let {
            try {
                val current = it.first { it.state == "IN_PROGRESS" }
                viewModel.currentShipment = current
            }catch (e: Exception){
                viewModel.currentShipment = null
            }
        }
    }

    fun setCurrent(shipment: Shipment){
        showLoading()
        viewModel.currentShipment = shipment
        shipment.state = "IN_PROGRESS"
        viewModel.updateState(shipment, "", success = {
            viewModel.getShipments {
                hideLoading()
                CustomAlertDialog(requireActivity())
                    .setBasicProperties(
                        "No se pudo actualizar el estado. Intente nuevamente.",
                        R.string.accept_button,
                        DialogInterface.OnClickListener { _, _ ->
                            //Nothing
                        },
                        null,
                        null,
                        null,
                        null
                    ).show()
            }
        }, failure = {
            viewModel.getShipments {
                hideLoading()
                CustomAlertDialog(requireActivity())
                    .setBasicProperties(
                        "No se pudo actualizar el listado. Intente nuevamente.",
                        R.string.accept_button,
                        DialogInterface.OnClickListener { _, _ ->
                            //Nothing
                        },
                        null,
                        null,
                        null,
                        null
                    ).show()
            }
        })
    }

    fun setShipmentList(list: ArrayList<Shipment>?){
        if (list != null && list.isNotEmpty()) {
            var currentExists = list.filter { it.state == "IN_PROGRESS"}.isNotEmpty()
            shipmentsGroup.visibility = View.VISIBLE
            shipmentsIndicatorsGroup.visibility = View.VISIBLE
            noShipmentGroup.visibility = View.GONE
            checkSentShipments()
            shipmentRecyclerView.adapter =
                ShipmentsAdapter(list, context, { selected ->
                    selectedShipment = selected
                    val bundle = bundleOf("selected" to Gson().toJson(selectedShipment))
                    findNavController().navigate(R.id.action_shipmentListFragment_to_shipmentDetailFragment, bundle)
                }, {
                    setCurrent(it)
                }, {
                    postponeShipment(it)
                },
                    currentExists)
            val itemDecor = DividerItemDecoration(context, VERTICAL)
            shipmentRecyclerView.addItemDecoration(itemDecor)
            shipmentRecyclerView.layoutManager = LinearLayoutManager(activity)
            try {
                val current = list.first { it.state == "IN_PROGRESS" }
                viewModel.currentShipment = current
            }catch (e: Exception){
                viewModel.currentShipment = null
            }
        } else {
            noShipmentGroup.visibility = View.VISIBLE
            shipmentsIndicatorsGroup.visibility = View.GONE
            shipmentsGroup.visibility = View.GONE
        }

    }

    fun postponeShipment(shipment: Shipment){
        shipment.state = "RESCHEDULED"
        viewModel.updateState(shipment, "", success = {
            viewModel.getShipments {
                hideLoading()
                CustomAlertDialog(requireActivity())
                    .setBasicProperties(
                        "No se pudo actualizar el estado. Intente nuevamente.",
                        R.string.accept_button,
                        DialogInterface.OnClickListener { _, _ ->
                            //Nothing
                        },
                        null,
                        null,
                        null,
                        null
                    ).show()
            }
        }, failure = {
            viewModel.getShipments {
                hideLoading()
                CustomAlertDialog(requireActivity())
                    .setBasicProperties(
                        "No se pudo actualizar el listado. Intente nuevamente.",
                        R.string.accept_button,
                        DialogInterface.OnClickListener { _, _ ->
                            //Nothing
                        },
                        null,
                        null,
                        null,
                        null
                    ).show()
            }
        })
    }
}