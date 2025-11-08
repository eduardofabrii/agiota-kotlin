package com.agiotabank.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.agiotabank.screen.CardScreen
import com.agiotabank.screen.EmprestimoScreen
import com.agiotabank.screen.HistoricoScreen
import com.agiotabank.screen.HomeScreen
import com.agiotabank.screen.LoginScreen
import com.agiotabank.screen.PerfilScreen
import com.agiotabank.screen.SignInScreen
import com.agiotabank.screen.TransacaoScreen
import com.agiotabank.ui.ContaViewModel

enum class Telas {
    SIGNIN, LOGIN, HOME, TRANSACAO, EMPRESTIMO, CARTOES, HISTORICO, PERFIL
}

@Composable
fun Navegador() {
    val nav = rememberNavController()
    val contaVm: ContaViewModel = hiltViewModel()
    val conta by contaVm.contaLogada.collectAsState(initial = null)

    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        navController = nav,
        startDestination = if (conta != null) Telas.HOME.name else Telas.LOGIN.name
    ) {
        composable(Telas.SIGNIN.name) {
            SignInScreen(
                onSignIn = { nav.navigate(Telas.LOGIN.name) },
                onCreateAccount = { nome, email, senha -> contaVm.criar(nome, email, senha) })
        }
        composable(Telas.LOGIN.name) {
            LoginScreen(onLogin = { email, senha ->
                contaVm.login(email, senha) {
                    nav.navigate(Telas.HOME.name)
                }
            }, onNavigateToSignIn = { nav.navigate(Telas.SIGNIN.name) })
        }
        composable(Telas.HOME.name) {
            HomeScreen(
                onNavigate = { nav.navigate(it.name) },
                bottomBar = ({
                    BottomBar(telaAtual = Telas.HOME, onTelaSelecionada = { nav.navigate(it.name) })
                }),
                nome = conta?.nome ?: ""
            )
        }
        composable(Telas.TRANSACAO.name) { TransacaoScreen(goBack = { nav.popBackStack() }) }
        composable(Telas.CARTOES.name) {
            CardScreen(
                goBack = { nav.popBackStack() },
                holderName = conta?.nome ?: ""
            )
        }
        composable(Telas.EMPRESTIMO.name) { EmprestimoScreen { nav.popBackStack() } }
        composable(Telas.HISTORICO.name) { HistoricoScreen(goBack = { nav.popBackStack() }) }
        composable(Telas.PERFIL.name) { PerfilScreen(onSair = {nav.navigate(Telas.LOGIN.name)}, goBack = { nav.popBackStack() }, conta = conta) }
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
            label = { Text("Início") })
    }
}