package com.knight.salah.platform

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager as AndroidNotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.knight.salah.R
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

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
        description: String,
        soundType: NotificationSoundType
    ) {
        val channelId = channelIdFor(soundType)
        createNotificationChannelIfNeeded(soundType)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (areNotificationEnabled) {
            NotificationManagerCompat.from(context)
                .notify(NOW_NOTIFICATION_ID, builder.build())
        }
    }

    @OptIn(ExperimentalTime::class)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    actual fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String,
        soundType: NotificationSoundType
    ) {
        val triggerMillis = triggerAt.toEpochMilliseconds()

        val intent = Intent(context, AzanAlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("notificationId", id)
            putExtra("soundTypeOrdinal", soundType.ordinal)
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

    fun ensureAllChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelIfNeeded(NotificationSoundType.DEFAULT)
            createNotificationChannelIfNeeded(NotificationSoundType.ADHAN)
            createNotificationChannelIfNeeded(NotificationSoundType.IQAMA)
        }
    }

    private fun channelIdFor(type: NotificationSoundType): String = when (type) {
        NotificationSoundType.DEFAULT -> CHANNEL_ID_DEFAULT
        NotificationSoundType.ADHAN   -> CHANNEL_ID_ADHAN
        NotificationSoundType.IQAMA   -> CHANNEL_ID_IQAMA
    }

    private fun soundUriFor(type: NotificationSoundType): Uri? = when (type) {
        NotificationSoundType.DEFAULT -> null
        NotificationSoundType.ADHAN   ->
            Uri.parse("android.resource://${context.packageName}/${R.raw.adhan}")
        NotificationSoundType.IQAMA   ->
            Uri.parse("android.resource://${context.packageName}/${R.raw.iqama}")
    }

    private fun createNotificationChannelIfNeeded(type: NotificationSoundType) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channelId = channelIdFor(type)
        if (systemNotificationManager.getNotificationChannel(channelId) != null) return

        val name = when (type) {
            NotificationSoundType.DEFAULT -> "Prayer (default)"
            NotificationSoundType.ADHAN   -> "Prayer – Adhan"
            NotificationSoundType.IQAMA   -> "Prayer – Iqama"
        }

        val channel = NotificationChannel(
            channelId,
            name,
            AndroidNotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Prayer notifications"
            val uri = soundUriFor(type)
            if (uri != null) {
                val audioAttrs = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                setSound(uri, audioAttrs)
            }
        }

        systemNotificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID_DEFAULT = "prayer_default"
        const val CHANNEL_ID_ADHAN   = "prayer_adhan"
        const val CHANNEL_ID_IQAMA   = "prayer_iqama"

        private const val NOW_NOTIFICATION_ID = 1
    }
}
