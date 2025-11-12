package com.agiotabank.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.agiotabank.screen.CardScreen
import com.agiotabank.screen.ComprarInvestimentoScreen
import com.agiotabank.screen.HistoricoScreen
import com.agiotabank.screen.HomeScreen
import com.agiotabank.screen.InvestimentoDetailScreen
import com.agiotabank.screen.InvestimentoScreen
import com.agiotabank.screen.LoginScreen
import com.agiotabank.screen.MeusEmprestimosScreen
import com.agiotabank.screen.PerfilScreen
import com.agiotabank.screen.PixScreen
import com.agiotabank.screen.PixKeyScreen
import com.agiotabank.screen.SignInScreen
import com.agiotabank.screen.SolicitarEmprestimoScreen
import com.agiotabank.screen.TransacaoScreen
import com.agiotabank.ui.ContaViewModel
import com.agiotabank.ui.PixKeyViewModel
import com.agiotabank.ui.TransacaoViewModel
import com.agiotabank.ui.theme.TextPrimary
import kotlinx.coroutines.launch

enum class Telas {
    SIGNIN, LOGIN, HOME, TRANSACAO, EMPRESTIMO, CARTOES, HISTORICO, PERFIL, PIX, MEUS_EMPRESTIMOS, SOLICITAR_EMPRESTIMO, INVESTIMENTO, INVESTIMENTO_DETALHE, INVESTIR, PIXKEY
}

@Composable
fun Navegador() {
    val nav = rememberNavController()
    val contaVm: ContaViewModel = hiltViewModel()
    val conta by contaVm.contaLogada.collectAsState(initial = null)
    val pixKeyvm: PixKeyViewModel = hiltViewModel()
    val transacaoVm: TransacaoViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()

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

                onCreateAccount = { nome, email, senha, cpf, telefone ->
                    contaVm.criar(nome, email, senha, cpf, telefone)
                })
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
                onNavigate = { tela ->
                    val route = when (tela) {
                        Telas.CARTOES -> "${Telas.CARTOES.name}/${conta?.id ?: 0L}"
                        Telas.TRANSACAO -> "${Telas.TRANSACAO.name}/${conta?.id}"
                        Telas.PIX -> Telas.PIX.name
                        Telas.INVESTIMENTO -> "${Telas.INVESTIMENTO.name}/${conta?.id}"
                        else -> tela.name
                    }
                    nav.navigate(route)
                },
                bottomBar = ({
                    BottomBar(telaAtual = Telas.HOME, onTelaSelecionada = { nav.navigate(it.name) })
                }),
                nome = conta?.nome ?: "",
                conta = conta
            )
        }
        composable(Telas.PIX.name) {
            PixScreen(
                goBack = { nav.popBackStack() },
                conta = conta,
                viewModel = transacaoVm,
                contaViewModel = contaVm,
                pixKeyViewModel = pixKeyvm
            )
        }
        composable(Telas.PERFIL.name) {
            PerfilScreen(
                onSair = {
                    coroutineScope.launch {
                        contaVm.logout()
                        nav.navigate(Telas.LOGIN.name) {
                            popUpTo(Telas.HOME.name) {
                                inclusive = true
                            }
                        }
                    }
                },
                goBack = { nav.popBackStack() },
                conta = conta,
                onUpdateNome = { novoNome ->
                    contaVm.updateNome(novoNome)
                }
            )
        }

        composable(
            route = "${Telas.TRANSACAO.name}/{contaId}",
            arguments = listOf(navArgument("contaId") {
                type = NavType.LongType
                defaultValue = 0L
            })
        ) {
            TransacaoScreen(
                goBack = { nav.popBackStack() },
                conta = conta,
                viewModel = transacaoVm,
                contaViewModel = contaVm
            )
        }
        composable(
            route = "${Telas.CARTOES.name}/{contaId}",
            arguments = listOf(navArgument("contaId") { type = NavType.LongType })
        ) { backStackEntry ->
            CardScreen(
                goBack = { nav.popBackStack() },
                holderName = conta?.nome ?: "",
                contaId = backStackEntry.arguments?.getLong("contaId") ?: 0L
            )
        }
        composable(Telas.PIXKEY.name) {
            PixKeyScreen(goBack = { nav.popBackStack() })
        }
        composable(Telas.MEUS_EMPRESTIMOS.name) { MeusEmprestimosScreen(goBack = { nav.popBackStack() }) }
        composable(Telas.SOLICITAR_EMPRESTIMO.name) { SolicitarEmprestimoScreen(goBack = { nav.popBackStack()}) }
        composable(Telas.HISTORICO.name) { HistoricoScreen(goBack = { nav.popBackStack() }) }

        composable(
            route = "${Telas.INVESTIMENTO.name}/{contaId}",
            arguments = listOf(navArgument("contaId") {
                type = NavType.LongType
                defaultValue = 0L
            })
        ) {
            InvestimentoScreen(
                onVoltar = { nav.popBackStack() },
                onNavigateToDetail = { investimentoId ->
                    nav.navigate("${Telas.INVESTIMENTO_DETALHE.name}/$investimentoId")
                },
                onNavigateToInvestir = {
                    nav.navigate(Telas.INVESTIR.name)
                }
            )
        }

        composable(
            route = "${Telas.INVESTIMENTO_DETALHE.name}/{investimentoId}",
            arguments = listOf(navArgument("investimentoId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("investimentoId") ?: 0L
            InvestimentoDetailScreen(
                investimentoId = id,
                onVoltar = { nav.popBackStack() }
            )
        }

        composable(Telas.INVESTIR.name) {
            ComprarInvestimentoScreen(
                onVoltar = { nav.popBackStack() }
            )
        }
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
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Início"
                )
            },
            label = { Text("Início") })
    }
}
