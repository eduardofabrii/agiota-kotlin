@file:OptIn(ExperimentalMaterial3Api::class)

package com.agiotabank.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.Conta
import com.agiotabank.ui.ContaViewModel
import com.agiotabank.ui.InvestimentoViewModel
import com.agiotabank.ui.theme.CardBackground
import com.agiotabank.ui.theme.DarkBackground
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextPrimary
import com.agiotabank.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.pow

// Lista de ativos de exemplo (a mesma da InvestimentoScreen)
private val ativosDisponiveis = listOf(
    "Ação BR" to "Ação",
    "CriptoX" to "Cripto",
    "Fundo Imob" to "FII"
)

@Composable
fun ComprarInvestimentoScreen(
    onVoltar: () -> Unit,
    contaViewModel: ContaViewModel = hiltViewModel(),
    investimentoViewModel: InvestimentoViewModel = hiltViewModel()
) {
    var etapa by remember { mutableStateOf(1) }
    var valor by remember { mutableStateOf("") } // Valor em centavos, como na TransacaoScreen
    var ativoSelecionado by remember { mutableStateOf<Pair<String, String>?>(null) }

    val conta by contaViewModel.contaLogada.collectAsState(initial = null)
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isButtonEnabled by remember { mutableStateOf(false) }
    LaunchedEffect(valor, ativoSelecionado) {
        val valorDouble = valor.toDoubleOrNull() ?: 0.0
        isButtonEnabled = valorDouble > 0 && ativoSelecionado != null
    }

    fun avancar() {
        scope.launch {
            val valorDouble = (valor.toDoubleOrNull() ?: 0.0) / 100.0
            val saldo = conta?.saldo ?: 0.0

            if (etapa == 1) {
                if (valorDouble <= 0.0) {
                    snackbarHostState.showSnackbar("Valor inválido")
                } else if (valorDouble > saldo) {
                    snackbarHostState.showSnackbar("Saldo insuficiente")
                } else {
                    etapa++ // Vai para a confirmação
                }
            } else if (etapa == 2) {
                // Executa a compra
                ativoSelecionado?.let { (nome, tipo) ->
                    investimentoViewModel.comprarInvestimento(
                        nomeAtivo = nome,
                        tipoAtivo = tipo,
                        valorCompra = valorDouble,
                        quantidade = 1.0 // Simplificado: 1 unidade
                    )
                }
                onVoltar() // Volta para a tela anterior
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Investir",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar, modifier = Modifier.size(40.dp)) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                actions = { Spacer(Modifier.size(40.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = DarkBackground,
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = { avancar() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
                    enabled = isButtonEnabled
                ) {
                    Text(if (etapa == 1) "Continuar" else "Confirmar Compra", fontSize = 18.sp)
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
            // Mostra Saldo Disponível (copiado da TransacaoScreen - Etapa 2)
            SaldoDisponivel(conta = conta)

            if (etapa == 1) {
                Etapa1Investir(
                    valor = valor,
                    onValorChange = { valor = it.filter { ch -> ch.isDigit() }.take(12) },
                    ativoSelecionado = ativoSelecionado,
                    onAtivoSelected = { ativoSelecionado = it }
                )
            } else {
                Etapa2ConfirmarInvestimento(
                    valor = valor,
                    ativo = ativoSelecionado
                )
            }
        }
    }
}

@Composable
private fun SaldoDisponivel(conta: Conta?) {
    Column {
        Text("Saldo disponível", color = TextSecondary, fontSize = 14.sp)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            var isHidden by remember { mutableStateOf(false) }
            val saldoFormatado = remember(conta?.saldo) {
                DecimalFormat("R$ #,##0.00").format(conta?.saldo ?: 0.0)
            }
            Text(
                if (isHidden) "R$ ----,--" else saldoFormatado,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(Modifier.width(12.dp))
            IconButton(
                onClick = { isHidden = !isHidden }, modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    Icons.Filled.Visibility, null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun Etapa1Investir(
    valor: String,
    onValorChange: (String) -> Unit,
    ativoSelecionado: Pair<String, String>?,
    onAtivoSelected: (Pair<String, String>) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Dropdown para Ativos
        var expandido by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandido,
                onExpandedChange = { expandido = !expandido },
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = ativoSelecionado?.let { "${it.first} (${it.second})" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Escolha o ativo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandido,
                    onDismissRequest = { expandido = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ativosDisponiveis.forEach { ativo ->
                        DropdownMenuItem(
                            text = { Text("${ativo.first} (${ativo.second})") },
                            onClick = {
                                onAtivoSelected(ativo)
                                expandido = false
                            }
                        )
                    }
                }
            }
        }

        // Input de Valor
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground)
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = onValorChange,
                label = { Text("Valor a investir") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                singleLine = true,
                prefix = { Text("R$ ") },
                visualTransformation = DinheiroVisualTransformation(), // Copiado da TransacaoScreen
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

@Composable
private fun Etapa2ConfirmarInvestimento(
    valor: String,
    ativo: Pair<String, String>?
) {
    val valorFormatado = remember(valor) {
        val cleanValue = valor.filter { it.isDigit() }
        if (cleanValue.isEmpty()) {
            "R$ 0,00"
        } else {
            val valueDouble = cleanValue.toLong() / 100.0
            DecimalFormat("R$ #,##0.00").format(valueDouble)
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Confirmar Investimento",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextPrimary
            )
            InfoRow("Ativo:", ativo?.first ?: "N/A")
            InfoRow("Tipo:", ativo?.second ?: "N/A")
            InfoRow("Valor:", valorFormatado)
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