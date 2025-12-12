package com.knight.salah

import android.content.ContentResolver
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import androidx.core.net.toUri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.knight.salah.datastore.SettingDataStore
import com.knight.salah.platform.NotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()


actual fun isSystemInDarkMode(): Boolean =
    (Resources.getSystem().configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES


actual fun onApplicationStartPlatformSpecific() {
    val customNotificationSound =
        (ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + "com.mmk.kmpnotifier.sample" + "/" + R.raw.custom_notification_sound).toUri()
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = R.drawable.ic_launcher_foreground,
            showPushNotification = true,
            notificationChannelData = NotificationPlatformConfiguration.Android.NotificationChannelData(
                soundUri = customNotificationSound.toString()
            )
        )
    )
}

actual fun platformModule(): Module = module {
    single { NotificationManager(context = SalahApp.applicationContext()) }
    single<DataStore<Preferences>> { SettingDataStore.createDataStore(SalahApp.applicationContext()) }
}