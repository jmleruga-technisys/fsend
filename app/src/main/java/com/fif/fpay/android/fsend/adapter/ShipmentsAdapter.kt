package com.fif.fpay.android.fsend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.data.Shipment

class ShipmentsAdapter(offersListIn: List<Shipment>,
                       ctx: Context?,
                       val selectedCallback: ((Shipment) -> Unit)?) : RecyclerView.Adapter<ShipmentsAdapter.ViewHolder>() {



    private val shipmentList: List<Shipment> = offersListIn
    private val context: Context? = ctx
    var lastSelectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.select_shipment_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder,
                                  position: Int) {
        val shipment: Shipment = shipmentList[position]

        holder.addressTitle.text = shipment.clientInfo.address.fullAddress
        holder.addressSubtitle.text = shipment.clientInfo.address.department

    }

    override fun getItemCount(): Int {
        return shipmentList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var addressTitle: TextView = view.findViewById<View>(R.id.addressTitle) as TextView
        var addressSubtitle: TextView = view.findViewById<View>(R.id.addressSubtitle) as TextView
        var card: ConstraintLayout = view.findViewById<View>(R.id.cardView) as ConstraintLayout
        var shipment: Shipment? = null

        init {
            if(selectedCallback != null){
                val onClick: (View) -> Unit = {
                    lastSelectedPosition = adapterPosition
                    notifyDataSetChanged()
                    //selectedCallback.invoke(it)
                }
                card.setOnClickListener(onClick)
            }
        }
    }

}
