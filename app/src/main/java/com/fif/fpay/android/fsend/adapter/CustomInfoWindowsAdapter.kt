package com.fif.fpay.android.fsend.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.data.InfoWindowData
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.Marker


class CustomInfoWindowsAdapter(ctx: Context) : InfoWindowAdapter {
    private val context: Context = ctx
    override fun getInfoWindow(marker: Marker): View? {
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val view: View = (context as Activity).layoutInflater
            .inflate(R.layout.custominfowindow, null)
        val name_tv: TextView = view.findViewById(R.id.name)
        val details_tv: TextView = view.findViewById(R.id.details)
        val img: ImageView = view.findViewById(R.id.pic)
        val hotel_tv: TextView = view.findViewById(R.id.hotels)
        val food_tv: TextView = view.findViewById(R.id.food)
        val transport_tv: TextView = view.findViewById(R.id.transport)
        val btnAltiro: Button = view.findViewById(R.id.btnIr)
        name_tv.text = marker.title
        details_tv.text = marker.snippet
        val infoWindowData: InfoWindowData? = marker.tag as InfoWindowData?
        val imageId: Int = context.getResources().getIdentifier(
            infoWindowData!!.image!!.toLowerCase(),
            "drawable", context.getPackageName()
        )
        img.setImageResource(imageId)
        hotel_tv.text= (infoWindowData.hotel)
        food_tv.text =(infoWindowData.food)
        transport_tv.text = (infoWindowData.transport)
        btnAltiro.setOnClickListener {
            Toast.makeText(context, "Al tiro", Toast.LENGTH_SHORT).show() }

        return view
    }

}
