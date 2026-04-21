package com.knight.salah.presentation.screens.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.knight.salah.domain.repository.mosque.MosqueRepository
import com.knight.salah.presentation.screens.search.data.SearchMosqueEvent
import com.knight.salah.presentation.screens.search.data.SearchMosqueState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchMosqueViewModel(
    private val repository: MosqueRepository,
) : ViewModel() {
    private val _mosqueState = MutableStateFlow(SearchMosqueState())
    val prayerState = _mosqueState.asStateFlow()

    init {
        getMosques()
    }

    fun onEvent(event: SearchMosqueEvent) {
        when (event) {
            is SearchMosqueEvent.OnSearchQueryChange -> searchMosques(event.query)
            is SearchMosqueEvent.OnMosqueSelected -> setSelectedMosqueId(event.id)
        }
    }

    private fun searchMosques(query: String) {
        _mosqueState.update {
            it.copy(
                searchQuery = query
            )
        }
        if (query.isBlank()) {
            _mosqueState.update {
                it.copy(
                    searchResults = it.mosques
                )
            }
            return
        }

        val filteredMosques = _mosqueState.value.mosques.filter { mosque ->
            mosque.name.contains(query, ignoreCase = true)
        }
        _mosqueState.update {
            it.copy(
                searchResults = filteredMosques
            )
        }
    }

    private fun setSelectedMosqueId(id: String) {
        viewModelScope.launch {
            repository.setSelectedMosque(id)
        }
    }

    private fun getMosques() {
        viewModelScope.launch {
            val mosques = repository.getMosques()
            _mosqueState.update { state ->
                state.copy(
                    mosques = mosques,
                    searchResults = mosques
                )
            }
        }
    }
}
