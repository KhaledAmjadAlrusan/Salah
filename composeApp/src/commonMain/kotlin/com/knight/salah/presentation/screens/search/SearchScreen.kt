package com.knight.salah.presentation.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.knight.salah.domain.model.mosque.AwqatMosque
import com.knight.salah.presentation.components.MosqueSearchResultItem
import com.knight.salah.presentation.components.SearchBar
import com.knight.salah.presentation.screens.search.data.SearchMosqueEvent
import com.knight.salah.presentation.screens.search.data.SearchMosqueState
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchMosqueState,
    onEvent: (SearchMosqueEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Search Mosques",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SearchContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEvent = onEvent,
            onBackClick = onBackClick
        )
    }
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchMosqueState,
    onEvent: (SearchMosqueEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        SearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onEvent(SearchMosqueEvent.OnSearchQueryChange(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Results Count
        Text(
            text = "Mosques found",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        // Search Results
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = state.searchResults, key = { it.id }) { mosque ->
                MosqueSearchResultItem(
                    mosque = mosque,
                    onMosqueClick = {
                        onEvent(SearchMosqueEvent.OnMosqueSelected(mosque.id))
                        onBackClick()
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val mosque = AwqatMosque(
        id = "1",
        name = "Khaled bin Al Waleed",
        city = "Vancover",
        address = "Vancover",
        isActive = true,
        latitude = 37.7749,
        longitude = -122.4194,
        type = "mosque",
        provinceState = "BC"
    )
    val state = SearchMosqueState(
        mosques = listOf(mosque),
        searchResults = listOf(mosque)
    )
    SearchScreen(
        state = state,
        onEvent = {},
        onBackClick = {}
    )
}