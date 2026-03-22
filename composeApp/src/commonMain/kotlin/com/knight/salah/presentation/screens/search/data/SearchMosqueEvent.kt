package com.knight.salah.presentation.screens.search.data

sealed interface SearchMosqueEvent {
    data class OnSearchQueryChange(val query: String) : SearchMosqueEvent
    data class OnMosqueSelected(val id: String) : SearchMosqueEvent
}
