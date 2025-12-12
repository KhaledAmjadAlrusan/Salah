package com.knight.salah

import com.knight.salah.data.datastore.DataStoreInitiator
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
fun provideDataStore() = DataStoreInitiator.createDataStore {
    val documentDir: NSURL? = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    requireNotNull(documentDir).path + "/${DataStoreInitiator.SETTING_DATA_STORE}"
}
