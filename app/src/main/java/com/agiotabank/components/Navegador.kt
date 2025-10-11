package com.agiotabank.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

import androidx.compose.runtime.Composable

@Composable
fun Navegador(selected: Int, onSelect: (Int) -> Unit) {
    BottomAppBar {
        NavigationBarItem(
            selected = selected == 1,
            onClick = { onSelect(1) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home, // Ícone do pacote material.icons
                    contentDescription = "Início"
                )
            },
            label = { Text("Início") }
        )
    }
}