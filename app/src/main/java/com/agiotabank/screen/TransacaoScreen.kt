package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TransacaoScreen(goBack: () -> Unit = {}) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(WindowInsets.systemBars.asPaddingValues()),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { goBack() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Ícone do pacote material.icons
                        contentDescription = "Voltar"
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
                        Text("C", fontWeight = FontWeight.Bold)
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
                Text("Saldo disponível", fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var isHidden by remember { mutableStateOf(false) }
                    if (isHidden) {
                        Text(
                            "R$ ----,--",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    } else {
                        Text(
                            "R$ 3.363,32",
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
            Text("Dados de destino")

            Column (modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12)
                ) {
                    val options = mapOf<String, String>(
                        "Agibank S.A." to "019",
                        "Banco do Brasil" to "001",
                        "Banco Inter S.A." to "077",
                    )
                    var selected by remember { mutableStateOf(options.keys.first()) }
                    var expandido by remember { mutableStateOf(false) }
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)){
                        ExposedDropdownMenuBox(
                            expanded = expandido,
                            onExpandedChange = { expandido = !expandido },
                        ) {
                            TextField(
                                value = "$selected - ${options[selected]}",
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Banco") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandido,
                                onDismissRequest = { expandido = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                options.keys.forEach { banco ->
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = "$banco - ${options[banco]}",
                                            )
                                        },
                                        onClick = {
                                            selected = banco
                                            expandido = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        val tipos = listOf("Conta Corrente", "Conta Poupança")
                        var tipo by remember { mutableStateOf("") }
                        var tipoExpandido by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = tipoExpandido,
                            onExpandedChange = { tipoExpandido = !tipoExpandido },
                        ) {
                            TextField(
                                value = tipo,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Tipo de conta") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(tipoExpandido) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = tipoExpandido,
                                onDismissRequest = { tipoExpandido = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                tipos.forEach { tipoConta ->
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = tipoConta,
                                            )
                                        },
                                        onClick = {
                                            tipo = tipoConta
                                            tipoExpandido = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        var agencia by remember { mutableStateOf("") }
                        TextField(
                            value = agencia,
                            onValueChange = {agencia = it},
                            label = { Text("Agência") },
                            modifier = Modifier.fillMaxWidth()

                        )
                        Spacer(Modifier.height(8.dp))
                        var conta by remember { mutableStateOf("") }
                        TextField(
                            value = conta,
                            onValueChange = {conta = it},
                            label = { Text("Conta") },
                            modifier = Modifier.fillMaxWidth()

                        )

                    }


                }
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                ) {
                    Text("Continuar", fontSize = 18.sp)
                }
            }
        }
    }
}