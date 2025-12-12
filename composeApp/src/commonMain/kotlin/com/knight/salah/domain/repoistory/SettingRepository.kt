package com.knight.salah.domain.repoistory

import com.knight.salah.data.datastore.SettingDataSource
import kotlinx.coroutines.flow.Flow

class SettingRepository(
    private val dataSource: SettingDataSource
) {
    suspend fun setNotificationEnabled(enabled: Boolean){
        dataSource.setNotificationEnabled(enabled)
    }

    fun getNotificationEnabled(): Flow<Boolean> {
        return dataSource.getNotificationEnabled()
    }
}