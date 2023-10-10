package br.pucminas.teamworktask.utils

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat


class PermissionUtils {
    companion object {
        fun checkBiometricSupport(context: Context): Boolean {
            val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE)
                    as KeyguardManager

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P){
                return false
            }

            if (!keyguardManager.isKeyguardSecure) {
                return false
            }
/*
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) !=
                PackageManager.PERMISSION_GRANTED) {
                return false
            }
*/
            return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                true
            } else true
        }

        fun checkCameraPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        }

        fun checkAgendaPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
        }
    }
}