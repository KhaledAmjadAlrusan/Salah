package com.knight.salah.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private val NOTIFICATION_ENABLED_KEY = booleanPreferencesKey("notification_enabled")
    private val ATHAN_SOUND_ENABLED_KEY = booleanPreferencesKey("athan_sound_enabled")
    private val IQAMA_SOUND_ENABLED_KEY = booleanPreferencesKey("iqama_sound_enabled")

    suspend fun setNotificationEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] = enabled
        }
    }

    fun getNotificationEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[NOTIFICATION_ENABLED_KEY] ?: false
        }
    }

    suspend fun setAthanSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[ATHAN_SOUND_ENABLED_KEY] = enabled
        }
    }

    fun getAthanSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ATHAN_SOUND_ENABLED_KEY] ?: true
        }
    }

    suspend fun setIqamaSoundEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[IQAMA_SOUND_ENABLED_KEY] = enabled
        }
    }

    fun getIqamaSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IQAMA_SOUND_ENABLED_KEY] ?: true
        }
    }
}