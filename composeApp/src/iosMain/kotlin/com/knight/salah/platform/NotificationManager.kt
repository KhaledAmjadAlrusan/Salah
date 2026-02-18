package com.knight.salah.platform

import kotlinx.datetime.toNSDate
import platform.Foundation.NSBundle
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDateComponents
import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

private const val TAG = "[iOS NotificationManager]"

actual class NotificationManager {
    private val center = UNUserNotificationCenter.currentNotificationCenter()

    init {
        requestAuthorizationIfNeeded()
    }

    private fun requestAuthorizationIfNeeded() {
        center.requestAuthorizationWithOptions(
            UNAuthorizationOptionAlert or UNAuthorizationOptionSound
        ) { granted, error ->
            println("$TAG authorization callback -> granted=$granted error=$error")
        }
    }

    actual fun showNotification(
        title: String,
        description: String,
        soundType: NotificationSoundType
    ) {
        val sound = soundForType(soundType)
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(description)
            setSound(sound)
        }
        val uuid = NSUUID.UUID().UUIDString()
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content, null)
        setDelegateIfNeeded()
        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("$TAG showNotification() addNotificationRequest error=$error")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    actual fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String,
        soundType: NotificationSoundType
    ) {
        val date = triggerAt.toNSDate()
        val calendar = NSCalendar.currentCalendar
        val comps = calendar.components(
            NSCalendarUnitYear or
                    NSCalendarUnitMonth or
                    NSCalendarUnitDay or
                    NSCalendarUnitHour or
                    NSCalendarUnitMinute,
            fromDate = date
        ) as NSDateComponents
        val sound = soundForType(soundType)
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(description)
            setSound(sound)
        }
        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            comps,
            repeats = false
        )
        val request = UNNotificationRequest.requestWithIdentifier(id, content, trigger)
        setDelegateIfNeeded()
        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("$TAG scheduleNotification() addNotificationRequest error=$error")
            }
        }
    }

    actual fun cancelScheduledNotification(id: String) {
        center.removePendingNotificationRequestsWithIdentifiers(listOf(id))
    }

    actual fun cancelAllPrayerNotifications() {
        center.removeAllPendingNotificationRequests()
    }

    private fun soundForType(type: NotificationSoundType): UNNotificationSound? {
        val bundle = NSBundle.mainBundle
        return when (type) {
            NotificationSoundType.DEFAULT -> {
                UNNotificationSound.defaultSound()
            }
            NotificationSoundType.ADHAN -> {
                val path = bundle.pathForResource("adhan_short", "caf")
                if (path == null) {
                    UNNotificationSound.defaultSound()
                } else {
                    UNNotificationSound.soundNamed("adhan_short.caf")
                }
            }
            NotificationSoundType.IQAMA -> {
                val path = bundle.pathForResource("iqama_short", "caf")
                if (path == null) {
                    UNNotificationSound.defaultSound()
                } else {
                    UNNotificationSound.soundNamed("iqama_short.caf")
                }
            }
        }
    }

    private var delegateSet = false

    private fun setDelegateIfNeeded() {
        if (delegateSet) {
            return
        }
        delegateSet = true
        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                val id = willPresentNotification.request.identifier
                val title = willPresentNotification.request.content.title
                withCompletionHandler(
                    UNNotificationPresentationOptionAlert or
                            UNNotificationPresentationOptionSound
                )
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) {
                val id = didReceiveNotificationResponse.notification.request.identifier
                val title = didReceiveNotificationResponse.notification.request.content.title
                withCompletionHandler()
            }
        }
    }
}