package com.knight.salah.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.knight.salah.data.datastore.DataStoreInitiator.SETTING_DATA_STORE
import com.knight.salah.data.datastore.DataStoreInitiator.createDataStore

object SettingDataStore {
    fun createDataStore(context: Context): DataStore<Preferences> = createDataStore(
        producePath = { context.filesDir.resolve(SETTING_DATA_STORE).absolutePath }
    )
}
