package com.knight.salah.notifications

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.knight.salah.R

class PrayerAlarmReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Prayer"
        val description = intent.getStringExtra("description") ?: ""
        val id = intent.getStringExtra("notificationId") ?: "prayer"
        val soundOrdinal = intent.getIntExtra("soundTypeOrdinal", 0)
        val sound = PrayerNotificationSound.entries[soundOrdinal]

        // 1) Make sure channels exist, even if app was never opened
        val notificationManager = PrayerNotificationManager(context)
        notificationManager.ensureAllChannels()

        // 2) Pick correct channel for this sound type
        val channelId = when (sound) {
            PrayerNotificationSound.DEFAULT -> PrayerNotificationManager.CHANNEL_ID_DEFAULT
            PrayerNotificationSound.ADHAN   -> PrayerNotificationManager.CHANNEL_ID_ADHAN
            PrayerNotificationSound.IQAMA   -> PrayerNotificationManager.CHANNEL_ID_IQAMA
        }

        val notificationId = id.hashCode()

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).apply {
            cancelAll()
            notify(notificationId, builder.build())
        }
    }
}
