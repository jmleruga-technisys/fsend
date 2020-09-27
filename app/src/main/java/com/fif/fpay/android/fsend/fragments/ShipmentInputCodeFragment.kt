package com.fif.fpay.android.fsend.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import kotlinx.android.synthetic.main.shipment_input_code_fragment.*
import kotlinx.android.synthetic.main.shipment_qr_fragment.*

class ShipmentInputCodeFragment : BaseFragment() {

    private val viewModel: ShipmentViewModel by navGraphViewModels(R.id.nav_graph_shipment)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.shipment_input_code_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbar(toolbarInputCode)
        toolbarInputCode?.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.validatedQr.observe(viewLifecycleOwner, Observer { result ->
            result.let {
                hideLoading()
                validQR()
            }
        })

        cmdInputAceptar.setOnClickListener {
            viewModel.setFinalize(inputTextCodigo.editText?.text.toString()){
                findNavController().navigate(R.id.action_shipmentInputCodeFragment_to_shipmentErrorFragment)
            }
        }
    }

    fun validQR(){
        findNavController().navigate(R.id.action_shipmentInputCodeFragment_to_shipmentSuccesFragment)
    }
    
    

}