package com.knight.salah.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboard = LocalSoftwareKeyboardController.current

    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChange(it) },
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp)),
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        onSearchQueryChange("")
                        keyboard?.hide()
                    }
                )
            }
        },
        placeholder = {
            Text(
                text = "Search by name ",
                color = MaterialTheme.colorScheme.outline
            )
        },
        singleLine = true
    )
}