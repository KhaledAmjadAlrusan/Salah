package com.knight.salah.data.datastore.mosue

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MosqueDataSource(
    private val dataStore: DataStore<Preferences>
) {
    private val SELECTED_MOSQUE_ID_KEY = stringPreferencesKey("selected_mosque_id")

    suspend fun setSelectedMosqueId(id: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_MOSQUE_ID_KEY] = id
        }
    }

    fun getSelectedMosqueId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[SELECTED_MOSQUE_ID_KEY]
        }
    }
}