package com.knight.salah.domain.repoistory.mosque

import com.knight.salah.data.datastore.mosue.MosqueDataSource
import com.knight.salah.data.mosque.MosqueApi
import com.knight.salah.domain.model.remote.mosque.AwqatMosque
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest

class MosqueRepository(
    private val mosqueApi: MosqueApi,
    private val mosqueDataSource: MosqueDataSource
) {
    private var cachedMosques: List<AwqatMosque>? = null

    suspend fun getMosques(): List<AwqatMosque> {
        return cachedMosques ?: mosqueApi.getMosques().also {
            cachedMosques = it
        }
    }

    suspend fun setSelectedMosque(mosqueId: String) {
        mosqueDataSource.setSelectedMosqueId(mosqueId)
    }

    fun observeSelectedMosqueId(): Flow<String?> {
        return mosqueDataSource.getSelectedMosqueId()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun observeSelectedMosque(): Flow<AwqatMosque?> {
        return mosqueDataSource.getSelectedMosqueId()
            .mapLatest { selectedId ->
                if (selectedId == null) {
                    null
                } else {
                    getMosques().firstOrNull { it.id == selectedId }
                }
            }
            .distinctUntilChanged()
    }
}