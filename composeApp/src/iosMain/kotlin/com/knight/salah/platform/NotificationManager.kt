package com.knight.salah.platform

import kotlinx.datetime.toNSDate
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDateComponents
import platform.Foundation.NSUUID
import platform.UserNotifications.*
import platform.darwin.NSObject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


actual class NotificationManager {

    private val center = UNUserNotificationCenter.currentNotificationCenter()

    actual fun showNotification(
        title: String,
        description: String
    ) {
        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(description)
        }

        val uuid = NSUUID.UUID().UUIDString()
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content, null)

        setDelegateIfNeeded()
        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error -> $error")
            } else {
                println("Notification sent")
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    actual fun scheduleNotification(
        id: String,
        triggerAt: Instant,
        title: String,
        description: String
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

        val content = UNMutableNotificationContent().apply {
            setTitle(title)
            setBody(description)
            // if you have a custom azan sound in the bundle:
            // sound = UNNotificationSound.soundNamed("custom_notification_sound.wav")
        }

        val trigger = UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            comps,
            repeats = false
        )

        val request = UNNotificationRequest.requestWithIdentifier(id, content, trigger)

        setDelegateIfNeeded()
        center.addNotificationRequest(request) { error ->
            if (error != null) {
                println("Error scheduling -> $error")
            } else {
                println("Scheduled notification: $id")
            }
        }
    }

    actual fun cancelScheduledNotification(id: String) {
        center.removePendingNotificationRequestsWithIdentifiers(listOf(id))
    }

    // reuse one delegate
    private var delegateSet = false
    private fun setDelegateIfNeeded() {
        if (delegateSet) return
        delegateSet = true

        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                withCompletionHandler(UNNotificationPresentationOptionAlert)
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) {
                withCompletionHandler()
            }
        }
    }
}
