package com.agiotabank.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

import androidx.compose.runtime.Composable
import com.agiotabank.Telas

@Composable
fun Navegador(selected: Telas, onSelect: (Telas) -> Unit) {
    BottomAppBar {
        NavigationBarItem(
            selected = selected == Telas.HOME,
            onClick = { onSelect(Telas.HOME) },
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