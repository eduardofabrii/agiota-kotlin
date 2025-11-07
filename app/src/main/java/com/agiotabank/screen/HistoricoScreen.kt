package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.Green
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary

@Preview
@Composable
fun HistoricoScreen(goBack: () -> Unit = {})  {

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
//            items(mockedTransactions) { transacao ->
//                TransactionItem(transacao = transacao)
//            }
        }
    }
}

//@Composable
//private fun TransactionItem(transacao: Transacao) {
//    Row(
//        Modifier.fillMaxWidth(),
//        Arrangement.SpaceBetween,
//        Alignment.CenterVertically
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Surface(Modifier.size(40.dp), CircleShape, color = CardBackground) {
//                Box(contentAlignment = Alignment.Center) {
//                    Icon(
//                        when {
//                            transacao.descricao.contains("Pix") -> Icons.Filled.QrCode
//                            transacao.descricao.contains("boleto") || transacao.descricao.contains("Conta") -> Icons.Filled.Receipt
//                            transacao.descricao.contains("Transferência") -> Icons.Filled.SwapHoriz
//                            else -> Icons.Filled.Payments
//                        },
//                        null, tint = if (transacao.valor.startsWith("+")) Green else TextPrimary,
//                        modifier = Modifier.size(20.dp)
//                    )
//                }
//            }
//            Column {
//                Text(transacao.descricao, color = TextPrimary, fontSize = 14.sp, maxLines = 1)
//                Text(transacao.data, color = TextSecondary, fontSize = 12.sp)
//            }
//        }
//        Text(
//            transacao.valor,
//            color = if (transacao.valor.startsWith("+")) Green else TextPrimary,
//            fontSize = 15.sp,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}