package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agiotabank.ui.theme.AgiotaBankTheme
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary

@Composable
@Preview
fun TransacaoScreen(goBack: () -> Unit = {}) {
    AgiotaBankTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ícone do pacote material.icons
                            contentDescription = "Voltar",
                            tint = TextPrimary
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = { /* RF-020: Alertas */ }) {
                            Icon(Icons.Filled.Notifications, null, tint = TextPrimary)
                        }
                        IconButton(onClick = { /* RF-019: Histórico de acessos */ }) {
                            Icon(Icons.Filled.History, null, tint = TextPrimary)
                        }
                        Box(
                            Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(LightBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("C", color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column {
                    Text("Saldo disponível", color = TextSecondary, fontSize = 14.sp)
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        var isHidden by remember { mutableStateOf(false) }
                        if (isHidden) {
                            Text(
                                "R$ ----,--",
                                color = TextPrimary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        } else {
                            Text(
                                "R$ 3.363,32",
                                color = TextPrimary,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                        IconButton(
                            onClick = { isHidden = !isHidden },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Filled.Visibility,
                                null,
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}