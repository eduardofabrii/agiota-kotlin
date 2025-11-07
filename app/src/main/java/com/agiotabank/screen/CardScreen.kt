package com.agiotabank.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardScreen(goBack: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cartão",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { goBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = { /* RF-020: Alertas */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Alertas")
                    }
                    IconButton(onClick = { /* RF-019: Histórico de acessos */ }) {
                        Icon(Icons.Filled.History, contentDescription = "Histórico")
                    }
                    Box(
                        Modifier
                            .padding(bottom = 20.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightBlue)
                            .padding(2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("C", fontWeight = FontWeight.Bold)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            )
        }
    ) { contentPadding ->
        // Top bar simples, alinhado ao seu padrão

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Cartão (estático)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.6f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(Modifier
                    .fillMaxSize()
                    .padding(18.dp)) {
                    // Fundo com "brilho" em gradiente usando apenas as cores do tema
                    val glow = Brush.linearGradient(
                        listOf(
                            LightBlue.copy(alpha = 0.25f),
                            Color.Transparent
                        )
                    )
                    Box(
                        Modifier
                            .matchParentSize()
                            .background(glow)
                    )

                    // Marca + ícone
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            Modifier.size(36.dp),
                            shape = CircleShape,
                            color = LightBlue.copy(alpha = 0.15f)
                        ) {}
                        Icon(Icons.Filled.CreditCard, null, tint = LightBlue)
                    }

                    // Número mascarado + nome
                    Column(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(top = 8.dp)
                    ) {
                        CardNumber(masked = "••••  ••••  ••••  1234")
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "nome do tio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Validade + etiqueta de modalidade
                    Row(
                        Modifier.align(Alignment.BottomStart),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Válido até", color = TextSecondary, fontSize = 12.sp)
                        Spacer(Modifier.width(8.dp))
                        Text("12/28", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Text(
                        text = "crédito • débito",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }
            }

            // Ações do cartão (somente UI)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CardAction(icon = Icons.Filled.Lock, label = "Bloquear")
                CardAction(icon = Icons.Filled.PhoneIphone, label = "Virtual")
                CardAction(icon = Icons.Filled.ChevronRight, label = "Ajustes")
                CardAction(icon = Icons.Filled.MoreHoriz, label = "Mais")
            }

            // Fatura atual (estática)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.CreditCard, null, tint = LightBlue)
                        Text("Fatura atual", color = TextSecondary, fontSize = 13.sp)
                    }
                    Text("R$ 563,00", fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                    LinearProgressIndicator(
                        progress = { 0.42f },
                        modifier = Modifier.fillMaxWidth(),
                        color = LightBlue,
                        trackColor = CardBackground.copy(alpha = 0.6f)
                    )
                    Text("Limite disponível: R$ 2.765,44", color = TextSecondary, fontSize = 12.sp)
                }
            }

            // Benefícios/atalhos simples (estático)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SmallInfoCard(
                    title = "Cartão virtual",
                    subtitle = "Use em compras online",
                    modifier = Modifier.weight(1f)
                )
                SmallInfoCard(
                    title = "Ajustar limite",
                    subtitle = "Defina um teto",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CardNumber(masked: String) {
    val text = buildAnnotatedString {
        val chunks = masked.split(" ")
        chunks.forEachIndexed { idx, chunk ->
            withStyle(
                SpanStyle(
                    letterSpacing = 2.sp,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(chunk)
            }
            if (idx != chunks.lastIndex) append("  ")
        }
    }
    Text(text = text, fontSize = 18.sp)
}

@Composable
private fun CardAction(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(Modifier.size(56.dp), shape = CircleShape) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = TextPrimary)
            }
        }
        Text(label, color = TextSecondary, fontSize = 12.sp)
    }
}

@Composable
private fun SmallInfoCard(title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = TextSecondary, fontSize = 12.sp, maxLines = 2)
        }
    }
}

@Preview(
    name = "Card Screen — Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewCardDark() {
    AgiotaBankTheme(darkTheme = true, dynamicColor = false) {
        CardScreen()
    }
}

// ————————————————————————————————————————————————————————————————
// Navegação (exemplo simples) — Home -> Card
// (Use Navigation-Compose OU um estado local na Home)
// ————————————————————————————————————————————————————————————————
/*
// 1) Altere a assinatura do HomeScreen
@Composable
fun HomeScreen(onOpenCard: () -> Unit) { /* ... */ }

// 2) Dentro do seu card "Cartão de crédito" troque o ícone final:
IconButton(onClick = onOpenCard) {
    Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = TextSecondary)
}

// 3) Sem Navigation-Compose, faça dentro do bloco "home" da MainActivity:
var homeRoute by remember { mutableStateOf("home") }
when (homeRoute) {
    "home" -> HomeScreen(onOpenCard = { homeRoute = "card" })
    "card" -> CardScreen()
}
*/