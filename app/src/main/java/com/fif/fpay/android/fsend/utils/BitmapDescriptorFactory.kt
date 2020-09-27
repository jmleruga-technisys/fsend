package com.fif.fpay.android.fsend.utils

import android.graphics.Bitmap
import android.os.RemoteException
import com.google.android.gms.common.internal.Preconditions
import com.google.android.gms.internal.maps.zze
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.RuntimeRemoteException

object BitmapDescriptorFactory {
    const val HUE_RED = 0.0f
    const val HUE_ORANGE = 30.0f
    const val HUE_YELLOW = 60.0f
    const val HUE_GREEN = 120.0f
    const val HUE_CYAN = 180.0f
    const val HUE_AZURE = 210.0f
    const val HUE_BLUE = 240.0f
    const val HUE_VIOLET = 270.0f
    const val HUE_MAGENTA = 300.0f
    const val HUE_ROSE = 330.0f

    private var zzcm: zze? = null
    private fun zzg(): zze? {
        return Preconditions.checkNotNull(
            zzcm,
            "IBitmapDescriptorFactory is not initialized"
        )
    }

    fun zza(var0: zze?) {
        if (zzcm == null) {
            zzcm =
                Preconditions.checkNotNull(
                    var0
                )
        }
    }

    fun fromResource(var0: Int): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zza(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }

    fun fromAsset(var0: String?): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zza(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }

    fun fromFile(var0: String?): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zzb(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }

    fun fromPath(var0: String?): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zzc(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }

    fun defaultMarker(): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zzi())
        } catch (var1: RemoteException) {
            throw RuntimeRemoteException(var1)
        }
    }

    fun defaultMarker(var0: Float): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zza(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }

    fun fromBitmap(var0: Bitmap?): BitmapDescriptor {
        return try {
            BitmapDescriptor(zzg()!!.zza(var0))
        } catch (var2: RemoteException) {
            throw RuntimeRemoteException(var2)
        }
    }
}
