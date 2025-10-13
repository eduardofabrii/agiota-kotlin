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
import com.agiotabank.screen.CardScreen

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

        "login" -> LoginScreen(
            onLogin = { telaAtual = "home" },
            onNavigateToSignIn = { telaAtual = "signin" }
        )

        "home" -> {
            // sub-rota local da Home: "home" (lista) ou "card" (detalhe)
            var homeRoute by remember { mutableStateOf("home") }

            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                ) {
                    when (telaNavegador) {
                        1 -> {
                            when (homeRoute) {
                                "home" -> HomeScreen(
                                    onOpenCard = { homeRoute = "card" } // <- seta do cartão chama isso
                                )
                                "card" -> CardScreen() // tela estática do cartão
                            }
                        }
                        // se tiver outras abas, mantém aqui:
                        // 2 -> OutraTela()
                        else -> HomeScreen(onOpenCard = { homeRoute = "card" })
                    }
                }

                // se quiser esconder a barra quando estiver na tela do cartão, comenta este bloco:
                Navegador(
                    selected = telaNavegador,
                    onSelect = { selecionado ->
                        telaNavegador = selecionado
                        homeRoute = "home" // ao trocar de aba, volta para a lista
                    }
                )
            }
        }
    }
}