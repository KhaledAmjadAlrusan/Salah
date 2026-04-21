package com.knight.salah.notifications

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

actual class PrayerNotificationManager(
    private val context: Context
) {
    private val systemNotificationManager get() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as AndroidNotificationManager

    private val alarmManager get() =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val prefs get() = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    @SuppressLint("MissingPermission")
    actual fun showNotification(
        title: String,
        description: String,
        sound: PrayerNotificationSound
    ) {
        val channelId = channelIdFor(sound)
        createNotificationChannelIfNeeded(sound)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        if (areNotificationEnabled) {
            NotificationManagerCompat.from(context).apply {
                cancelAll()
                notify(NOW_NOTIFICATION_ID, builder.build())
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    actual fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String,
        sound: PrayerNotificationSound
    ) {
        val triggerMillis = triggerAt.toEpochMilliseconds()

        val intent = Intent(context, PrayerAlarmReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("notificationId", id)
            putExtra("soundTypeOrdinal", sound.ordinal)
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

        saveScheduledNotificationId(id)
    }

    actual fun cancelScheduledNotification(id: String) {
        val intent = Intent(context, PrayerAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()

        removeScheduledNotificationId(id)
    }

    actual fun cancelAllPrayerNotifications() {
        val scheduledIds = getScheduledNotificationIds()

        scheduledIds.forEach { id ->
            cancelScheduledNotification(id)
        }

        clearScheduledNotificationIds()
        NotificationManagerCompat.from(context).cancelAll()
    }

    private val areNotificationEnabled get() =
        NotificationManagerCompat.from(context).areNotificationsEnabled()

    fun ensureAllChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannelIfNeeded(PrayerNotificationSound.DEFAULT)
            createNotificationChannelIfNeeded(PrayerNotificationSound.ADHAN)
            createNotificationChannelIfNeeded(PrayerNotificationSound.IQAMA)
        }
    }

    private fun channelIdFor(type: PrayerNotificationSound): String = when (type) {
        PrayerNotificationSound.DEFAULT -> CHANNEL_ID_DEFAULT
        PrayerNotificationSound.ADHAN   -> CHANNEL_ID_ADHAN
        PrayerNotificationSound.IQAMA   -> CHANNEL_ID_IQAMA
    }

    private fun soundUriFor(type: PrayerNotificationSound): Uri? = when (type) {
        PrayerNotificationSound.DEFAULT -> null
        PrayerNotificationSound.ADHAN   ->
            Uri.parse("android.resource://${context.packageName}/${R.raw.adhan}")
        PrayerNotificationSound.IQAMA   ->
            Uri.parse("android.resource://${context.packageName}/${R.raw.iqama}")
    }

    private fun createNotificationChannelIfNeeded(type: PrayerNotificationSound) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channelId = channelIdFor(type)
        if (systemNotificationManager.getNotificationChannel(channelId) != null) return

        val name = when (type) {
            PrayerNotificationSound.DEFAULT -> "Prayer (default)"
            PrayerNotificationSound.ADHAN   -> "Prayer – Adhan"
            PrayerNotificationSound.IQAMA   -> "Prayer – Iqama"
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

    private fun saveScheduledNotificationId(id: String) {
        val currentIds = getScheduledNotificationIds().toMutableSet()
        currentIds.add(id)
        prefs.edit().putStringSet(SCHEDULED_IDS_KEY, currentIds).apply()
    }

    private fun removeScheduledNotificationId(id: String) {
        val currentIds = getScheduledNotificationIds().toMutableSet()
        currentIds.remove(id)
        prefs.edit().putStringSet(SCHEDULED_IDS_KEY, currentIds).apply()
    }

    private fun getScheduledNotificationIds(): Set<String> {
        return prefs.getStringSet(SCHEDULED_IDS_KEY, emptySet()) ?: emptySet()
    }

    private fun clearScheduledNotificationIds() {
        prefs.edit().remove(SCHEDULED_IDS_KEY).apply()
    }

    companion object {
        const val CHANNEL_ID_DEFAULT = "prayer_default"
        const val CHANNEL_ID_ADHAN   = "prayer_adhan"
        const val CHANNEL_ID_IQAMA   = "prayer_iqama"

        private const val NOW_NOTIFICATION_ID = 1
        private const val PREFS_NAME = "prayer_notifications"
        private const val SCHEDULED_IDS_KEY = "scheduled_ids"
    }
}
