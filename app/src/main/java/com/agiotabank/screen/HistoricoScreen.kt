package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.Transacao
import com.agiotabank.ui.ContaViewModel
import com.agiotabank.ui.TransacaoViewModel
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.Green
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.Red
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun HistoricoScreen(goBack: () -> Unit = {}) {
    val viewModel: TransacaoViewModel = hiltViewModel()
    val contaViewModel: ContaViewModel = hiltViewModel()
    val idConta by contaViewModel.contaId.collectAsState()
    val transacoes by viewModel.transacoes.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        topBar = {
            // TopBar similar à da TransacaoScreen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBackground)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { goBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
                Text(
                    "Histórico de Transações",
                    color = TextPrimary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { }) { Icon(Icons.Filled.Notifications, null, tint = TextPrimary) }
                    IconButton(onClick = { }) { Icon(Icons.Filled.History, null, tint = TextPrimary) }
                    Box(
                        Modifier.size(32.dp).clip(CircleShape).background(LightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("C", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(transacoes) { transacao ->
                TransactionItem(transacao = transacao, contaId = idConta ?: 0, contaViewModel = contaViewModel)
            }
        }
    }
}

@Composable
private fun TransactionItem(transacao: Transacao, contaId: Long, contaViewModel: ContaViewModel = hiltViewModel()) {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val data = simpleDateFormat.format(Date(transacao.timestamp))
    Row(
        Modifier.fillMaxWidth(),
        Arrangement.SpaceBetween,
        Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(Modifier.size(40.dp), CircleShape, color = CardBackground) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.SwapHoriz,
                        null, tint = TextPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column {
                val destinoConta by produceState(initialValue = null as com.agiotabank.data.Conta?, key1 = transacao.paraContaId) {
                    value = contaViewModel.findContaById(transacao.paraContaId)
                }
                val origemConta by produceState(initialValue = null as com.agiotabank.data.Conta?, key1 = transacao.deContaId) {
                    value = contaViewModel.findContaById(transacao.deContaId)
                }
                Text(if (transacao.deContaId == contaId) "Para ${destinoConta?.nome}" else "De ${origemConta?.nome}", fontSize = 14.sp, maxLines = 1)
                Text(data, color = TextSecondary, fontSize = 12.sp)
            }
        }
        Text(
            "R$ %.2f".format(transacao.valor),
            color = if (transacao.deContaId == contaId) Red else Green,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}