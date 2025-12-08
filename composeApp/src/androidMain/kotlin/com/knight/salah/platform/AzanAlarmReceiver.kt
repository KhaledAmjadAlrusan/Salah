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
        val notificationId = id.hashCode()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationManager.CHANNEL_ID,
                "Prayer channel",
                AndroidNotificationManager.IMPORTANCE_HIGH
            )
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager
            nm.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(
            context,
            NotificationManager.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        NotificationManagerCompat.from(context).notify(notificationId, builder.build())
    }
}
