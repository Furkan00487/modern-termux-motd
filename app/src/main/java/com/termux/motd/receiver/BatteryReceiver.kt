package com.termux.motd.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import java.io.File

/**
 * Modern Termux:MOTD - On-Demand Battery Receiver
 *
 * Sıfır arka plan yükü: Yalnızca terminalde `termux-motd-refresh` çalıştırıldığında
 * veya yeni sekme açıldığında 0.01 sn tek seferlik çağrılır.
 */
class BatteryReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        try {
            val batteryStatus = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            val level = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, 100) ?: 100
            val status = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1

            val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL

            val percentage = if (scale > 0 && level >= 0) (level * 100) / scale else level
            val statusSuffix = when {
                status == BatteryManager.BATTERY_STATUS_FULL -> " (Dolu 🔋)"
                isCharging -> " (Şarj Oluyor ⚡)"
                else -> ""
            }

            val batteryInfo = if (percentage >= 0) "$percentage%$statusSuffix" else "N/A"

            // Termux'un geçici dizinine yaz (sharedUserId="com.termux" sayesinde köksüz yazılabilir)
            val outputFile = File("/data/data/com.termux/files/usr/tmp/motd_battery.txt")
            outputFile.parentFile?.mkdirs()
            outputFile.writeText(batteryInfo, Charsets.UTF_8)
        } catch (_: Exception) {
            // 0 çökme ve sıfır arka plan engeli
        }
    }
}
