@file:OptIn(ExperimentalMaterial3Api::class)
package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.Investimento
import com.agiotabank.ui.InvestimentoViewModel
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InvestimentoDetailScreen(
    investimentoId: Long,
    onVoltar: () -> Unit,
    viewModel: InvestimentoViewModel = hiltViewModel()
) {
    var investimento by remember { mutableStateOf<Investimento?>(null) }

    // Busca o investimento pelo ID
    LaunchedEffect(key1 = investimentoId) {
        investimento = viewModel.getInvestimentoById(investimentoId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(DarkBackground),
        topBar = {
            TopAppBar(
                title = { Text(investimento?.nomeAtivo ?: "Detalhe") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (investimento != null) {
                val formatadorMoeda = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }

                InfoRow("Valor Aplicado:", formatadorMoeda.format(investimento!!.valorCompra))
                InfoRow("Tipo de Ativo:", investimento!!.tipoAtivo)
                InfoRow("Quantidade:", investimento!!.quantidade.toString())
                // ... Outros detalhes ...
            } else {
                Text("Investimento não encontrado.", color = TextSecondary)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextSecondary, fontSize = 14.sp)
        Text(value, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}