package com.knight.salah.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingDataSource(
    private val dataStore: DataStore<Preferences>
) {
    companion object{
        //Notification key
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    }

    //setters and getters as flow for notification
    suspend fun setNotificationEnabled(enabled: Boolean)
    {
        withContext(Dispatchers.IO){
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    set(NOTIFICATION_ENABLED, enabled)
                }
            }
        }
    }

    fun getNotificationEnabled(): Flow<Boolean>{
        return dataStore.data.map {
            it[NOTIFICATION_ENABLED] ?: false
        }
    }
}