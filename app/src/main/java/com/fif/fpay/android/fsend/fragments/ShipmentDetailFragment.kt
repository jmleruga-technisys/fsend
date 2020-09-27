package com.fif.fpay.android.fsend.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.CustomAlertDialog
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.data.DirectionResponses
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.fif.tech.android.commons.android_connection.utils.converter.Json
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.fragment_shipment_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ShipmentDetailFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var myPosition: LatLng
    private var initDirection: String = ""
    private var goToDirection: String = ""
    private var zoom: Float = 16.0f
    private var mapFragment:SupportMapFragment? = null

    val REQUEST_PHONE_CALL = 1
    lateinit var infoShipment: Shipment

    private val viewModel: ShipmentViewModel by navGraphViewModels(R.id.nav_graph_shipment)

    companion object {
        fun newInstance() =
            ShipmentListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shipment_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbar(shipmentDetailToolbar)
        shipmentDetailToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        requireArguments().get("selected").let {
            infoShipment =  Gson().fromJson(it.toString(), Shipment::class.java)
            //Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        textDirection?.text = infoShipment.clientInfo.address.fullAddress
        textAdditionalInfo?.text = infoShipment.clientInfo.address.additionalInfo
        textUserName?.text = infoShipment.clientInfo.name

        buttonPaymentDetail?.setOnClickListener{
            findNavController().navigate(R.id.action_shipmentDetailFragment_to_shipmentQrFragment)
        }


        myPosition = LatLng(-34.8954889, -58.4001518) //Obtener mi posicion de gps
        mapFragment = childFragmentManager.findFragmentById(R.id.maps_view) as? SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15.6f))

        initDirection = "Nuestras Malvinas 277,Glew"
        goToDirection = "Aranguren 242,Glew"

        imgCall.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
            }else{
                startCall()
            }
        }

        val apiServices = RetrofitClient.apiServices(requireContext())
        apiServices.getDirection(initDirection, goToDirection, getString(R.string.api_key))
            .enqueue(object : Callback<DirectionResponses> {
                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                    drawPolyline(response)
                    Log.d("bisa dong oke", response.message())
                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e("anjir error", t.localizedMessage)
                }
            })
    }

    @SuppressLint("MissingPermission")
    private fun startCall(){
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + infoShipment.clientInfo.phone))
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        if(requestCode == REQUEST_PHONE_CALL)startCall()
    }

    private fun drawPolyline(response: Response<DirectionResponses>) {
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)

        val markerInit = MarkerOptions()
            .position(polyline.points[0])
            .title(initDirection)

        val markerGoTo = MarkerOptions()
            .position(polyline.points[polyline.points.size -1])
            .title(goToDirection)


        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(polyline.points[(polyline.points.size-1)/2], zoom-((polyline.points.size/2).toFloat())))
        map.addPolyline(polyline)
        map.addMarker(markerInit)
        map.addMarker(markerGoTo)
    }

    private interface ApiServices {
        @GET("maps/api/directions/json")
        fun getDirection(@Query("origin") origin: String,
                         @Query("destination") destination: String,
                         @Query("key") apiKey: String): Call<DirectionResponses>
    }

    private object RetrofitClient {
        fun apiServices(context: Context): ApiServices {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(context.resources.getString(R.string.base_url))
                .build()

            return retrofit.create<ApiServices>(ApiServices::class.java)
        }
    }
}
