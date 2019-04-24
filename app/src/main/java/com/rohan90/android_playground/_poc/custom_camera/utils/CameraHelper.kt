package com.rohan90.narcissistic.utils

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by rohan on 17/4/19.
 */
class CameraHelper {
    companion object {
        /** Check if this device has a camera */
        @JvmStatic
        fun checkCameraHardware(context: Context): Boolean {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        }

    }

}