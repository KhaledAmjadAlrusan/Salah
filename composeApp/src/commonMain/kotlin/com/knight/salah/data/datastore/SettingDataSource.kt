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

        // Adhan key
        val ATHAN_SOUND_ENABLED = booleanPreferencesKey("athan_sound_enabled")
        val IQAMA_SOUND_ENABLED = booleanPreferencesKey("iqama_sound_enabled")

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

    suspend fun setAthanSoundEnabled(enabled: Boolean) {
        withContext(Dispatchers.IO) {
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    set(ATHAN_SOUND_ENABLED, enabled)
                }
            }
        }
    }

    fun getAdhanSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map {
            it[ATHAN_SOUND_ENABLED] ?: false
        }
    }

    suspend fun setIqamaSoundEnabled(enabled: Boolean) {
        withContext(Dispatchers.IO) {
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    set(IQAMA_SOUND_ENABLED, enabled)
                }
            }
        }
    }

    fun getIqamaSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map {
            it[IQAMA_SOUND_ENABLED] ?: false
        }
    }

}