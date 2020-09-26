package com.fif.fpay.android.fsend

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.adapter.CustomInfoWindowsAdapter
import com.fif.fpay.android.fsend.data.DirectionResponses
import com.fif.fpay.android.fsend.data.InfoWindowData
import com.fif.fpay.android.fsend.fragments.ShipmentListFragment
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *
 *
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private lateinit var map: GoogleMap
    private lateinit var myPosition: LatLng
    private var mPolyline : Polyline? = null
    private var initDirection: String = ""
    private var goToDirection: String = ""
    private var zoom: Float = 16.0f
    private var mapFragment: SupportMapFragment? = null

    private val viewModel: ShipmentViewModel by navGraphViewModels(R.id.nav_graph_shipment)

    companion object {
        fun newInstance() =
            ShipmentListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        myPosition = LatLng(-34.8954889, -58.4001518) //Obtener mi posicion de gps
        mapFragment = childFragmentManager.findFragmentById(R.id.maps_view) as? SupportMapFragment?
        mapFragment!!.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.setOnMarkerClickListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15.6f))

        initDirection = "Nuestras Malvinas 277,Glew"
        goToDirection = "Aranguren 242,Glew"

       // addMakers()

        addInfoMaker()

    }


    private fun getPolyline(myLocation:String,customerLocation:String){

        val apiServices = RetrofitClient.apiServices(requireContext())
        apiServices.getDirection(myLocation, customerLocation, getString(R.string.api_key))
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

    private fun drawPolyline(response: Response<DirectionResponses>) {
        mPolyline.let {
            if (it != null)
            it!!.remove()
        }
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        val polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(8f)
            .color(Color.RED)

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(polyline.points[(polyline.points.size-1)/2], zoom-((polyline.points.size/2).toFloat())))
        mPolyline = map.addPolyline(polyline)

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


    override fun onMarkerClick(marker: Marker?): Boolean {

        val myPositionString = myPosition.latitude.toString()+","+myPosition.longitude.toString()
        val markerPositionString = marker!!.position.latitude.toString()+","+marker!!.position.longitude.toString()
        getPolyline(myPositionString,markerPositionString)
        marker.showInfoWindow()
        return true
    }


    fun addMakers(){
        var uno = LatLng(-34.897568, -58.402854)
        var dos = LatLng(-34.899152, -58.400891)
        var tres = LatLng(-34.890424, -58.376778)
        var cuatro = LatLng(-34.887191, -58.381391)
        var cinco:LatLng = LatLng(-34.874404, -58.379401)


        val mKuno = MarkerOptions()
            .position(uno)
            .title("uno")
        val mKdos = MarkerOptions()
            .position(dos)
            .title("uno")
        val mKtres = MarkerOptions()
            .position(tres)
            .title("uno")
        val mKcuatro = MarkerOptions()
            .position(cuatro)
            .title("uno")
        val mKcinco = MarkerOptions()
            .position(cinco)
            .title("uno")
            .snippet("")

        map.addMarker(mKuno)
        map.addMarker(mKdos)
        map.addMarker(mKtres)
        map.addMarker(mKcuatro)
        map.addMarker(mKcinco)


    }

    fun addInfoMaker(){

        var uno = LatLng(-34.897568, -58.402854)
        var dos = LatLng(-34.899152, -58.400891)
        var tres = LatLng(-34.890424, -58.376778)
        var cuatro = LatLng(-34.887191, -58.381391)
        var cinco:LatLng = LatLng(-34.874404, -58.379401)


        val markerOptions1 = MarkerOptions()
        markerOptions1.position(uno)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerOptions2 = MarkerOptions()
        markerOptions2.position(dos)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerOptions3 = MarkerOptions()
        markerOptions3.position(tres)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerOptions4 = MarkerOptions()
        markerOptions4.position(cuatro)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val markerOptions5 = MarkerOptions()
        markerOptions5.position(cinco)
            .title("Snowqualmie Falls")
            .snippet("Snoqualmie Falls is located 25 miles east of Seattle.")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        val info = InfoWindowData()
        info.image = "ic_map"
        info.hotel = "Hotel : excellent hotels available"
        info.food = "Food : all types of restaurants available"
        info.transport = "Reach the site by bus, car and train."

        val customInfoWindow = CustomInfoWindowsAdapter(requireContext())
        map.setInfoWindowAdapter(customInfoWindow)

        val m1: Marker = map.addMarker(markerOptions1)
        val m2: Marker = map.addMarker(markerOptions2)
        val m3: Marker = map.addMarker(markerOptions3)
        val m4: Marker = map.addMarker(markerOptions4)
        val m5: Marker = map.addMarker(markerOptions5)
        m1.tag = info
        m2.tag = info
        m3.tag = info
        m4.tag = info
        m5.tag = info

    }



}
