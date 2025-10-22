package com.agiotabank.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.agiotabank.screen.CardScreen
import com.agiotabank.screen.EmprestimoScreen
import com.agiotabank.screen.HomeScreen
import com.agiotabank.screen.LoginScreen
import com.agiotabank.screen.SignInScreen
import com.agiotabank.screen.TransacaoScreen

enum class Telas {
    SIGNIN,
    LOGIN,
    HOME,
    TRANSACAO,
    EMPRESTIMO,
    CARTOES
}
@Composable
fun Navegador() {
    var telaAtual by remember { mutableStateOf(Telas.SIGNIN) }
    when (telaAtual) {
        Telas.SIGNIN -> SignInScreen(onSignIn = { telaAtual = Telas.LOGIN })
        Telas.LOGIN -> LoginScreen(
            onLogin = { telaAtual = Telas.HOME},
            onNavigateToSignIn = { telaAtual = Telas.SIGNIN })

        Telas.HOME -> HomeScreen(onNavigate = { telaAtual = it }, bottomBar = ({
            BottomBar(telaAtual = telaAtual, onTelaSelecionada = { telaAtual = it })
        }))
        Telas.TRANSACAO -> TransacaoScreen(goBack = { telaAtual = Telas.HOME })
        Telas.CARTOES -> CardScreen({ telaAtual = Telas.HOME })
        Telas.EMPRESTIMO -> EmprestimoScreen { telaAtual = Telas.HOME }
        else -> HomeScreen(onNavigate = { telaAtual = it })
        }
}


@Composable
fun BottomBar(telaAtual: Telas, onTelaSelecionada: (Telas) -> Unit) {
    BottomAppBar {
        NavigationBarItem(
            selected = telaAtual == Telas.HOME,
            onClick = { onTelaSelecionada(Telas.HOME) },
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