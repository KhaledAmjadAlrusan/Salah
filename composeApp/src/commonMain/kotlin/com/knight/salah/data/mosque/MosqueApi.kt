package com.knight.salah.data.mosque

import com.knight.salah.domain.model.mosque.AwqatMosque

interface MosqueApi {
    suspend fun getMosques(): List<AwqatMosque>
}