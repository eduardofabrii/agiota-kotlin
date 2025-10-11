package com.agiotabank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.agiotabank.components.Navegador
import com.agiotabank.screen.HomeScreen
import com.agiotabank.screen.LoginScreen
import com.agiotabank.screen.SignInScreen
import com.agiotabank.ui.theme.AgiotaBankTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    var telaAtual by remember { mutableStateOf("signin") }
    var telaNavegador by remember { mutableStateOf(1) }


    when (telaAtual) {
        "signin" -> SignInScreen(onSignIn = { telaAtual = "login" })
        "login" -> LoginScreen(onLogin = { telaAtual = "home" }, onNavigateToSignIn = { telaAtual = "signin"})
        "home" -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    when (telaNavegador) {
                        1 -> HomeScreen()
                    }
                }
                Navegador(
                    selected = telaNavegador,
                    onSelect = { telaNavegador = it }
                )
            }
        }
    }
}