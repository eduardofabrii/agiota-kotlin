package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.model.Transacao
import com.agiotabank.ui.theme.*

@Composable
fun HomeScreen(onOpenCard: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBackground)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Olá, Cliente",
                fontSize = 20.sp,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { /* RF-020: Alertas */ }) {
                    Icon(Icons.Filled.Notifications, null, tint = TextPrimary)
                }
                IconButton(onClick = { /* RF-019: Histórico de acessos */ }) {
                    Icon(Icons.Filled.History, null, tint = TextPrimary)
                }
                Box(
                    Modifier.size(32.dp).clip(CircleShape).background(LightBlue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("C", color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // RF-006: Saldo em tempo real
            item {
                Column {
                    Text("Saldo disponível", color = TextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("R$ 3.363,32", color = TextPrimary, fontSize = 28.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.width(12.dp))
                        IconButton(onClick = { }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Filled.Visibility, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            item {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    ActionButton(Icons.Filled.AccountBalance, "Transferir")  // RF-009, RF-010
                    ActionButton(Icons.Filled.QrCode, "PIX")  // RF-010, RF-022
                    ActionButton(Icons.Filled.Receipt, "Boletos")  // RF-012
                    ActionButton(Icons.Filled.MoreHoriz, "Mais")
                }
            }

            // RF-014: Cartão de crédito
            item {
                Card(Modifier.fillMaxWidth(), RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(CardBackground)) {
                    Row(Modifier.fillMaxWidth().padding(20.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.CreditCard, null, tint = LightBlue, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Cartão de crédito", color = TextSecondary, fontSize = 13.sp)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text("Fatura atual", color = TextPrimary, fontSize = 12.sp)
                            Text("R$ 563,00", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = onOpenCard) {
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextSecondary)
                        }
                    }
                }
            }

            // RF-016 e RF-017
            item {
                Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(12.dp)) {
                    SmallCard(Icons.Filled.TrendingUp, "Investimentos", "R$ 7.800", Modifier.weight(1f))  // RF-017
                    SmallCard(Icons.Filled.Savings, "Empréstimo", "Até R$ 5.000", Modifier.weight(1f))  // RF-016
                }
            }

            // RF-007: Extrato recente
            item {
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Text("Extrato", color = TextPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = { /* RF-023: Exportar PDF */ }) {
                            Icon(Icons.Filled.PictureAsPdf, null, tint = LightBlue, modifier = Modifier.size(20.dp))
                        }
                        TextButton(onClick = { }) {
                            Text("Ver tudo", color = LightBlue, fontSize = 14.sp)
                        }
                    }
                }
            }

            items(listOf(
                Transacao("Pix recebido de João Silva", "+R$ 250,00", "Hoje, 14:32"),
                Transacao("Pagamento de boleto - Conta de luz", "-R$ 120,00", "Ontem, 09:15"),
                Transacao("Transferência para Maria Santos", "-R$ 340,00", "05/10, 16:22"),
                Transacao("Pix recebido de Pedro Costa", "+R$ 90,00", "04/10, 11:05"),
                Transacao("Pagamento recorrente - Internet", "-R$ 99,90", "03/10, 08:00")
            )) { t ->
                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(Modifier.size(40.dp), CircleShape, color = CardBackground) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    when {
                                        t.descricao.contains("Pix") -> Icons.Filled.QrCode
                                        t.descricao.contains("boleto") -> Icons.Filled.Receipt
                                        t.descricao.contains("Transferência") -> Icons.Filled.SwapHoriz
                                        else -> Icons.Filled.Payments
                                    },
                                    null, tint = if (t.valor.startsWith("+")) Green else TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Column {
                            Text(t.descricao, color = TextPrimary, fontSize = 14.sp, maxLines = 1)
                            Text(t.data, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                    Text(
                        t.valor,
                        color = if (t.valor.startsWith("+")) Green else TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun ActionButton(icon: ImageVector, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(Modifier.size(56.dp), CircleShape, CardBackground) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = TextPrimary, modifier = Modifier.size(24.dp))
            }
        }
        Text(label, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun SmallCard(icon: ImageVector, title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier, RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(CardBackground)) {
        Column(Modifier.padding(16.dp), Arrangement.spacedBy(8.dp)) {
            Icon(icon, null, tint = LightBlue, modifier = Modifier.size(20.dp))
            Text(title, color = TextSecondary, fontSize = 12.sp)
            Text(value, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}