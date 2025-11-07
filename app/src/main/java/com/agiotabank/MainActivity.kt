package com.agiotabank

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.agiotabank.components.Navegador
import com.agiotabank.ui.theme.AgiotaBankTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            AgiotaBankTheme(
                darkTheme = true,
                dynamicColor = false,
            ) {
                AgiotaApp()
            }
        }
    }
}

@Composable
fun AgiotaApp() {
    Navegador()
}

