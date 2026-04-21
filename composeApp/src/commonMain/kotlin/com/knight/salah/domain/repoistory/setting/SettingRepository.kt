package com.knight.salah.domain.repoistory.setting

import com.knight.salah.data.datastore.setting.SettingDataSource
import kotlinx.coroutines.flow.Flow

class SettingRepository(
    private val dataSource: SettingDataSource
) {
    suspend fun setNotificationEnabled(enabled: Boolean) {
        dataSource.setNotificationEnabled(enabled)
    }

    fun getNotificationEnabled(): Flow<Boolean> {
        return dataSource.getNotificationEnabled()
    }

    suspend fun setAthanSoundEnabled(enabled: Boolean) {
        dataSource.setAthanSoundEnabled(enabled)
    }

    fun getAthanSoundEnabled(): Flow<Boolean> {
        return dataSource.getAthanSoundEnabled()
    }

    suspend fun setIqamaSoundEnabled(enabled: Boolean) {
        dataSource.setIqamaSoundEnabled(enabled)
    }

    fun getIqamaSoundEnabled(): Flow<Boolean> {
        return dataSource.getIqamaSoundEnabled()
    }
}