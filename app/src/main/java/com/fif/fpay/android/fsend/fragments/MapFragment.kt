package com.fif.fpay.android.fsend.fragments

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.CustomAlertDialog
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.data.DirectionResponses
import com.fif.fpay.android.fsend.data.Shipment
import com.fif.fpay.android.fsend.utils.SortPlaces
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import kotlinx.android.synthetic.main.custominfowindow.*
import kotlinx.android.synthetic.main.fragment_map.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


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
class MapFragment : BaseFragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener{

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latitudeLabel: String? = null
    private var longitudeLabel: String? = null
    private val TAG = "LocationProvider"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private lateinit var map: GoogleMap
    private lateinit var myPosition: LatLng
    private var mPolyline : Polyline? = null
    private var initDirection: String = ""
    private var goToDirection: String = ""
    private var zoom: Float = 16.0f
    private var mapFragment: SupportMapFragment? = null
    var polyline : PolylineOptions? = null
    var time : String? = null
    var hashMap: HashMap<LatLng, Int>? = HashMap<LatLng,Int>()
    var shipmentOnlyValids: ArrayList<Shipment> = ArrayList()
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }

        myPosition = LatLng(-34.551934, -58.449241) //Obtener mi posicion de gps
        mapFragment = childFragmentManager.findFragmentById(R.id.maps_view) as? SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        map.setOnMarkerClickListener(this)
        val markerMyLocation = MarkerOptions()
        markerMyLocation.position(myPosition)
            .title("Mi posición")
            .icon(vectorToBitmap(
                R.drawable.ic_delivery, Color.parseColor("#000000")))
        map.addMarker(markerMyLocation)

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15.6f))


            map.setOnMyLocationChangeListener { arg0 ->
                map.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            arg0.latitude,
                            arg0.longitude
                        )
                    ).title("It's Me!")
                )
            }


        initDirection = "Nuestras Malvinas 277,Glew"
        goToDirection = "Aranguren 242,Glew"

        addMakers()


        imgBestRoute.setOnClickListener {
            setBestRoute()
        }



        //addInfoMaker()

    }

    private fun setBestRoute() {
        Collections.sort(shipmentOnlyValids,  SortPlaces(myPosition))
        val ltLong = LatLng(shipmentOnlyValids!![0].clientInfo.address.location.position.latitude.toDouble(),
            shipmentOnlyValids!![0].clientInfo.address.location.position.longitude.toDouble())
        val markerOptions1 = MarkerOptions()
        markerOptions1.position(ltLong)
            .title(shipmentOnlyValids!![0].clientInfo.address.fullAddress)
            .icon((BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)))

           val markerMyLocation = MarkerOptions()
                markerMyLocation.position(myPosition)
            .title("Mi posición")
            .icon(vectorToBitmap(
                R.drawable.ic_delivery, Color.parseColor("#000000")))

        map.addMarker(markerMyLocation)
        var marker:Marker = map.addMarker(markerOptions1)


        val myPositionString = myPosition.latitude.toString()+","+myPosition.longitude.toString()
        val markerPositionString = ltLong!!.latitude.toString()+","+ltLong!!.longitude.toString()

        getPolyline(myPositionString,markerPositionString,marker)


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 15.6f))

    }


    private fun getPolyline(myLocation:String,customerLocation:String,marker: Marker?){

        val apiServices =
            RetrofitClient.apiServices(
                requireContext()
            )
        apiServices.getDirection(myLocation, customerLocation, getString(R.string.api_key))
            .enqueue(object : Callback<DirectionResponses> {
                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
                    drawPolyline(response,marker)
                    Log.d("bisa dong oke", response.message())
                }

                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
                    Log.e("anjir error", t.localizedMessage)
                }
            })
    }

    private fun drawPolyline(response: Response<DirectionResponses>,marker: Marker?) {
        mPolyline.let {
            if (it != null)
            it!!.remove()
        }
        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
        time = response.body()?.routes?.get(0)?.legs!!.get(0)!!.duration!!.text
        polyline = PolylineOptions()
            .addAll(PolyUtil.decode(shape))
            .width(12f)
            .color(Color.RED)

        //map.moveCamera(CameraUpdateFactory.newLatLngZoom(polyline.points[(polyline.points.size-1)/2], zoom-((polyline.points.size/2).toFloat())))
        mPolyline = map.addPolyline(polyline)
        marker!!.snippet = time
        marker.showInfoWindow()

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

            return retrofit.create<ApiServices>(
                ApiServices::class.java)
        }
    }


    override fun onMarkerClick(marker: Marker?): Boolean {
        view?.let {
            Snackbar.make(it, "", Snackbar.LENGTH_INDEFINITE)
                .setAction("Iniciar entrega") {

                    var currentExists = viewModel.shipments!!.filter { it.state == "IN_PROGRESS"}.isNotEmpty()
                    if (!currentExists){
                        val bundle = bundleOf("selected" to Gson().toJson(hashMap!![LatLng(marker!!.position.latitude,marker.position.longitude)]?.let { it1 ->
                            viewModel.shipments!![it1]
                        }))
                        findNavController().navigate(R.id.action_mapFragment_to_shipmentDetailFragment,bundle)
                    }else{
                        CustomAlertDialog(requireActivity())
                            .setBasicProperties(
                                "Ya tenes un envio en curso, terminalo para comenzar otro",
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

                }
                .setActionTextColor(getResources().getColor(R.color.sap_green))
                .show()
        }

        val myPositionString = myPosition.latitude.toString()+","+myPosition.longitude.toString()
        val markerPositionString = marker!!.position.latitude.toString()+","+marker!!.position.longitude.toString()
        getPolyline(myPositionString,markerPositionString, marker)

        return true
    }


    fun addMakers(){
        for (i in viewModel.shipments!!.indices){

            val state =viewModel!!.shipments!!.get(i).state
            val ltLong = LatLng(viewModel!!.shipments!![i].clientInfo.address.location.position.latitude.toDouble(),
                viewModel.shipments!![i].clientInfo.address.location.position.longitude.toDouble())
            val m = MarkerOptions()
                .position(ltLong)
                .title(viewModel!!.shipments!![i].clientInfo.address.fullAddress)
                .snippet(i.toString())
                .icon(
            when(state) {
                "IN_PROGRESS" -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                }
                "DELIVERED" -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                }
                "REJECTED", "FAILED" -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                }
                "CREATED","PENDING" -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                }
                "RESCHEDULED" -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                }
                else -> {
                    (BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                }
            }
                )
            if (state == "IN_PROGRESS"||state == "CREATED"||state == "PENDING"){
                map.addMarker(m)
                shipmentOnlyValids.add(viewModel!!.shipments!![i])
            }

            hashMap!![ltLong] = i

        }
    }

    private fun vectorToBitmap(@DrawableRes id : Int, @ColorInt color : Int): BitmapDescriptor {
        val vectorDrawable: Drawable = ResourcesCompat.getDrawable(resources, id, null)
            ?: //Log.e(TAG, "Resource not found")
            return BitmapDescriptorFactory.defaultMarker()
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        DrawableCompat.setTint(vectorDrawable, color)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }



    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        if(fusedLocationClient?.lastLocation!!.isComplete){
            myPosition = (LatLng(fusedLocationClient?.lastLocation!!.result.latitude,fusedLocationClient?.lastLocation!!.result.longitude))
        }
        }

    private fun showMessage(string: String) {

    }
    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(requireContext(), mainTextStringId, Toast.LENGTH_LONG).show()
    }


    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.")
            showSnackbar("Location permission is needed for core functionality", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "Requesting permission")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.i(TAG, "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                    Log.i(TAG, "User interaction was cancelled.")
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }



}