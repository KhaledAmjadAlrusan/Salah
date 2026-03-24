package com.knight.salah.presentation.screens.search.data

import com.knight.salah.domain.model.remote.mosque.AwqatMosque

data class SearchMosqueState(
    val searchQuery: String = "",
    val mosques: List<AwqatMosque> = emptyList(),
    val searchResults: List<AwqatMosque> = emptyList()
)