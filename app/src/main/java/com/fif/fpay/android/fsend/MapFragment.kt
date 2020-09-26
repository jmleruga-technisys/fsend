package com.fif.fpay.android.fsend

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.data.DirectionResponses
import com.fif.fpay.android.fsend.fragments.ShipmentListFragment
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
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
class MapFragment : Fragment(), OnMapReadyCallback , GoogleMap.OnMarkerClickListener{

    private lateinit var map: GoogleMap
    private lateinit var myPosition: LatLng
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

        var uno = LatLng(-34.897568, -58.402854)
        var dos = LatLng(-34.899152, -58.400891)
        var tres = LatLng(-34.890424, -58.376778)
        var cuatro = LatLng(-34.887191, -58.381391)
        var cinco:LatLng = LatLng(-34.874404, -58.379401)


        val mKuno = MarkerOptions()
            .position(uno)
            .title("Nuestras Malvinas 277,Glew")
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

        map.addMarker(mKuno)
        map.addMarker(mKdos)
        map.addMarker(mKtres)
        map.addMarker(mKcuatro)
        map.addMarker(mKcinco)




    }


    private fun getPolyline(myLocation:LatLng,customerLocation:LatLng){

        val apiServices = RetrofitClient.apiServices(requireContext())
        apiServices.getDirection(myLocation.toString(), customerLocation.toString(), getString(R.string.api_key))
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

    override fun onMarkerClick(marker: Marker?): Boolean {
        getPolyline(myPosition,marker!!.position)
        return true
    }


}
