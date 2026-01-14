package com.knight.salah.platform

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.knight.salah.R

class AzanAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check if we have notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                // Permission not granted, can't show notification
                return
            }
        }

        val title = intent.getStringExtra("title") ?: "Prayer"
        val description = intent.getStringExtra("description") ?: ""
        val id = intent.getStringExtra("notificationId") ?: "prayer"
        val soundOrdinal = intent.getIntExtra("soundTypeOrdinal", 0)
        val soundType = NotificationSoundType.entries.getOrNull(soundOrdinal)
            ?: NotificationSoundType.DEFAULT

        // 1) Make sure channels exist, even if app was never opened
        val nm = NotificationManager(context)
        nm.ensureAllChannels()

        // 2) Pick correct channel for this sound type
        val channelId = when (soundType) {
            NotificationSoundType.DEFAULT -> NotificationManager.CHANNEL_ID_DEFAULT
            NotificationSoundType.ADHAN -> NotificationManager.CHANNEL_ID_ADHAN
            NotificationSoundType.IQAMA -> NotificationManager.CHANNEL_ID_IQAMA
        }

        val notificationId = id.hashCode()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        try {
            NotificationManagerCompat.from(context)
                .notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Permission was revoked between scheduling and firing
            e.printStackTrace()
        }
    }
}
