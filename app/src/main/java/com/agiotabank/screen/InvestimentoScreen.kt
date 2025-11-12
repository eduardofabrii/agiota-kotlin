@file:OptIn(ExperimentalMaterial3Api::class)
package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.components.InvestmentCard // <-- Usando o novo componente
import com.agiotabank.ui.InvestmentState
import com.agiotabank.ui.InvestimentoViewModel
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InvestimentoScreen(
    onVoltar: () -> Unit = {},
    onNavigateToDetail: (Long) -> Unit, // <-- Para o clique no card
    onNavigateToInvestir: () -> Unit, // <-- Para o FAB
    viewModel: InvestimentoViewModel = hiltViewModel()
) {
    // 1. Coletando o novo State
    val state by viewModel.state.collectAsState()
    val formatadorMoeda = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        topBar = {
            TopAppBar(
                title = { Text("Investimentos", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        floatingActionButton = {
            // 2. FAB como sugerido
            ExtendedFloatingActionButton(
                text = { Text("Investir") },
                icon = { Icon(Icons.Default.Add, null) },
                onClick = onNavigateToInvestir
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
        ) {

            item {
                Text("Total Investido", color = TextSecondary, fontSize = 14.sp)
                Text(
                    formatadorMoeda.format(state.totalInvestido), // <-- Do State
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(24.dp))
                Text("Meus Ativos", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            }

            // 3. Renderizando a lista com o novo Card
            items(state.investimentos) { investimento ->
                InvestmentCard(
                    investment = investimento,
                    onClick = {
                        onNavigateToDetail(investimento.id)
                    }
                )
            }
        }
    }
}