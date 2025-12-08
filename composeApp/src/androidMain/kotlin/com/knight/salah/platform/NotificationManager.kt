package com.knight.salah.platform

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.knight.salah.R
import kotlin.time.ExperimentalTime
import kotlin.time.Instant
import android.app.NotificationManager as AndroidNotificationManager


actual class NotificationManager(
    private val context: Context
) {
    private val systemNotificationManager get() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager

    private val alarmManager get() =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("MissingPermission")
    actual fun showNotification(
        title: String,
        description: String
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        createNotificationChannel()

        if (areNotificationEnabled) {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, builder.build())
        }
    }

    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    @OptIn(ExperimentalTime::class)
    actual fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String
    ) {
        val triggerMillis = triggerAt.toEpochMilliseconds()

        val intent = Intent(context, AzanAlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("notificationId", id)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            pendingIntent
        )
    }

    actual fun cancelScheduledNotification(id: String) {
        val intent = Intent(context, AzanAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private val areNotificationEnabled get() =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }.also { systemNotificationManager.createNotificationChannel(it) }
        }
    }

    companion object {
        private const val CHANNEL_DESCRIPTION = "Prayer notifications"
        private const val CHANNEL_NAME = "Prayer channel"
        const val CHANNEL_ID = "prayer_channel"
        private const val NOTIFICATION_ID = 1
    }
}
