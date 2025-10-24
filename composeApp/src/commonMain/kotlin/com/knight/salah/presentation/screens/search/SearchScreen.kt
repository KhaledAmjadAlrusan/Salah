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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.knight.salah.domain.Mosque
import com.knight.salah.domain.generateMosques
import com.knight.salah.presentation.components.MosqueSearchResultItem
import com.knight.salah.presentation.components.SearchBar
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick:()->Unit,
    onMosqueSelected: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

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
    ) {paddingValues ->
        SearchContent(
            modifier = Modifier.padding(paddingValues),
            searchQuery = searchQuery,
            onMosqueSelected = {onMosqueSelected(searchQuery)},
            onSearchQuery = {searchQuery = it}

        )

    }
}
@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQuery: (String) -> Unit,
    onMosqueSelected: (String) -> Unit,

    ){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        SearchBar(
            searchQuery = searchQuery,
            onSearchQueryChange =  onSearchQuery ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Results Count
        Text(
            text = "mosques found",
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
            items(generateMosques()) { mosque->
                MosqueSearchResultItem(
                    mosque = mosque,
                    onMosqueClick = { onMosqueSelected(mosque.name) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    val mosque= Mosque(
        id = "1",
        name = "Khaled bin Al Waleed",
        location = "Vancover",
        distance = "3.5 km"
    )
    SearchScreen({})
}