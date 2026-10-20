package com.example.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.BatteryManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast

object DeviceTools {
    private var isTorchOn = false

    fun toggleFlashlight(context: Context, turnOn: Boolean? = null): Boolean {
        return try {
            val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as? CameraManager
            val cameraId = cameraManager?.cameraIdList?.firstOrNull() ?: return false
            val newState = turnOn ?: !isTorchOn
            cameraManager.setTorchMode(cameraId, newState)
            isTorchOn = newState
            vibrate(context, 40)
            isTorchOn
        } catch (e: Exception) {
            false
        }
    }

    fun isFlashlightOn(): Boolean = isTorchOn

    fun getBatteryInfo(context: Context): Pair<Int, Boolean> {
        return try {
            val batteryManager = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
            val level = batteryManager?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: 85
            val isCharging = batteryManager?.isCharging ?: false
            Pair(level.coerceIn(0, 100), isCharging)
        } catch (e: Exception) {
            Pair(85, false)
        }
    }

    fun copyToClipboard(context: Context, text: String, label: String = "MYRA Assistant") {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard?.setPrimaryClip(clip)
            vibrate(context, 30)
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        } catch (_: Exception) {}
    }

    fun openWhatsApp(context: Context, message: String = "Hello via MYRA Assistant!") {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?text=${Uri.encode(message)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "WhatsApp not installed or accessible", Toast.LENGTH_SHORT).show()
        }
    }

    fun openSMS(context: Context, message: String = "Hello from MYRA Assistant") {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:")
                putExtra("sms_body", message)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "SMS client unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    fun openEmail(context: Context, subject: String = "Note from MYRA", body: String = "") {
        try {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "Email client unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    fun triggerEmergencySOS(context: Context, userName: String = "User") {
        vibrate(context, 200)
        openSMS(context, "EMERGENCY ALERT: $userName needs immediate assistance. Location telemetry requested.")
    }

    fun vibrate(context: Context, durationMs: Long = 50) {
        try {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }
}
