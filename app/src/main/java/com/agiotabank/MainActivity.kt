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
import com.agiotabank.screen.TransacaoScreen
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
    var telaAtual by remember { mutableStateOf(Telas.SIGNIN) }

    when (telaAtual) {
        Telas.SIGNIN -> SignInScreen(onSignIn = { telaAtual = Telas.LOGIN })
        Telas.LOGIN -> LoginScreen(
            onLogin = { telaAtual = Telas.HOME},
            onNavigateToSignIn = { telaAtual = Telas.SIGNIN })

        Telas.HOME -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    HomeScreen(onNavigate = { telaAtual = it })
                }
                Navegador(
                    selected = Telas.HOME,
                    onSelect = { telaAtual = it }
                )
            }
        }
        Telas.TRANSACAO -> TransacaoScreen(goBack = { telaAtual = Telas.HOME })
        Telas.CARTOES -> Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                CardScreen()
            }
            Navegador(
                selected = Telas.HOME,
                onSelect = { telaAtual = it }
            )
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    HomeScreen(onNavigate = { telaAtual = it })
                }
                Navegador(
                    selected = Telas.HOME,
                    onSelect = { telaAtual = it }
                )
            }
        }
    }
}

enum class Telas {
    SIGNIN,
    LOGIN,
    HOME,
    TRANSACAO,
    EMPRESTIMO,
    CARTOES
}