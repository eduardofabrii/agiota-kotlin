package com.agiotabank.screen

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agiotabank.data.Conta
import com.agiotabank.ui.ContaViewModel
import com.agiotabank.ui.TransacaoViewModel
import com.agiotabank.ui.theme.LightBlue
import com.agiotabank.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.math.pow

enum class TipoChave(val label: String) {
    CPF_CNPJ("CPF/CNPJ"),
    CELULAR("Celular"),
    EMAIL("E-mail"),
    ALEATORIA("Chave Aleatória")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PixScreen(
    goBack: () -> Unit = {},
    conta: Conta?,
    viewModel: TransacaoViewModel,
    contaViewModel: ContaViewModel,
) {
    var etapa by remember { mutableStateOf(1) }
    var valor by remember { mutableStateOf("") }
    var chave by remember { mutableStateOf("") }
    val tiposChave = TipoChave.values().map { it.label }
    var selectedTipoChave by remember { mutableStateOf(tiposChave.first()) }
    var contaDestino by remember { mutableStateOf<Conta?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun voltar() {
        if (etapa > 1) {
            etapa--
        } else {
            goBack()
        }
    }

    fun avancar() {
        scope.launch {
            if (etapa == 1) {
                // Etapa 1: Validar chave
                val contaEncontrada = contaViewModel.findContaByPixKey(chave)
                if (contaEncontrada != null) {
                    if (contaEncontrada.id == conta?.id) {
                        snackbarHostState.showSnackbar("Você não pode enviar um PIX para si mesmo")
                        return@launch
                    }
                    contaDestino = contaEncontrada
                    etapa++
                } else {
                    // Se não encontrou, simula uma conta externa (não bloqueia a transação)
                    contaDestino = Conta(
                        id = -1L, // ID inválido para sabermos que é externo
                        nome = "Destinatário (Outra Instituição)",
                        email = "", senha = "", cpf = "", telefone = "",
                        agencia = "0001", numero = "999999-9"
                    )
                    Log.d("PixScreen", "Chave PIX não encontrada localmente, tratando como externa.")
                    etapa++ // Avança para a etapa de valor
                }
            } else if (etapa == 2) {
                // Etapa 2: Validar valor
                val valorDouble = valor.toDoubleOrNull()?.div(100.0) ?: 0.0
                if (valorDouble <= 0.0) {
                    snackbarHostState.showSnackbar("Valor Inválido")
                } else if (valorDouble > (conta?.saldo ?: 0.0)) {
                    snackbarHostState.showSnackbar("Saldo Insuficiente")
                } else {
                    etapa++ // Avança para a etapa de confirmação
                }
            } else if (etapa == 3) {
                // Etapa 3: Confirmar e realizar transação
                conta?.let { de ->
                    contaDestino?.let { para ->
                        // A sua TransacaoViewModel já lida com a lógica de
                        // debitar da conta 'de' e creditar na conta 'para'
                        viewModel.realizarTransacao(de, para, valor.toDouble() / 100.0)
                        goBack()
                    } ?: run {
                        scope.launch { snackbarHostState.showSnackbar("Destinatário inválido") }
                    }
                } ?: run {
                    scope.launch { snackbarHostState.showSnackbar("Conta logada não encontrada") }
                }
            }
        }
    }

    BackHandler {
        voltar()
    }

    var isButtonEnabled by remember { mutableStateOf(false) }
    fun toggleButton(boolean: Boolean) {
        isButtonEnabled = boolean
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("PIX") },
                navigationIcon = {
                    IconButton(onClick = { voltar() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                ),
                actions = {
                    IconButton(onClick = { /* Alertas */ }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Alertas")
                    }
                    IconButton(onClick = { /* Histórico */ }) {
                        Icon(Icons.Filled.History, contentDescription = "Histórico")
                    }
                    Box(
                        Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(LightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(conta?.nome?.firstOrNull()?.toString() ?: "C")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Button(
                    onClick = { avancar() },
                    shape = RoundedCornerShape(12.dp),
                    enabled = isButtonEnabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                ) {
                    Text(if (etapa == 3) "Confirmar" else "Continuar", fontSize = 18.sp)
                }
            }
        }) { paddingValues ->

        when (etapa) {
            1 -> Etapa1Pix(
                paddingValues = paddingValues,
                tiposChave = tiposChave,
                selectedTipoChave = selectedTipoChave,
                onTipoChaveSelected = {
                    selectedTipoChave = it
                    chave = "" // Limpa a chave ao trocar o tipo
                },
                chave = chave,
                onChaveChange = { chave = it },
                toggleButton = { toggleButton(it) }
            )

            2 -> Etapa2Valor( // Reutilizado de TransacaoScreen
                paddingValues = paddingValues,
                valor = valor,
                onValorChange = { valor = it },
                toggleButton = { toggleButton(it) },
                saldoDisponivel = conta?.saldo
            )

            3 -> Etapa3Pix( // Nova tela de confirmação
                paddingValues = paddingValues,
                tipoChave = selectedTipoChave,
                chave = chave,
                valor = valor,
                destinatario = contaDestino
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Etapa1Pix(
    paddingValues: PaddingValues,
    tiposChave: List<String>,
    selectedTipoChave: String,
    onTipoChaveSelected: (String) -> Unit,
    chave: String,
    onChaveChange: (String) -> Unit,
    toggleButton: (Boolean) -> Unit
) {
    // Habilita o botão se a chave não estiver vazia
    DisposableEffect(chave) {
        toggleButton(chave.isNotBlank())
        onDispose { }
    }

    // Determina a transformação visual e o tipo de teclado
    val visualTransformation = when (selectedTipoChave) {
        TipoChave.CPF_CNPJ.label -> CpfCnpjVisualTransformation()
        TipoChave.CELULAR.label -> CelularVisualTransformation()
        else -> VisualTransformation.None
    }

    val keyboardType = when (selectedTipoChave) {
        TipoChave.CPF_CNPJ.label, TipoChave.CELULAR.label -> KeyboardType.Number
        TipoChave.EMAIL.label -> KeyboardType.Email
        else -> KeyboardType.Text
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Para quem você quer transferir via PIX?")

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            var expandido by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Dropdown para TIPO DE CHAVE
                ExposedDropdownMenuBox(
                    expanded = expandido,
                    onExpandedChange = { expandido = !expandido },
                ) {
                    OutlinedTextField(
                        value = selectedTipoChave,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de Chave") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
                        modifier = Modifier
                            .menuAnchor(
                                ExposedDropdownMenuAnchorType.PrimaryNotEditable, true
                            )
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expandido,
                        onDismissRequest = { expandido = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tiposChave.forEach { tipo ->
                            DropdownMenuItem(text = {
                                Text(text = tipo)
                            }, onClick = {
                                onTipoChaveSelected(tipo)
                                expandido = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))

                // Input da CHAVE com máscara dinâmica
                OutlinedTextField(
                    value = chave,
                    onValueChange = {
                        // Filtra os inputs
                        val newText = if (keyboardType == KeyboardType.Number) {
                            it.filter { ch -> ch.isDigit() }
                        } else {
                            it
                        }
                        onChaveChange(newText)
                    },
                    label = { Text("Chave") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = visualTransformation,
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                )
            }
        }
    }
}

// Esta é a Etapa2 da sua TransacaoScreen, perfeitamente reutilizável.
@Composable
fun Etapa2Valor(
    paddingValues: PaddingValues,
    valor: String,
    onValorChange: (String) -> Unit,
    toggleButton: (Boolean) -> Unit = {},
    saldoDisponivel: Double?
) {
    DisposableEffect(valor) {
        val valorDouble = valor.toDoubleOrNull() ?: 0.0
        toggleButton(valor.isNotEmpty() && valorDouble > 0)
        onDispose { }
    }
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
                val saldoFormatado = remember(saldoDisponivel) {
                    saldoDisponivel?.let { DecimalFormat("R$ #,##0.00").format(it) } ?: "R$ ----,--"
                }
                Text(
                    if (isHidden) "R$ ----,--" else saldoFormatado,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(12.dp))
                IconButton(
                    onClick = { isHidden = !isHidden }, modifier = Modifier.size(24.dp)
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
        Text("Qual o valor da transferência?")
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            OutlinedTextField(
                value = valor,
                onValueChange = { onValorChange(it.filter { ch -> ch.isDigit() }.take(12)) },
                label = { Text("Valor") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                singleLine = true,
                prefix = { Text("R$ ") },
                visualTransformation = DinheiroVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            )
        }
    }
}

// Nova Etapa 3, específica para PIX
@Composable
fun Etapa3Pix(
    paddingValues: PaddingValues,
    tipoChave: String,
    chave: String,
    valor: String,
    destinatario: Conta?
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

    // Formata a chave para exibição
    val chaveFormatada = remember(chave, tipoChave) {
        when (tipoChave) {
            TipoChave.CPF_CNPJ.label -> CpfCnpjVisualTransformation().filter(AnnotatedString(chave)).text.text
            TipoChave.CELULAR.label -> CelularVisualTransformation().filter(AnnotatedString(chave)).text.text
            else -> chave
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text("Confirme os dados da sua transferência", fontSize = 18.sp, fontWeight = FontWeight.Medium)
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Valor: $valorFormatado", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Divider()
                InfoRow(label = "Para:", value = destinatario?.nome ?: "Não identificado")
                InfoRow(label = "Instituição:", value = if (destinatario?.id == -1L) "Outra Instituição" else "AgiotaBank")
                InfoRow(label = tipoChave, value = chaveFormatada)

                // Mostra agência e conta apenas se for um usuário interno (encontrado no DB)
                if (destinatario?.id != -1L) {
                    InfoRow(label = "Agência:", value = destinatario?.agencia ?: "----")
                    InfoRow(label = "Conta:", value = destinatario?.numero ?: "----")
                }
            }
        }
    }
}

// --- COMPONENTES E MÁSCARAS ---
// (Coloque isso no final do seu arquivo PixScreen.kt)

@Composable
private fun InfoRow(label: String, value: String) {
    Column(Modifier.fillMaxWidth()) {
        Text(label, fontSize = 14.sp, color = TextSecondary)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun Divider() {
    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)))
}

/**
 * VisualTransformation para formatar um número de celular: (##) #####-####
 */
class CelularVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(11)
        if (trimmed.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val out = buildString {
            if (trimmed.length >= 1) {
                append("(${trimmed.substring(0, minOf(trimmed.length, 2))}")
            }
            if (trimmed.length > 2) {
                append(") ${trimmed.substring(2, minOf(trimmed.length, 7))}")
            }
            if (trimmed.length > 7) {
                append("-${trimmed.substring(7)}")
            }
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return offset
                if (offset <= 2) return offset + 1
                if (offset <= 7) return offset + 3
                if (offset <= 11) return offset + 4
                return out.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset - 1
                if (offset <= 10) return offset - 3
                if (offset <= 15) return offset - 4
                return trimmed.length
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

/**
 * VisualTransformation para formatar CPF/CNPJ.
 * Formata CPF: ###.###.###-## (até 11 dígitos)
 * Formata CNPJ: ##.###.###/####-## (mais de 11 dígitos)
 */
class CpfCnpjVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { it.isDigit() }.take(14) // Limite de 14 (CNPJ)
        if (trimmed.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val isCpf = trimmed.length <= 11
        val out: String
        val offsetTranslator: OffsetMapping

        if (isCpf) {
            // Formatar como CPF: ###.###.###-##
            out = buildString {
                if (trimmed.length >= 1) {
                    append(trimmed.substring(0, minOf(trimmed.length, 3)))
                }
                if (trimmed.length > 3) {
                    append(".${trimmed.substring(3, minOf(trimmed.length, 6))}")
                }
                if (trimmed.length > 6) {
                    append(".${trimmed.substring(6, minOf(trimmed.length, 9))}")
                }
                if (trimmed.length > 9) {
                    append("-${trimmed.substring(9)}")
                }
            }
            offsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 0) return offset
                    if (offset <= 3) return offset
                    if (offset <= 6) return offset + 1
                    if (offset <= 9) return offset + 2
                    if (offset <= 11) return offset + 3
                    return out.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 0) return offset
                    if (offset <= 4) return offset
                    if (offset <= 8) return offset - 1
                    if (offset <= 12) return offset - 2
                    if (offset <= 14) return offset - 3
                    return trimmed.length
                }
            }
        } else {
            // Formatar como CNPJ: ##.###.###/####-##
            out = buildString {
                if (trimmed.length >= 1) {
                    append(trimmed.substring(0, minOf(trimmed.length, 2)))
                }
                if (trimmed.length > 2) {
                    append(".${trimmed.substring(2, minOf(trimmed.length, 5))}")
                }
                if (trimmed.length > 5) {
                    append(".${trimmed.substring(5, minOf(trimmed.length, 8))}")
                }
                if (trimmed.length > 8) {
                    append("/${trimmed.substring(8, minOf(trimmed.length, 12))}")
                }
                if (trimmed.length > 12) {
                    append("-${trimmed.substring(12)}")
                }
            }
            offsetTranslator = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 0) return offset
                    if (offset <= 2) return offset
                    if (offset <= 5) return offset + 1
                    if (offset <= 8) return offset + 2
                    if (offset <= 12) return offset + 3
                    if (offset <= 14) return offset + 4
                    return out.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    if (offset <= 0) return offset
                    if (offset <= 2) return offset
                    if (offset <= 6) return offset - 1
                    if (offset <= 10) return offset - 2
                    if (offset <= 15) return offset - 3
                    if (offset <= 18) return offset - 4
                    return trimmed.length
                }
            }
        }

        return TransformedText(AnnotatedString(out), offsetTranslator)
    }
}

