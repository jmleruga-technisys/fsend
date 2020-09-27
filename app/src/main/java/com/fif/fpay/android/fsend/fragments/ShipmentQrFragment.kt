package com.fif.fpay.android.fsend.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.fif.fpay.android.fsend.CustomAlertDialog
import com.fif.fpay.android.fsend.R
import com.fif.fpay.android.fsend.viewmodels.ShipmentViewModel
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Detector.Detections
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.shipment_qr_fragment.*
import java.io.IOException

class ShipmentQrFragment : BaseFragment(), QRReaderIteract {

    private var qrDetected: String = "";
    private var isProcessing: Boolean=false;
    private var lastDetectedQR: String=""
    private val MY_PERMISSIONS_REQUEST_CAMERA: Int = 13
    private val viewModel: ShipmentViewModel by navGraphViewModels(R.id.nav_graph_shipment)
    private lateinit var barcodeDetector : BarcodeDetector
    private lateinit var cameraSource: CameraSource

    companion object {
        //fun newInstance() = SelectAccountFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shipment_qr_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setToolbar(paymentsQrToolbar)
        paymentsQrToolbar?.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.validatedQr.observe(viewLifecycleOwner, Observer { result ->
            result.let {
                hideLoading()
                validQR()
            }
        })
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String?>, @NonNull grantResults: IntArray) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            for (index in permissions.indices.reversed()) {
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    showError("Faltan permisos de cámara", true)
                    return
                }
            }
        }
        try {
            cameraSource.start(scanQr.holder)
        } catch (e: IOException) {
            showError("Error en camara", true);
        }
    }

    private fun showError(error: String, exit: Boolean) {
        CustomAlertDialog(requireActivity())
            .setBasicProperties(
                error,
                R.string.accept_button,
                DialogInterface.OnClickListener { _, _ ->
                    if (exit)//Nothing
                        requireActivity().finish()
                },
                null,
                null,
                null,
                null
            ).show()
    }

    private fun startCamera() {
        // creo el detector qr
        barcodeDetector = BarcodeDetector.Builder(requireActivity())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        // creo la camara
        cameraSource = CameraSource.Builder(requireActivity(), barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedFps(10f)
            .build()

        scanQr?.holder?.addCallback(object : SurfaceHolder.Callback2 {
            override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
            }

            override fun surfaceChanged(holder: SurfaceHolder,format: Int,width: Int,height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA));
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    } else ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA), MY_PERMISSIONS_REQUEST_CAMERA)
                    return
                } else {
                    try {
                        cameraSource.start(scanQr.holder)
                    } catch (ie: IOException) {
                        showError("Error en cámara!", true)
                    } catch (ex: Exception) {
                        showError("Error en cámara!", true)
                    }
                }
            }
        })

        // preparo el detector de QR
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}
            override fun receiveDetections(detections: Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0 && !isProcessing) {

                    // obtenemos el qrDetected
                    qrDetected = barcodes.valueAt(0).displayValue.toString()

                    // verificamos que el qrDetected anterior no se igual al actual
                    // esto es util para evitar multiples llamadas empleando el mismo qrDetected
                    if (!qrDetected.equals(lastDetectedQR)) {

                        // guardamos el ultimo qrDetected proceado
                        lastDetectedQR = qrDetected
                        isProcessing = true
                        OnSuccessQR(lastDetectedQR)
                    } else {
                        Thread(object : Runnable {
                            override fun run() {
                                try {
                                    synchronized(this) {
                                        Thread.sleep(2000)
                                        // limpiamos el qrDetected
                                        lastDetectedQR = ""
                                    }
                                } catch (e: InterruptedException) {
                                    showError("Tiempo de cámara agotado",true)
                                }
                            }
                        }).start()
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        lastDetectedQR="";
        isProcessing=false;
        startCamera()
    }

    fun validQR(){
        cameraSource.stop()
        findNavController().navigate(R.id.action_shipmentQrFragment_to_shipmentSuccesFragment)
    }

    override fun OnSuccessQR(result: String) {
        requireActivity().runOnUiThread {
            showLoading()
        }
        viewModel.setFinalize(result){
            findNavController().navigate(R.id.action_shipmentQrFragment_to_shipmentErrorFragment)
        }
    }

}

interface QRReaderIteract {
    fun OnSuccessQR(result: String)
}