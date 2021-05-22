package com.aliensquad.trafficlightmonitor.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.aliensquad.trafficlightmonitor.R

object PermissionUtils {

    fun isAccessFineLocationGranted(context: Context): Boolean {
        return ContextCompat
            .checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    fun isLocationEnabled(context: Context): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun showGPSNotEnableDialog(
        context: Context,
        onPositiveButtonClickListener: (() -> Unit),
        onNegativeButtonClickListener: (() -> Unit)
    ) {
        AlertDialog.Builder(context)
            .setTitle(context.getString(R.string.enable_gps))
            .setMessage(context.getString(R.string.enable_gps_required_message))
            .setCancelable(false)
            .setPositiveButton(context.getString(R.string.enable_now)) { _, _ ->
                onPositiveButtonClickListener()
            }
            .setNegativeButton(
                context.getString(R.string.exit)
            ) { _, _ ->
                onNegativeButtonClickListener()
            }
            .show()
    }
}