package com.knight.salah.platform

import android.Manifest
import android.app.NotificationChannel
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.knight.salah.R
import android.app.NotificationManager as AndroidNotificationManager


class AzanAlarmReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Prayer"
        val description = intent.getStringExtra("description") ?: ""
        val id = intent.getStringExtra("notificationId") ?: "prayer"
        val soundOrdinal = intent.getIntExtra("soundTypeOrdinal", 0)
        val soundType = NotificationSoundType.entries[soundOrdinal]

        // 1) Make sure channels exist, even if app was never opened
        val nm = NotificationManager(context)
        nm.ensureAllChannels()

        // 2) Pick correct channel for this sound type
        val channelId = when (soundType) {
            NotificationSoundType.DEFAULT -> NotificationManager.CHANNEL_ID_DEFAULT
            NotificationSoundType.ADHAN   -> NotificationManager.CHANNEL_ID_ADHAN
            NotificationSoundType.IQAMA   -> NotificationManager.CHANNEL_ID_IQAMA
        }

        val notificationId = id.hashCode()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context)
            .notify(notificationId, builder.build())
    }
}
