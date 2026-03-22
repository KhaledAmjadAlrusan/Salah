package com.knight.salah.domain.repoistory.mosque

import com.knight.salah.data.mosque.MosqueApi
import com.knight.salah.domain.model.mosque.AwqatMosque

class MosqueRepository(
    private val mosqueApi: MosqueApi
) {
    suspend fun getMosques(): List<AwqatMosque> {
        return mosqueApi.getMosques().filter { it.isActive }
    }
}