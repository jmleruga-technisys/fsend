package com.fif.fpay.android.fsend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.data.Shipment

class ShipmentsAdapter(offersListIn: List<Shipment>,
                       ctx: Context?,
                       val selectedCardCallback: ((Shipment) -> Unit)?, val selectedButtonCallback: ((Shipment) -> Unit)?, val postponeButtonCallback: ((Shipment) -> Unit)?, val currentExists: Boolean) : RecyclerView.Adapter<ShipmentsAdapter.ViewHolder>() {



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
        holder.addressSubtitle.text = shipment.clientInfo.address.additionalInfo

        when(shipment.state){
            "IN_PROGRESS" -> {
                holder.state.text = "En entrega"
                holder.buttonSelect.visibility = View.GONE
                holder.buttonPostpone.visibility = View.GONE
                holder.state.setTextColor(context!!.getColor(R.color.logo_main_light_blue))
            }
            "DELIVERED" -> {
                holder.addressTitle.setTextColor(context!!.getColor(R.color.extra_white))
                holder.addressSubtitle.setTextColor(context.getColor(R.color.extra_white))
                holder.state.text = "Entregado"
                holder.state.setTextColor(context.getColor(R.color.extra_white))
                holder.card.setBackgroundColor(context!!.getColor(R.color.color_main_green))
                holder.card.alpha = 0.3F
                holder.buttonSelect.visibility = View.GONE
                holder.buttonPostpone.visibility = View.GONE
            }
            "REJECTED", "FAILED" -> {
                holder.state.text = "Entrega fallida"
                holder.buttonSelect.visibility = View.GONE
                holder.buttonPostpone.visibility = View.GONE
                holder.state.setTextColor(context!!.getColor(R.color.color_main_red))
            }
            "CREATED", "PENDING" -> {
                holder.state.visibility = View.GONE
                holder.buttonSelect.visibility = View.VISIBLE
                holder.buttonPostpone.visibility = View.VISIBLE
            }
            "RESCHEDULED" -> {
                holder.state.visibility = View.GONE
                holder.buttonSelect.visibility = View.VISIBLE
                holder.buttonSelect.text = context!!.getString(R.string.reschedule)
                holder.buttonPostpone.visibility = View.GONE
            }
            else -> {}
        }

        if(currentExists)
            holder.buttonSelect.isEnabled = false
            holder.buttonPostpone.isEnabled = false
            holder.buttonSelect.setTextColor(context!!.getColor(R.color.extra_white))
            holder.buttonPostpone.setTextColor(context!!.getColor(R.color.extra_white))
            holder.buttonSelect.setBackgroundColor(context.getColor(R.color.gray20))
            holder.buttonPostpone.setBackgroundColor(context.getColor(R.color.gray20))

    }

    override fun getItemCount(): Int {
        return shipmentList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var addressTitle: TextView = view.findViewById<View>(R.id.addressTitle) as TextView
        var addressSubtitle: TextView = view.findViewById<View>(R.id.addressSubtitle) as TextView
        var state: TextView = view.findViewById<View>(R.id.shipmentState) as TextView
        var buttonSelect: TextView = view.findViewById<View>(R.id.buttonSelectShipment) as TextView
        var buttonPostpone: TextView = view.findViewById<View>(R.id.buttonPostpone) as TextView
        var chevron: ImageView = view.findViewById<ImageView>(R.id.chevron_select) as ImageView
        var card: ConstraintLayout = view.findViewById<View>(R.id.cardView) as ConstraintLayout
        var shipment: Shipment? = null

        init {
            if(selectedCardCallback != null){
                val onClick: (View) -> Unit = {
                    lastSelectedPosition = adapterPosition
                    notifyDataSetChanged()
                    selectedCardCallback.invoke(shipmentList[lastSelectedPosition])
                }
                card.setOnClickListener(onClick)
            }

            if(selectedButtonCallback != null){
                val onClick: (View) -> Unit = {
                    lastSelectedPosition = adapterPosition
                    notifyDataSetChanged()
                    selectedButtonCallback.invoke(shipmentList[lastSelectedPosition])
                }
                buttonSelect.setOnClickListener(onClick)
            }

            if(postponeButtonCallback != null){
                val onClick: (View) -> Unit = {
                    lastSelectedPosition = adapterPosition
                    notifyDataSetChanged()
                    postponeButtonCallback.invoke(shipmentList[lastSelectedPosition])
                }
                buttonPostpone.setOnClickListener(onClick)
            }
        }
    }

}
