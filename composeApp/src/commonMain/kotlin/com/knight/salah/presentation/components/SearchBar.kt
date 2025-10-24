package com.knight.salah.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onClickFetch:()->Unit={}
) {
    TextField(
        value = searchQuery,
        onValueChange = {onSearchQueryChange(searchQuery)},
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        placeholder = {
            Text(
                text = "Search by name ",
                color = MaterialTheme.colorScheme.outline
            )
        },

        keyboardActions = KeyboardActions(
            onSearch = { onClickFetch /* Handle search */ }
        ),
        singleLine = true
    )
}