package com.fif.fpay.android.fsend.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel

class ShipmentListFragment : Fragment() {

    companion object {
        fun newInstance() =
            ShipmentListFragment()
    }

    private lateinit var viewModel: ShipmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shipment_list_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShipmentViewModel::class.java)
        // TODO: Use the ViewModel
    }

}